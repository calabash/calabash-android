package sh.calaba.instrumentationbackend.actions.webview;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import sh.calaba.instrumentationbackend.InstrumentationBackend;
import android.os.Build;
import android.os.ConditionVariable;
import android.view.View;
import android.webkit.JsPromptResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;


public class CalabashChromeClient extends WebChromeClient {
	private final ConditionVariable eventHandled;
	private final AtomicReference<String> resultBox;	
	private WebChromeClient mWebChromeClient;
	private final WebView webView;

	public CalabashChromeClient(WebView webView, ConditionVariable computationFinished, AtomicReference<String> resultBox) {
		this.eventHandled = computationFinished;
		this.resultBox = resultBox;
		this.webView = webView;
        if (Build.VERSION.SDK_INT < 16) { // jelly bean
            try {
                Method methodGetConfiguration = webView.getClass().getMethod("getWebChromeClient");
                mWebChromeClient = (WebChromeClient)methodGetConfiguration.invoke(webView);
            } catch(Exception e) {
                throw new RuntimeException(e);
            }
		}
        webView.setWebChromeClient(this);
	}

	@Override
	public boolean onJsPrompt(WebView view, String url, String message,	String defaultValue, JsPromptResult r) {
		if (message != null && message.startsWith("calabash:")) {
			r.confirm("CALABASH_ACK");
			System.out.println("onJsPrompt: " + message);			
			resultBox.set(message.replaceFirst("calabash:", ""));
			eventHandled.open();

			return true;
		} else {
			if (mWebChromeClient == null) {
				r.confirm("CALABASH_ERROR");
				eventHandled.open();
				return true;
			} else {
				//TODO I'm not what this case does...
				return mWebChromeClient.onJsPrompt(view, url, message, defaultValue, r);
			}
		}
	}

    public WebView getWebView() {
        return webView;
    }

	public AtomicReference<String> getResultBox() {
		return resultBox;
	}
   

	public static List<CalabashChromeClient> findAndPrepareWebViews() {
		List<CalabashChromeClient> webViews = new ArrayList<CalabashChromeClient>();
		ArrayList<View> views = InstrumentationBackend.solo.getCurrentViews();
		for (View view : views) {
			if ( view instanceof WebView) {
				WebView webView = (WebView)view;				
				webViews.add(prepareWebView(webView,new ConditionVariable(),new AtomicReference<String>()));
				System.out.println("Setting CalabashChromeClient");
			}
		}
		return webViews;
	}

	public static CalabashChromeClient prepareWebView(WebView webView, ConditionVariable computationFinished, AtomicReference<String> result) {
		CalabashChromeClient calabashChromeClient = new CalabashChromeClient(webView,computationFinished,result);
		webView.getSettings().setJavaScriptEnabled(true);
		return calabashChromeClient;
	}

	public String getResult() {
		throw new UnsupportedOperationException("Disabled temporarily");		
	}
}
