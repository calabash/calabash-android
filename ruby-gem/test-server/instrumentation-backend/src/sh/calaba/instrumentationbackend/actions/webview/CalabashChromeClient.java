package sh.calaba.instrumentationbackend.actions.webview;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.org.codehaus.jackson.map.ObjectMapper;
import sh.calaba.org.codehaus.jackson.type.TypeReference;
import android.os.Build;
import android.os.ConditionVariable;
import android.view.View;
import android.webkit.JsPromptResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;


public class CalabashChromeClient extends WebChromeClient {
	private final ConditionVariable eventHandled;
	private final AtomicReference<List<Map<String, Object>>> resultBox;	
	private WebChromeClient mWebChromeClient;
	private final WebView webView;

	public CalabashChromeClient(WebView webView, ConditionVariable computationFinished, AtomicReference<List<Map<String, Object>>> resultBox) {
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

	@SuppressWarnings("unchecked")
	@Override
	public boolean onJsPrompt(WebView view, String url, String message,	String defaultValue, JsPromptResult r) {
		if (message != null && message.startsWith("calabash:")) {
			r.confirm("CALABASH_ACK");
			System.out.println("onJsPrompt: " + message);
			String jsonResponse = message.replaceFirst("calabash:", "");
			try {
				List<Map<String, Object>> parsedResult = new ObjectMapper()
						.readValue(
								jsonResponse,
								new TypeReference<List<HashMap<String, Object>>>() {
								});
				
				for (Map<String,Object> data : parsedResult) {
					Map<String,Object> rect = (Map<String, Object>) data.get("rect");
					Map <String,Object> updatedRect = QueryHelper.translateRectToScreenCoordinates(view, rect);
					data.put("rect", updatedRect);
					data.put("webView", view);
				}
				
				resultBox.set(parsedResult);
				
			} catch (Exception e) {
				e.printStackTrace();
				resultBox.set(null);
			} finally {
				eventHandled.open();	
			}					
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

	public AtomicReference<List<Map<String, Object>>> getResultBox() {
		return resultBox;
	}
   

	public static List<CalabashChromeClient> findAndPrepareWebViews() {
		List<CalabashChromeClient> webViews = new ArrayList<CalabashChromeClient>();
		ArrayList<View> views = InstrumentationBackend.solo.getCurrentViews();
		for (View view : views) {
			if ( view instanceof WebView) {
				WebView webView = (WebView)view;				
				webViews.add(prepareWebView(webView,new ConditionVariable(),new AtomicReference<List<Map<String, Object>>>()));
				System.out.println("Setting CalabashChromeClient");
			}
		}
		return webViews;
	}

	public static CalabashChromeClient prepareWebView(WebView webView, ConditionVariable computationFinished, AtomicReference<List<Map<String, Object>>> result) {
		CalabashChromeClient calabashChromeClient = new CalabashChromeClient(webView,computationFinished,result);
		webView.getSettings().setJavaScriptEnabled(true);
		return calabashChromeClient;
	}

	public String getResult() {
		throw new UnsupportedOperationException("Disabled temporarily");		
	}
}
