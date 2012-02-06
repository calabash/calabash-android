package sh.calaba.instrumentationbackend.actions.webview;


import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;
import android.util.Log;
import android.webkit.WebView;

public class DumpBodyHtml implements Action {

    @Override
    public Result execute(String... args) {
    	                         
    	for (CalabashChromeClient ccc : CalabashChromeClient.findAndPrepareWebViews()) {
    		WebView webView = ccc.getWebView();
			
			webView.loadUrl("javascript:(function() {" +
				"prompt('calabash:' + document.body.innerHTML);" + 
				"})()");

			String r = ccc.getResult();
			//Log.i("Html", r);
			System.out.println("Html:");
			System.out.println("" + r);
		}
    	return Result.successResult();
    }

    @Override
    public String key() {
        return "dump_body_html";
    }
}
