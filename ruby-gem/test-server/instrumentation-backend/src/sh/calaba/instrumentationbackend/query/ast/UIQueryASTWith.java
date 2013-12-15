package sh.calaba.instrumentationbackend.query.ast;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import org.antlr.runtime.tree.CommonTree;

import sh.calaba.instrumentationbackend.actions.webview.QueryHelper;
import sh.calaba.instrumentationbackend.actions.webview.UnableToFindChromeClientException;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;

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
	public List evaluateWithViews(final List inputViews, final UIQueryDirection direction,
			final UIQueryVisibility visibility) {
		
		List queryResult = (List) UIQueryUtils.evaluateSyncInMainThread(new Callable() {
			
			@Override
			public Object call() throws Exception {
				List futureResult = new ArrayList(8);

				for (int i = 0; i < inputViews.size(); i++) {
					Object o = inputViews.get(i);

					if (o instanceof WebView && isDomQuery()) {
						Future webResult = evaluateForWebView((WebView) o);
						if (webResult != null) {
							futureResult.add(webResult);
						}
					}
					else if (o instanceof Map) {
						Map result = evaluateForMap((Map) o);
						if (result != null) {
							futureResult.add(result);
						}
						
					}
					else {
						Object result = evaluateForObject(o, i);
						if (result != null) {
							futureResult.add(result);
						}
					}

				}
				List visibilityFilteredResults = visibility.evaluateWithViews(futureResult, direction,
						visibility);
				return new PartialFutureList(visibilityFilteredResults);
			}
		});
		
		List processedResult = new ArrayList(queryResult.size());
		for (Object o : queryResult) {
			if (o instanceof Map) {
				Map m = (Map) o;
				if (m.containsKey("result")) {
					processedResult.addAll(UIQueryUtils.mapWebViewJsonResponse((String) m.get("result"),(WebView) m.get("webView")));	
				}
				else {
					processedResult.add(m);
				}
				
			}
			else {
				processedResult.add(o);
			}
		}
		return processedResult;
		

	}

    private boolean isDomQuery() {
        System.out.println("isDomQuery: " + propertyName);
        return propertyName.equalsIgnoreCase("css") || propertyName.equalsIgnoreCase("xpath");
    }


    @SuppressWarnings("rawtypes")
	private Map evaluateForMap(Map map) {		
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
	private Future evaluateForWebView(WebView o) {
		if (!(this.value instanceof String)) {
			return null;
		}
		try {
			return QueryHelper.executeAsyncJavascriptInWebviews(o,
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
