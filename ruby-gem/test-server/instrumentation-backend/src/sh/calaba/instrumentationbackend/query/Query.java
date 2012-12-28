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

    public QueryResult executeInMainThread(final boolean includeInvisible)
    {
        if ( Looper.getMainLooper().getThread() == Thread.currentThread()) {
            return execute(includeInvisible);
        }

        final AtomicReference<QueryResult> result = new AtomicReference<QueryResult>();
        InstrumentationBackend.instrumentation.runOnMainSync(new Runnable() {
            @Override
            public void run() {
                result.set(execute(includeInvisible));
            }
        });
        return result.get();

    }

    @SuppressWarnings({"unchecked", "rawtypes" })
	public QueryResult execute(boolean includeInvisible) {
        List result = new ArrayList();
		
        long before = System.currentTimeMillis();
        List queryResults = UIQueryEvaluator.evaluateQueryWithOptions(this.queryString, rootViews(), this.arguments);
        long after = System.currentTimeMillis();
        
        String action = "EvaluateQuery";
        System.out.println(action+ " took: "+ (after-before) + "ms");
                
        for (Object v : queryResults) {
            if (includeInvisible || UIQueryUtils.isVisible(viewFetcher,v)) {
            	result.add(ViewMapper.extractDataFromView(v));
            }            
        }
               
        return new QueryResult(result);
    }


    public List<View> allVisibleViews() {
        return viewFetcher.getAllViews(false);
    }

    public List<View> rootViews() {
    	return Arrays.asList(viewFetcher.getWindowDecorViews());
    }
}
