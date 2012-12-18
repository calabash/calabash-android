package sh.calaba.instrumentationbackend.query;

import static sh.calaba.instrumentationbackend.InstrumentationBackend.viewFetcher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import sh.calaba.instrumentationbackend.query.ast.UIQueryEvaluator;
import sh.calaba.instrumentationbackend.query.ast.UIQueryUtils;
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


    @SuppressWarnings({"unchecked", "rawtypes" })
	public QueryResult execute() {
        List result = new ArrayList();
        List<View> all = rootViews();
                
        long before = System.currentTimeMillis();
        List queryResults = UIQueryEvaluator.evaluateQueryWithOptions(this.queryString, all, this.arguments);
        long after = System.currentTimeMillis();
        String action = "EvaluateQuery";
        System.out.println(action+ " took: "+ (after-before) + "ms");
        
        before = System.currentTimeMillis();

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
