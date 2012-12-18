package sh.calaba.instrumentationbackend.query;

import static sh.calaba.instrumentationbackend.InstrumentationBackend.viewFetcher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.query.ast.UIQueryEvaluator;
import sh.calaba.instrumentationbackend.query.ast.UIQueryUtils;
import android.os.Looper;
import android.view.View;

public class Query {

    private String queryString;
	@SuppressWarnings("rawtypes")
	private List arguments;

    public Query(String queryString) {        
        this.queryString = queryString;
        this.arguments = Collections.EMPTY_LIST;
        if (this.queryString == null || this.queryString.trim().equals("")) 
        {
        	throw new IllegalArgumentException("Illegal query: "+this.queryString);
        }
    }

    @SuppressWarnings("rawtypes")
	public Query(String queryString,List args) {
        this(queryString);
        this.arguments = args;
    }

    public QueryResult executeInMainThread()
    {
        if ( Looper.getMainLooper().getThread() != Thread.currentThread()) {
            return execute();
        }

        final AtomicReference<QueryResult> result = new AtomicReference<QueryResult>();
        InstrumentationBackend.instrumentation.runOnMainSync(new Runnable() {
            @Override
            public void run() {
                result.set(execute());
            }
        });
        return result.get();

    }

    @SuppressWarnings({"unchecked", "rawtypes" })
	public QueryResult execute() {
        List result = new ArrayList();
		View decorView = InstrumentationBackend.solo
				.getCurrentActivity().getWindow().getDecorView();
		if (decorView == null) { throw new IllegalStateException("Unable to find window decorView."); } 
		View rootView = decorView
				.findViewById(android.R.id.content);
		if (rootView == null) { throw new IllegalStateException("Unable to find root view."); }
		
        long before = System.currentTimeMillis();
        List queryResults = UIQueryEvaluator.evaluateQueryWithOptions(this.queryString, Collections.singletonList(rootView), this.arguments);
        long after = System.currentTimeMillis();
        
        String action = "EvaluateQuery";
        System.out.println(action+ " took: "+ (after-before) + "ms");
                
        for (Object v : queryResults) {
            if (UIQueryUtils.isVisible(v)) {
            	System.out.println("Query result: "+v);
            	result.add(ViewMapper.extractDataFromView(v));
            }
            
        }
               
        return new QueryResult(result);
    }


    public List<View> allVisibleViews() {
        return viewFetcher.getAllViews(false);
    }

    public List<View> rootViews() {
    	Set<View> parents = new HashSet<View>(8);
    	for (View v : allVisibleViews()) 
    	{
    		parents.add(viewFetcher.getTopParent(v));
    	}
    	List<View> results = new ArrayList<View>();
    	results.addAll(parents);
    	return results;
    }
}
