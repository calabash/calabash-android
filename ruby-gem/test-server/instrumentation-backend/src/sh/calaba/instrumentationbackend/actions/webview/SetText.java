package sh.calaba.instrumentationbackend.actions.webview;


import java.util.List;
import java.util.Map;

import android.webkit.WebView;

import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;
import sh.calaba.instrumentationbackend.actions.webview.CalabashChromeClient.WebFuture;


public class SetText implements Action {

	/**
	 * args[0]: Selector type "xpath" or "css"
	 * args[1]: xpath or css selector
	 * args[2]: text to enter into the first selected element 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked"})
	@Override
    public Result execute(final String... args) {
		final String uiQuery = "webView " + args[0] + ":'"+ args[1] + "'";
		List queryResult = new sh.calaba.instrumentationbackend.query.Query(uiQuery).executeQuery();
		if (queryResult.isEmpty()) {
			throw new RuntimeException("No element found");
		}
		Map<String, Object> firstElement = QueryHelper.findFirstVisibleElement(queryResult);
		//TODO: Hack! Should be serialized instead of removed
		WebView webView = (WebView) firstElement.get("webView");
		firstElement.remove("class");
		firstElement.remove("webView");
		firstElement.remove("html");
		String firstElementJson = QueryHelper.toJsonString(firstElement);

		WebFuture result = QueryHelper.executeAsyncJavascriptInWebviews(webView,"set_text.js", firstElementJson, args[2]);
		
		return new Result(true,result.getAsString());    			    
    }

    @Override
    public String key() {
        return "set_text";
    }
    
    

}
