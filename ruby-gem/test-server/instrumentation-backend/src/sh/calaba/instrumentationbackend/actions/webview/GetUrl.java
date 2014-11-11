package sh.calaba.instrumentationbackend.actions.webview;

import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;
import android.webkit.WebView;

public class GetUrl implements Action {

	@Override
	public Result execute(String... args) {
		CalabashChromeClient ccc = CalabashChromeClient.findAndPrepareWebViews().get(0);
		WebView webView = ccc.getWebView();
		return new Result(true, webView.getUrl());
	}

	@Override
	public String key() {
		return "get_url";
	}

}
