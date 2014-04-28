package sh.calaba.instrumentationbackend.actions.webview;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;
import sh.calaba.instrumentationbackend.actions.webview.CalabashChromeClient.WebFuture;
import sh.calaba.instrumentationbackend.query.ast.UIQueryUtils;
import android.webkit.WebView;

public class ExecuteJavascriptReturnResponse implements Action {

    @SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
    public Result execute(String... args) {
    	final String scriptCode = args[0]; 
		final Integer webviewNumber = Integer.parseInt(args[1]);

    	List<WebFuture> webResults = (List<WebFuture>) UIQueryUtils.evaluateSyncInMainThread(new Callable() {			
			
			public Object call() throws Exception {
				
				List<WebFuture> webResults = new ArrayList();
				List<CalabashChromeClient> list = CalabashChromeClient.findAndPrepareWebViews();
				if (list.isEmpty()) {
					return webResults;
				}
				
				System.out.println("WebView Count : " + list.size());
				CalabashChromeClient ccc = list.get(0);

				if (list.size() >= webviewNumber && webviewNumber > 0 )
					ccc = list.get(webviewNumber-1);

				WebView webView = ccc.getWebView();
				final String script = "javascript:(function() {"
							+ " try { "
		                    + "prompt('calabash:' + " + scriptCode + ");"
		                    + " } catch (e) {" 
		                    + " } "
		                    + "})()";

				System.out.println("execute javascript: " + script);

		        webView.loadUrl(script);
		        webResults.add(ccc.getResult());				
				return webResults;

			}
		});
    	
    	List<String> allResults = new ArrayList<String>(webResults.size());
    	for (WebFuture f : webResults) {
    		allResults.add(f.getAsString());			    		
    	}
    		
    	if (allResults.size() == 0) {
    		return new Result(false, "No WebView found");	
    	}
    	else {
    		return new Result(true, allResults);
    	}

    }

    @Override
    public String key() {
        return "execute_javascript_return_response";
    }

}
