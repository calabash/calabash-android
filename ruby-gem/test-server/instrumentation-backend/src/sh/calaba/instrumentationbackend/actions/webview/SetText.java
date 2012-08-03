package sh.calaba.instrumentationbackend.actions.webview;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;
import sh.calaba.org.codehaus.jackson.map.ObjectMapper;
import sh.calaba.org.codehaus.jackson.type.TypeReference;


public class SetText implements Action {

	/**
	 * args[0]: Selector type "xpath" or "css"
	 * args[1]: xpath or css selector
	 * args[2]: text to enter into the first selected element 
	 */
    @Override
    public Result execute(String... args) {
    	try {
	    	String queryResult = QueryHelper.executeJavascriptInWebview("calabash.js", args[1], args[0]);
	    	List<HashMap<String,Object>> p = new ObjectMapper().readValue(queryResult, new TypeReference<List<HashMap<String,Object>>>(){});
			
			if (p.isEmpty()) {
				throw new RuntimeException("No element found");
			}
			Map<String, Object> firstElement = QueryHelper.findFirstVisibleElement(p);
			//TODO: Hack! Should be serialized instead of removed
			firstElement.remove("class");
			firstElement.remove("html");
			String firstElementJson = QueryHelper.toJsonString(firstElement);
			
			String result = QueryHelper.executeJavascriptInWebview("set_text.js", firstElementJson, args[2]);
	    	return new Result(true, result);
    	} catch (Exception e) {
    		throw new RuntimeException(e);
		}
    }

    @Override
    public String key() {
        return "set_text";
    }
    
    

}
