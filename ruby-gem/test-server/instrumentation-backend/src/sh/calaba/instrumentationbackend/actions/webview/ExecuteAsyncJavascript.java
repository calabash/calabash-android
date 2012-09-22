package sh.calaba.instrumentationbackend.actions.webview;

import java.util.List;

import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;
import android.webkit.WebView;

public class ExecuteAsyncJavascript implements Action {

	@Override
	public Result execute(String... args) {

		List<CalabashChromeClient> list = CalabashChromeClient.findAndPrepareWebViews();
		if (list.isEmpty()) {
			return new Result(false, "No WebView component found");
		}
		
		CalabashChromeClient ccc = list.get(0);
		final WebView webView = ccc.getWebView();
		final String script = "javascript:(function() {"
				+ " function cb(ret) {"
				+ "  prompt('calabash:'+ret);"
				+ " }"
				+ " try {"
				+ "  (function(returnValue) {"
				+ args[0] + ";"
				+ "  }(cb));"
				+ " } catch (e) {"
				+ "  prompt('calabash:Exception: ' + e);"
				+ " }"
				+ "}())";

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

		boolean success = true;
		if (r.startsWith("Exception:")) {
			success = false;
		}

		return new Result(success, r);
	}

	@Override
	public String key() {
		return "execute_async_javascript";
	}

}
