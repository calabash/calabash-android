package sh.calaba.instrumentationbackend.actions.webview;


import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;


public class Query implements Action {

    @Override
    public Result execute(String... args) {
    	    	
    	String result = QueryHelper.executeJavascriptInWebview("calabash.js", args[1], args[0]);
    	return new Result(true, result);
    }

    @Override
    public String key() {
        return "query";
    }
    
    

}
