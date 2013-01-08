package sh.calaba.instrumentationbackend.actions.webview;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import sh.calaba.instrumentationbackend.InstrumentationBackend;
import android.os.Build;
import android.os.ConditionVariable;
import android.view.View;
import android.webkit.JsPromptResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

public class CalabashChromeClient extends WebChromeClient {
	private WebChromeClient mWebChromeClient;
	private final WebView webView;
	private final WebFuture scriptFuture;

	public CalabashChromeClient(WebView webView) {
		this.webView = webView;
		this.scriptFuture = new WebFuture(webView);
		if (Build.VERSION.SDK_INT < 16) { // jelly bean
			try {
				Method methodGetConfiguration = webView.getClass().getMethod(
						"getWebChromeClient");
				mWebChromeClient = (WebChromeClient) methodGetConfiguration
						.invoke(webView);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		webView.setWebChromeClient(this);
	}

	@Override
	public boolean onJsPrompt(WebView view, String url, String message,
			String defaultValue, JsPromptResult r) {
		if (message != null && message.startsWith("calabash:")) {
			r.confirm("CALABASH_ACK");
			System.out.println("onJsPrompt: " + message);
			String jsonResponse = message.replaceFirst("calabash:", "");
			try {
				scriptFuture.setResult(jsonResponse);
			} catch (Exception e) {
				e.printStackTrace();
				scriptFuture.setResult(null);
			}
			return true;
		} else {
			if (mWebChromeClient == null) {
				r.confirm("CALABASH_ERROR");
				scriptFuture.complete();
				return true;
			} else {
				// TODO I'm not what this case does...
				return mWebChromeClient.onJsPrompt(view, url, message,
						defaultValue, r);
			}
		}
	}

	public WebView getWebView() {
		return webView;
	}

	public static CalabashChromeClient prepareWebView(WebView webView) {
		CalabashChromeClient calabashChromeClient = new CalabashChromeClient(
				webView);
		webView.getSettings().setJavaScriptEnabled(true);
		return calabashChromeClient;
	}

	public static List<CalabashChromeClient> findAndPrepareWebViews() {
		List<CalabashChromeClient> webViews = new ArrayList<CalabashChromeClient>();
		ArrayList<View> views = InstrumentationBackend.solo.getCurrentViews();
		for (View view : views) {
			if (view instanceof WebView) {
				WebView webView = (WebView) view;
				webViews.add(prepareWebView(webView));
			}
		}
		return webViews;

	}

	public WebFuture getResult() {
		return scriptFuture;
	}

	@SuppressWarnings("rawtypes")
	public static class WebFuture implements Future {
		private final ConditionVariable eventHandled;
		private volatile boolean complete;
		private String result;
		private final WebView webView;

		public WebView getWebView() {
			return webView;
		}

		public void complete() {
			this.complete = true;
			this.eventHandled.open();
		}

		public WebFuture(WebView webView) {
			this.webView = webView;
			eventHandled = new ConditionVariable();
			result = null;
		}

		public synchronized void setResult(String result) {
			this.result = result;
			this.complete();
		}

		public synchronized String getResult() {
			return this.result;
		}

		public boolean cancel(boolean mayInterruptIfRunning) {
			return false;
		}

		@Override
		public Object get() throws InterruptedException, ExecutionException {
			eventHandled.block();
			return asMap();
		}

		@Override
		public Object get(long timeout, TimeUnit unit)
				throws InterruptedException, ExecutionException,
				TimeoutException {
			eventHandled.block(unit.convert(timeout, TimeUnit.MILLISECONDS));
			return asMap();
		}

		@Override
		public boolean isCancelled() {
			return false;
		}

		@Override
		public boolean isDone() {
			return complete;
		}

		public String getAsString() {				
			try {
				get(10,TimeUnit.SECONDS);
				return getResult();
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}			
		}
		
		@SuppressWarnings("unchecked")
		public Map asMap() {			
			HashMap m = new HashMap();
			m.put("webView", webView);
			m.put("result",getResult());			
			return m;
		}
	}
}
