package sh.calaba.instrumentationbackend.query.ast;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

import org.antlr.runtime.tree.CommonTree;

import sh.calaba.instrumentationbackend.actions.webview.QueryHelper;
import sh.calaba.instrumentationbackend.actions.webview.UnableToFindChromeClientException;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import sh.calaba.instrumentationbackend.query.CompletedFuture;
import sh.calaba.instrumentationbackend.query.WebContainer;

public class UIQueryASTWith implements UIQueryAST {
	public final String propertyName;
	public final Object value;

	public UIQueryASTWith(String property, Object value) {
		if (property == null) {
			throw new IllegalArgumentException(
					"Cannot instantiate Filter with null property name");
		}
		this.propertyName = property;
		this.value = value;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override

    public List evaluateWithViews(final List inputViews,
                                  final UIQueryDirection direction, final UIQueryVisibility visibility) {

        try {
            List<Future<?>> futureResults = new ArrayList<Future<?>>();
            int index = 0;
            for (Object o : inputViews) {
                if (o instanceof View) {
                    View view = (View) o;
                    FutureTask<Future> march = new FutureTask<Future>(new MatchForViews(view, index));
                    UIQueryUtils.runOnViewThread(view, march);
                    futureResults.add(march);
                } else {
                    Future future = UIQueryUtils.evaluateAsyncInMainThread(new MatchForViews(o, index));
                    futureResults.add(future);
                }
                index++;
            }


            final List processedResult = new ArrayList(futureResults.size());

            for (Future<?> f : futureResults) {
                Object o;
                Object rawResult = f.get(10, TimeUnit.SECONDS);

                // The result will either be the actual result or another future
                if (rawResult instanceof Future) {
                    Future futureResult = (Future) rawResult;
                    o = futureResult.get(10, TimeUnit.SECONDS);
                } else {
                    o = rawResult;
                }

                if(o == null) {
                    continue;
                } else if (o instanceof Map) {
                    Map m = (Map) o;
                    if (m.containsKey("result")) {
                        List<Map<String, Object>> results =
                                UIQueryUtils.mapWebContainerJsonResponseOnViewThread((String) m.get("result"),
                                        (WebContainer) m.get("calabashWebContainer")).get(10, TimeUnit.SECONDS);

                        for (Map<String, Object> result : results) {
                            if (result.containsKey("error")) {
                                if (result.containsKey("details")) {
                                    throw new InvalidUIQueryException(result.get("error") + ". " + result.get("details"));
                                } else {
                                    throw new InvalidUIQueryException(result.get("error").toString());
                                }
                            }
                        }

                        processedResult.addAll(results);
                    }
                    else {
                        processedResult.add(m);
                    }
                }
                else {
                    processedResult.add(o);
                }
            }

            List visibilityFilteredResults = visibility.evaluateWithViews(processedResult, direction, visibility);
            return visibilityFilteredResults;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private class MatchForViews implements Callable<Future> {
        private final Object o;
        private final int index;

        MatchForViews(Object o, int index) {
            this.o = o;
            this.index = index;
        }

        public Future call() throws Exception {
            if (o instanceof View && isDomQuery()) {
                View view = (View) o;

                Future webResult = evaluateForWebContainer(new WebContainer(view));

                if (webResult != null) {
                    return webResult;
                }
            } else if (o instanceof Map) {
                Map result = evaluateForMap((Map) o, index);
                if (result != null) {
                    return new CompletedFuture(result);
                }

            } else {
                Object result = evaluateForObject(o, index);
                if (result != null) {
                    return new CompletedFuture(result);
                }
            }
            return new CompletedFuture(null);
        }
    }

    private boolean isDomQuery() {
        System.out.println("isDomQuery: " + propertyName);
        return propertyName.equalsIgnoreCase("css") || propertyName.equalsIgnoreCase("xpath");
    }


    @SuppressWarnings("rawtypes")
	private Map evaluateForMap(Map map, int index) {
        if (this.propertyName.equals("index") && this.value.equals(index)) {
            return map;
        }

		if (map.containsKey(this.propertyName)) {											
			Object value = map.get(this.propertyName);
			if (value == this.value || (value != null && value.equals(this.value))) {
				return map;
			}			
		} 
		return null;
	}

	private Object evaluateForObject(Object o, int index) {
		if (this.propertyName.equals("id") && hasId(o, this.value)) {
			return o;
		} else if (this.propertyName.equals("marked")
				&& isMarked(o, this.value)) {
			return o;
		} else if (this.propertyName.equals("index")
				&& this.value.equals(index)) {
			return o;
		} else {

			Method propertyAccessor = UIQueryUtils.hasProperty(o,
					this.propertyName);
			if (propertyAccessor != null) {
				Object value = UIQueryUtils.getProperty(o, propertyAccessor);

				if (value == this.value
						|| (value != null && value.equals(this.value))) {
					return o;
				} else if (this.value instanceof String
						&& value != null && this.value.equals(value.toString())) {
					return o;
				}
			}
		}
		return null;

	}

	@SuppressWarnings({ "rawtypes" })
	private Future evaluateForWebContainer(WebContainer webContainer) {
		if (!(this.value instanceof String)) {
			return null;
		}
		try {
			return QueryHelper.executeAsyncJavascriptInWebContainer(webContainer,
					"calabash.js", (String) this.value,this.propertyName);
				
		} catch (UnableToFindChromeClientException e) {
			Log.w("Calabash","Unable to find UnableToFindChromeClientException");
			return null;
		}
				
	}

	private boolean hasId(Object o, Object expectedValue) {
		if (!(o instanceof View)) {
			return false;
		}
		if (!(expectedValue instanceof String)) {
			return false;
		}
		View view = (View) o;
		String expected = (String) expectedValue;
		String id = UIQueryUtils.getId(view);
		return (id != null && id.equals(expected));
	}

	private boolean isMarked(Object o, Object expectedValue) {
		if (!(o instanceof View)) {
			return false;
		}
		if (!(expectedValue instanceof String)) {
			return false;
		}
		View view = (View) o;
		String expected = (String) expectedValue;

		if (hasId(o, expectedValue)) {
			return true;
		}

		CharSequence contentDescription = view.getContentDescription();
		if (contentDescription != null
				&& contentDescription.toString().equals(expected)) {
			return true;
		}

		try {
			Method getTextM = view.getClass().getMethod("getText");
			Object text = getTextM.invoke(view);
			if (text != null && text.toString().equals(expected)) {
				return true;
			}

		} catch (Exception e) {
		}

		return false;

	}

	public static UIQueryASTWith fromAST(CommonTree step) {
		CommonTree prop = (CommonTree) step.getChild(0);
		CommonTree val = (CommonTree) step.getChild(1);
		
		Object parsedVal = UIQueryUtils.parseValue(val);
		return new UIQueryASTWith(prop.getText(), parsedVal);
	}

	@Override
	public String toString() {
		return "With[" + this.propertyName + ":" + this.value + "]";
	}

}
