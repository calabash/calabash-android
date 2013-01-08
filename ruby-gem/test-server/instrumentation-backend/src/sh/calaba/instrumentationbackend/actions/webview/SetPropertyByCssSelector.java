package sh.calaba.instrumentationbackend.actions.webview;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;
import sh.calaba.instrumentationbackend.actions.webview.CalabashChromeClient.WebFuture;
import sh.calaba.instrumentationbackend.query.ast.UIQueryUtils;
import android.webkit.WebView;


public class SetPropertyByCssSelector implements Action {

    @SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
    public Result execute(String... args) {
    	final String cssSelector = args[0];
    	final String propertyName = args[1];
    	final String value = args[2];
    	      
    	List<WebFuture> webResults = (List<WebFuture>) UIQueryUtils.evaluateSyncInMainThread(new Callable() {			
			
			public Object call() throws Exception {
				
				List<WebFuture> webResults = new ArrayList();
		    	for (CalabashChromeClient ccc : CalabashChromeClient.findAndPrepareWebViews()) {
		    		WebView webView = ccc.getWebView();
					
		    		final String assignment = "document.querySelector(\"" + cssSelector + "\")." + propertyName + " = " + value + ";";
		    		System.out.println(assignment);
		            webView.loadUrl("javascript:(function() {" +
		                    assignment +
		                    "prompt('calabash:true');" +
		                    "})()");
		            webResults.add(ccc.getResult());		            		            
		        }

				return webResults;
				
			}
		});
    	
    	List<String> allResults = new ArrayList<String>(webResults.size());
    	boolean success = false;
    	for (WebFuture f : webResults) {
    		String result = f.getAsString();
			allResults.add(result);
    		if ("true".equals(result)) {	               
               success = true;
            }
    	}
    		
    	if (allResults.size() == 0) {
    		return new Result(false, "No WebView found");	
    	}
    	else {
    		return new Result(success, allResults);
    	}
    }

    @Override
    public String key() {
        return "set_property_by_css_selector";
    }

}
