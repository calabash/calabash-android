package sh.calaba.instrumentationbackend.actions.webview;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;
import sh.calaba.instrumentationbackend.actions.webview.CalabashChromeClient.WebFuture;
import sh.calaba.instrumentationbackend.query.ast.UIQueryUtils;
import android.webkit.WebView;

@Deprecated
public class PressByCssSelector implements Action {

    @SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
    public Result execute(final String... args) {
    	List<WebFuture> webResults = (List<WebFuture>) UIQueryUtils.evaluateSyncInMainThread(new Callable() {			
			
			public Object call() throws Exception {
				
				List<WebFuture> webResults = new ArrayList();
		    	for (CalabashChromeClient ccc : CalabashChromeClient.findAndPrepareWebViews()) {
		    		WebView webView = ccc.getWebView();

		            webView.loadUrl("javascript:(function() {" +
		                    "var element = document.querySelector(\"" + args[0] + "\");" +
		                    "if (element != null) {" +
		                    "	var oEvent = document.createEvent ('MouseEvent');" +
		                    "	oEvent.initMouseEvent('click', true, true,window, 1, 1, 1, 1, 1, false, false, false, false, 0, element);" +
		                    "	element.dispatchEvent( oEvent );" +
		                    "	prompt('calabash:true');" +
                            "   return;" +
		                    "}" +
		                    "prompt('calabash:false');" +
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
        return "click_by_selector";
    }

}
