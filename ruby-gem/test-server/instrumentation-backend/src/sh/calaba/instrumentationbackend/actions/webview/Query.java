package sh.calaba.instrumentationbackend.actions.webview;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;
import android.os.ConditionVariable;
import android.webkit.WebView;

public class Query implements Action {

	@Override
	public Result execute(String... args) {

		String result = QueryHelper.executeJavascriptInWebviews(null,
				"calabash.js", args[1], args[0]);
		return new Result(true, result);
	}

	public static AtomicReference<List<Map<String,Object>>> evaluateQueryInWebView(String type,
		String selector, WebView webView, ConditionVariable computationFinished) {
		AtomicReference<List<Map<String,Object>>> result = new AtomicReference<List<Map<String,Object>>>();
		QueryHelper.executeAsyncJavascriptInWebviews(webView,
				"calabash.js", selector, type,computationFinished,result);
		return result;
	}

	@Override
	public String key() {
		return "query";
	}

}
