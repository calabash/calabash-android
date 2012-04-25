package sh.calaba.instrumentationbackend.actions.webview;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import sh.calaba.instrumentationbackend.InstrumentationBackend;
import android.os.Build;
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
	
	
	
	public float getScale() {
		try {
			Field mActualScaleField = null;
			Object targetObject = webView;
			
			if (Build.VERSION.SDK_INT < 14) { //before Ice cream sandwich
				mActualScaleField = WebView.class.getDeclaredField("mActualScale");
			} else {
				Field zoomManagerField = WebView.class.getDeclaredField("mZoomManager");
				zoomManagerField.setAccessible(true);
				targetObject = zoomManagerField.get(webView);
				
				mActualScaleField = Class.forName("android.webkit.ZoomManager").getDeclaredField("mActualScale");
			}
			mActualScaleField.setAccessible(true);
			return mActualScaleField.getFloat(targetObject);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public WebView getWebView() {
		return webView;
	}
	
	public String getResult() {
		eventHandled.block(30000);
		if (result.message == null) {
			throw new RuntimeException("Timed out waiting for result for JavaScript");
		}
		return result.message;
	}
	
	private class Result {
		String message;
	}
	
	public static List<CalabashChromeClient> findAndPrepareWebViews() {
		List<CalabashChromeClient> webViews = new ArrayList<CalabashChromeClient>();
		ArrayList<View> views = InstrumentationBackend.solo.getCurrentViews();
		for (View view : views) {
			if ( view instanceof WebView) {
				WebView webView = (WebView)view;
				webViews.add(new CalabashChromeClient(webView));
				webView.getSettings().setJavaScriptEnabled(true);
				System.out.println("Setting CalabashChromeClient");
			}
		}
		return webViews;
	}
}
