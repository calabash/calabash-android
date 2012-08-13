package sh.calaba.instrumentationbackend.actions.webview;


import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;
import android.webkit.WebView;

public class DumpBodyHtml implements Action {

    @Override
    public Result execute(String... args) {
    	                         
    	final Result result = Result.successResult();
    	for (CalabashChromeClient ccc : CalabashChromeClient.findAndPrepareWebViews()) {
    		final WebView webView = ccc.getWebView();
            InstrumentationBackend.solo.getCurrentActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    webView.loadUrl("javascript:(function() {" +
                             "prompt('calabash:' + document.body.innerHTML);" +
                             "})()");
                 }
             });
            String r = ccc.getResult();
            System.out.println("Html:");
            System.out.println("" + r);
            result.addBonusInformation(r);

            return result;

        }
        return new Result(false, "No WebView found");
    }
    public String key() {
        return "dump_body_html";
    }
}
