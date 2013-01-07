package sh.calaba.instrumentationbackend.actions.webview;


import java.util.Collections;
import java.util.List;

import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;


public class SetText implements Action {

	/**
	 * args[0]: Query
	 * args[1]: text to enter into the first queried element
	 */
    @SuppressWarnings({ "rawtypes"})
	@Override
    public Result execute(String... args) {
    	try {
    		sh.calaba.instrumentationbackend.query.Query q = new sh.calaba.instrumentationbackend.query.Query(args[0],
    				Collections.singletonList(new JavaScriptOperation("set_text.js",args[1]))); 
    		
    		List queryResult = q.executeInMainThread(true);
			if (queryResult.isEmpty()) {
				throw new RuntimeException("No element found");
			}
								
	    	return new Result(true, "");
    	} catch (Exception e) {
    		e.printStackTrace();
    		throw new RuntimeException(e);
		}
    }

    @Override
    public String key() {
        return "set_text";
    }
    
    

}
