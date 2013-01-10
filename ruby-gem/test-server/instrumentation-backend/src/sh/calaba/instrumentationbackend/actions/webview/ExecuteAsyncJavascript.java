package sh.calaba.instrumentationbackend.actions.webview;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;
import sh.calaba.instrumentationbackend.actions.webview.CalabashChromeClient.WebFuture;
import sh.calaba.instrumentationbackend.query.ast.UIQueryUtils;
import android.webkit.WebView;

public class ExecuteAsyncJavascript implements Action {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Result execute(String... args) {
		final String scriptCode = args[0]; 
		List<WebFuture> webResults = (List<WebFuture>) UIQueryUtils.evaluateSyncInMainThread(new Callable() {			
			
			public Object call() throws Exception {
				
				List<WebFuture> webResults = new ArrayList();
				List<CalabashChromeClient> list = CalabashChromeClient.findAndPrepareWebViews();
				if (list.isEmpty()) {
					return webResults;
				}
				
				CalabashChromeClient ccc = list.get(0);
				WebView webView = ccc.getWebView();
				final String script = "javascript:(function() {"
						+ " function cb(ret) {"
						+ "  prompt('calabash:'+ret);"
						+ " }"
						+ " try {"
						+ "  (function(returnValue) {"
						+ scriptCode + ";"
						+ "  }(cb));"
						+ " } catch (e) {"
						+ "  prompt('calabash:Exception: ' + e);"
						+ " }"
						+ "}())";

				System.out.println("execute javascript: " + script);

		        webView.loadUrl(script);
		        webResults.add(ccc.getResult());				
				return webResults;
				
			}
		});
    	
    	List<String> allResults = new ArrayList<String>(webResults.size());
    	boolean success = true;
    	for (WebFuture f : webResults) {
    		String result = f.getAsString();
			allResults.add(result);    		
			if (result.startsWith("Exception:")) {
				success = false;
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
		return "execute_async_javascript";
	}

}
