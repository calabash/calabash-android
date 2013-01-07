package sh.calaba.instrumentationbackend.actions.webview;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import sh.calaba.instrumentationbackend.actions.Operation;
import android.os.ConditionVariable;
import android.webkit.WebView;

public class JavaScriptOperation implements Operation {

	public final String script;
	public final String arg;	

	public JavaScriptOperation(String script, String args) {		
		this.script = script;
		this.arg = args;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object apply(Object o) {		
		Map<String, Object> domEl = (Map<String, Object>) o;
		
		WebView webView = (WebView) domEl.get("webView");
		
		domEl.remove("class");
		domEl.remove("html");
		domEl.remove("webView");
		
		String elJson = QueryHelper.toJsonString(domEl);

		ConditionVariable cv = new ConditionVariable();
		AtomicReference<List<Map<String, Object>>> results = new AtomicReference<List<Map<String, Object>>>();

		QueryHelper.executeAsyncJavascriptInWebviews(webView, this.script,
						elJson, this.arg, cv, results);
		
		//cv.block(10000);
		return results.get();
		

	}

}
