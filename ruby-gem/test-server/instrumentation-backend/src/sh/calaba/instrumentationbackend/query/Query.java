package sh.calaba.instrumentationbackend.query;

import static sh.calaba.instrumentationbackend.InstrumentationBackend.viewFetcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.query.ast.UIQueryEvaluator;
import sh.calaba.instrumentationbackend.query.ast.UIQueryUtils;
import android.os.ConditionVariable;
import android.os.Looper;
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
					result.set(execute(includeInvisible, computationFinished));
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

	@SuppressWarnings("rawtypes")
	private List postProcessQueryResult(List list) {
		// TODO Auto-generated method stub
		return list;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List execute(boolean includeInvisible, ConditionVariable computationFinished) {
		List result = new ArrayList();

		long before = System.currentTimeMillis();
		List queryResults = UIQueryEvaluator.evaluateQueryWithOptions(
				this.queryString, rootViews(), this.arguments, computationFinished);
		long after = System.currentTimeMillis();

		String action = "EvaluateQuery";
		System.out.println(action + " took: " + (after - before) + "ms");

/*		for (Object v : queryResults) {
			if (includeInvisible || UIQueryUtils.isVisible(viewFetcher, v)) {
				result.add(ViewMapper.extractDataFromView(v));
			}
		}
*/
		return queryResults;//TODO
	}

	public List<View> allVisibleViews() {
		return viewFetcher.getAllViews(false);
	}

	public List<View> rootViews() {
		return Arrays.asList(viewFetcher.getWindowDecorViews());
	}
}
