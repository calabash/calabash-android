package sh.calaba.instrumentationbackend.query;

import static sh.calaba.instrumentationbackend.InstrumentationBackend.viewFetcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.actions.Operation;
import sh.calaba.instrumentationbackend.query.ast.UIQueryEvaluator;
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

	@SuppressWarnings({ "rawtypes" })
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
		

		for (Object methodName : methods) {			
			List nextResult = new ArrayList(result.size());
			for (Object o : result) {
				Operation op = null;
				if (methodName instanceof Operation) {
					op = (Operation) methodName;												
				}
				if (methodName instanceof String) {
					op = new PropertyOperation((String) methodName);	
				}					
				nextResult.add(op.apply(o));								
			}
			result = nextResult;
		}
		
		List finalResult = new ArrayList(result.size());
		for (Object o : result) {
			finalResult.add(ViewMapper.mapView(o));
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


}
