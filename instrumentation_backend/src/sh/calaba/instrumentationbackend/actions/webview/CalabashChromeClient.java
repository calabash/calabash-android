package sh.calaba.instrumentationbackend.actions.webview;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import sh.calaba.instrumentationbackend.InstrumentationBackend;
import android.os.ConditionVariable;
import android.view.View;
import android.webkit.JsPromptResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;


public class CalabashChromeClient extends WebChromeClient {
	private final ConditionVariable eventHandled = new ConditionVariable();
	private final Result result = new Result();
	
	
	
	private WebChromeClient mWebChromeClient;
	private final WebView webView;

	public CalabashChromeClient(WebView webView) {
		this.webView = webView;
		try {
	        Method methodGetConfiguration = webView.getClass().getMethod("getWebChromeClient");
	        mWebChromeClient = (WebChromeClient)methodGetConfiguration.invoke(webView);
	        webView.setWebChromeClient(this);
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public boolean onJsPrompt(WebView view, String url, String message,	String defaultValue, JsPromptResult r) {
		if (message != null && message.startsWith("calabash:")) {
			r.confirm("CALABASH_ACK");
			System.out.println("onJsPrompt: " + message);
			result.message = message.replace("calabash:", "");
			eventHandled.open();
			
			return true;
		} else {
			if (mWebChromeClient == null) {
				r.confirm("CALABASH_ERROR");
				return true;
			} else {
				return mWebChromeClient.onJsPrompt(view, url, message, defaultValue, r);
			}
		}
	}
	
	public WebView getWebView() {
		return webView;
	}
	
	public String getResult() {
		eventHandled.block(20000);
		return result.message;
	}
	
	private class Result {
		String message;
	}
	
	public static List<CalabashChromeClient> findAndPrepareWebViews() {
		List<CalabashChromeClient> webViews = new ArrayList<CalabashChromeClient>();
		for (View view : InstrumentationBackend.solo.getCurrentViews()) {
			if ( view instanceof WebView) {
				WebView webView = (WebView)view;
				webViews.add(new CalabashChromeClient(webView));
				webView.getSettings().setJavaScriptEnabled(true);
			}
		}
		return webViews;
	}
}
