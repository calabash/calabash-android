package sh.calaba.instrumentationbackend.actions.webview;


import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.TestHelpers;
import sh.calaba.instrumentationbackend.actions.Action;
import android.webkit.WebView;


public class PressByCssSelector implements Action {

    @Override
    public Result execute(final String... args) {
    	for (CalabashChromeClient ccc : CalabashChromeClient.findAndPrepareWebViews()) {
    		final WebView webView = ccc.getWebView();

            // runOnUiThread to avoid spurious "Only the original thread that created a view hierarchy can touch its views." errors
            InstrumentationBackend.solo.getCurrentActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    webView.loadUrl("javascript:(function() {" +
                            "var element = document.querySelector(\"" + args[0] + "\");" +
                            "if (element != null) {" +
                            "	var oEvent = document.createEvent ('MouseEvent');" +
                            "	oEvent.initMouseEvent('click', true, true,window, 1, 1, 1, 1, 1, false, false, false, false, 0, element);" +
                            "	element.dispatchEvent( oEvent );" +
                            "	prompt('calabash:true');" +
                            "}" +
                            "prompt('calabash:false');" +
                            "})()");
                }
            });


			String r = ccc.getResult();
			System.out.println("clickOnSelector: " + r);
			if ("true".equals(r)) {
				TestHelpers.wait(0.3);
				return Result.successResult();
			}	
		}
		return new Result(false, "");
    }

    @Override
    public String key() {
        return "click_by_selector";
    }

}
