package sh.calaba.instrumentationbackend.actions.webview;

import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;
import android.webkit.WebView;

public class ExecuteJavascript implements Action {

	@Override
	public Result execute(String... args) {

		CalabashChromeClient ccc = CalabashChromeClient.findAndPrepareWebViews().get(0);
		final WebView webView = ccc.getWebView();
		final String script = "javascript:(function() {"
				+ "var result;"
				+ args[0] + ";"
				+ "prompt('calabash:'+result);" + "})()";

		System.out.println("execute javascript: " + script);
		
		InstrumentationBackend.solo.getCurrentActivity().runOnUiThread(
				new Runnable() {
					@Override
					public void run() {
						webView.loadUrl(script);
					}
				});

		String r = ccc.getResult();
		System.out.println("javascript result: " + r);

		return new Result(true, r);
	}

	@Override
	public String key() {
		return "execute_javascript";
	}

}
