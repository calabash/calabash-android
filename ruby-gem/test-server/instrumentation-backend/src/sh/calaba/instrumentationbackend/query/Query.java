package sh.calaba.instrumentationbackend.query;

import static sh.calaba.instrumentationbackend.InstrumentationBackend.viewFetcher;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.query.ast.UIQueryEvaluator;
import sh.calaba.instrumentationbackend.query.ast.UIQueryUtils;
import android.os.ConditionVariable;
import android.view.View;

public class Query {

	private String queryString;
	@SuppressWarnings("rawtypes")
	private List arguments;

	public Query(String queryString) {
		this.queryString = queryString;
		this.arguments = Collections.EMPTY_LIST;
		if (this.queryString == null || this.queryString.trim().equals("")) {
			throw new IllegalArgumentException("Illegal query: "
					+ this.queryString);
		}
	}

	@SuppressWarnings("rawtypes")
	public Query(String queryString, List args) {
		this(queryString);
		this.arguments = args;
	}

	@SuppressWarnings("rawtypes")
	public List executeInMainThread(final boolean includeInvisible) {

		final AtomicReference<List> result = new AtomicReference<List>();
		final AtomicReference<Throwable> resultErr = new AtomicReference<Throwable>();

		final ConditionVariable computationFinished = new ConditionVariable();

		InstrumentationBackend.instrumentation.runOnMainSync(new Runnable() {
			@Override
			public void run() {
				try {
					List resultList = execute(includeInvisible,
							computationFinished);
					result.set(resultList);
					boolean noAsyncResult = true;
					for (Object o : resultList) {
						if (o instanceof AtomicReference) {
							noAsyncResult = false;
							break;
						}
					}
					if (noAsyncResult) {
						computationFinished.open();
					}
				} catch (Throwable t) {
					resultErr.set(t);
				}
			}
		});
		computationFinished.block(10000);
		if (resultErr.get() == null) {
			return postProcessQueryResult(result.get());
		}
		throw new RuntimeException(resultErr.get());

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List postProcessQueryResult(final List views) {
		final AtomicReference<List> resultRef = new AtomicReference<List>();
		final List methods = this.arguments;
		InstrumentationBackend.instrumentation.runOnMainSync(new Runnable() {
			@Override
			public void run() {
				List expandedViews = expandViews(views);
				mapViews(expandedViews, methods, resultRef);
			}
		});

		return resultRef.get();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void mapViews(List expandedViews, List methods,
			AtomicReference<List> resultRef) {
		List result = expandedViews;
		

		for (Object methodNameObj : methods) {
			String propertyName = (String) methodNameObj;
			List nextResult = new ArrayList(result.size());
			for (Object o : result) {
				try {
					if (o instanceof Map) {
						Map objAsMap = (Map) o;
						if (objAsMap.containsKey(propertyName)) {
							Map<String,Object> rect = (Map<String, Object>) objAsMap.get("rect");
							
							
							nextResult.add(objAsMap.get(propertyName));
						} else {
							nextResult.add(UIQueryResultVoid.instance.asMap(
									propertyName, o, "No key for "
											+ propertyName + ". Keys: "
											+ (objAsMap.keySet().toString())));
						}
					} else {
						if (o instanceof View && "id".equals(propertyName)) {
							nextResult.add(UIQueryUtils.getId((View) o));
						} else {
							Method m = UIQueryUtils
									.hasProperty(o, propertyName);
							if (m != null) {
								nextResult.add(m.invoke(o));
							} else {
								nextResult.add(UIQueryResultVoid.instance
										.asMap(propertyName, o,
												"NO accessor for "
														+ propertyName));
							}
						}
					}

				} catch (Exception e) {
					System.out.println(e.getMessage());
					nextResult.add(UIQueryResultVoid.instance.asMap(
							propertyName, o, e.getMessage()));
				}
			}
			result = nextResult;
		}
		
		List finalResult = new ArrayList(result.size());
		for (Object o : result) {
			if (o instanceof View)  {
				finalResult.add(ViewMapper.extractDataFromView((View) o));
			}
			else {//assume JSON serializable in this case
				finalResult.add(o);
			}
		}
		
		resultRef.set(finalResult);

	}

	@SuppressWarnings({ "rawtypes" })
	public List execute(boolean includeInvisible,
			ConditionVariable computationFinished) {
		long before = System.currentTimeMillis();
		List queryResults = UIQueryEvaluator.evaluateQueryWithOptions(
				this.queryString, rootViews(), this.arguments,
				computationFinished);
		long after = System.currentTimeMillis();

		String action = "EvaluateQuery";
		System.out.println(action + " took: " + (after - before) + "ms");

		/*
		 * for (Object v : queryResults) { if (includeInvisible ||
		 * UIQueryUtils.isVisible(viewFetcher, v)) { result.add(); } }
		 */
		return queryResults;// TODO
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List expandViews(List inputViews) {
		List expandedViews = new ArrayList(inputViews.size());
		for (Object o : inputViews) {
			if (o instanceof AtomicReference) {
				List<Map<String,Object>> refVal = ((AtomicReference<List<Map<String,Object>>>) o).get();
				if (refVal == null) {
					System.err
							.println("Query produced no results asynchronously");
					continue;
				}
				expandedViews.addAll(refVal);
				
			} else {
				expandedViews.add(o);
			}
		}
		return expandedViews;
	}

	public List<View> allVisibleViews() {
		return viewFetcher.getAllViews(false);
	}

	public List<View> rootViews() {
		return Arrays.asList(viewFetcher.getWindowDecorViews());
	}

	private static class UIQueryResultVoid {
		public static final UIQueryResultVoid instance = new UIQueryResultVoid();

		private UIQueryResultVoid() {
		}

		@SuppressWarnings({ "rawtypes", "unchecked" })
		public Object asMap(String methodName, Object receiver,
				String errorMessage) {
			Map map = new HashMap();
			map.put("error", errorMessage);
			map.put("methodName", methodName);
			map.put("receiverClass", receiver.getClass().getName());
			map.put("receiverString", receiver.toString());
			return map;
		}
	}

}
