package sh.calaba.instrumentationbackend.actions.webview;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;
import sh.calaba.org.codehaus.jackson.map.ObjectMapper;
import sh.calaba.org.codehaus.jackson.type.TypeReference;
import android.webkit.WebView;

public class Query implements Action {

	@Override
	public Result execute(String... args) {

		String result = QueryHelper.executeJavascriptInWebviews(null,
				"calabash.js", args[1], args[0]);
		return new Result(true, result);
	}

	public static List<Map<String, Object>> evaluateQueryInWebView(String type,
		String selector, WebView webView) {
		String queryResult = QueryHelper.executeJavascriptInWebviews(webView,
				"calabash.js", selector, type);

		try {
			return new ObjectMapper().readValue(queryResult,
					new TypeReference<List<HashMap<String, Object>>>() {});
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

	}

	@Override
	public String key() {
		return "query";
	}

}
