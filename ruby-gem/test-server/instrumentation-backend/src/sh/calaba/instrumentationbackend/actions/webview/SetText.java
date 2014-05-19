package sh.calaba.instrumentationbackend.actions.webview;


import android.webkit.WebView;

import java.util.Map;
import java.util.concurrent.Callable;

import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;
import sh.calaba.instrumentationbackend.query.QueryResult;
import sh.calaba.instrumentationbackend.query.ast.UIQueryUtils;


public class SetText implements Action {

	/**
	 * args[0]: Selector type "xpath" or "css"
	 * args[1]: xpath or css selector
	 * args[2]: text to enter into the first selected element 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked"})
	@Override
    public Result execute(final String... args) {
        String query = args[1];
        if (!query.startsWith("'")) {
            query = "'" + query;
        }
        if (!query.endsWith("'")) {
            query = query + "'";
        }

        final String uiQuery = "android.webkit.WebView " + args[0] + ":" + query;
        QueryResult queryResult = new sh.calaba.instrumentationbackend.query.Query(uiQuery).executeQuery();
        if (queryResult.isEmpty()) {
            return Result.failedResult("No element found: " + query);
        }
        Map<String, Object> firstElement = QueryHelper.findFirstVisibleElement(queryResult.getResult());
        firstElement.remove("class");
        firstElement.remove("html");
        final WebView webView =  (WebView) firstElement.remove("webView");

        final String firstElementJson = QueryHelper.toJsonString(firstElement);


        Map<String, Object> result = (Map<String, Object>) UIQueryUtils.evaluateSyncInMainThread(new Callable() {
            @Override
            public Object call() throws Exception {
                return QueryHelper.executeAsyncJavascriptInWebviews(webView, "set_text.js", firstElementJson, args[2]);
            }
        });

        if (result.containsKey("error")) {
            return Result.failedResult(result.get("details").toString());
        }
        return new Result(true);
     }

     @Override
     public String key() {
         return "set_text";
     }

 }
