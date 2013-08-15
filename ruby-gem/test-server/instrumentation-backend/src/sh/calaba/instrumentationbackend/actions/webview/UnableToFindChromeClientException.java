package sh.calaba.instrumentationbackend.actions.webview;

import android.webkit.WebView;

@SuppressWarnings("serial")
public class UnableToFindChromeClientException extends RuntimeException {

	private final WebView webView;

	public UnableToFindChromeClientException(WebView webView) {		
		this.webView = webView;
	}

	public UnableToFindChromeClientException(Exception e, WebView webView) {
		super(e);
		this.webView = webView;
	}

	public WebView getWebView() {
		return webView;
	}

	public String toString() {
		return super.toString() + "- WebView: " + webView;
	}
}
