package sh.calaba.instrumentationbackend.actions.webview;


import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.TestHelpers;
import sh.calaba.instrumentationbackend.actions.Action;
import android.webkit.WebView;


public class SetPropertyByCssSelector implements Action {

    @Override
    public Result execute(String... args) {
    	String cssSelector = args[0];
    	String propertyName = args[1];
    	String value = args[2];
    	                         
    	for (CalabashChromeClient ccc : CalabashChromeClient.findAndPrepareWebViews()) {
    		final WebView webView = ccc.getWebView();
			
    		final String assignment = "document.querySelector(\"" + cssSelector + "\")." + propertyName + " = " + value + ";";
    		System.out.println(assignment);

            InstrumentationBackend.solo.getCurrentActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    webView.loadUrl("javascript:(function() {" +
                               assignment +
                               "prompt('calabash:true');" +
                               "})()");

                }
            });
            String r = ccc.getResult();
            System.out.println("setPropertyByCssSelector: " + r);
            if ("true".equals(r)) {
                TestHelpers.wait(0.3);
                return Result.successResult();
            }
        }

       return new Result(false,"No WebView found");
   }

            @Override
    public String key() {
        return "set_property_by_css_selector";
    }

}
