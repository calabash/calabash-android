package sh.calaba.instrumentationbackend.actions.webview;


import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;
import android.webkit.WebView;

public class DumpHtml implements Action {

    @Override
    public Result execute(String... args) {
    	                         
    	Result result = Result.successResult();
    	for (CalabashChromeClient ccc : CalabashChromeClient.findAndPrepareWebViews()) {
    		final WebView webView = ccc.getWebView();
            webView.loadUrl("javascript:(function() {" +
                    "prompt('calabash:' + document.body.parentNode.innerHTML);" +
                    "})()");
            String r = ccc.getResult();
            System.out.println("Html:");
            System.out.println("" + r);
            result.addBonusInformation(r);
        }

        return result;
    }

            @Override
    public String key() {
        return "dump_html";
    }
}
