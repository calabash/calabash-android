package sh.calaba.instrumentationbackend.actions.webview;


import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.TestHelpers;
import sh.calaba.instrumentationbackend.actions.Action;
import android.webkit.WebView;


public class EnterTextByCssSelector implements Action {

    @Override
    public Result execute(String... args) {
    	final String cssSelector = args[0];
    	final String value = args[1];

    	for (CalabashChromeClient ccc : CalabashChromeClient.findAndPrepareWebViews()) {
    		final WebView webView = ccc.getWebView();
			
    		
            InstrumentationBackend.solo.getCurrentActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                	String functions = "        function simulateKeyEvent(elem, character) {\n" +
                    "            var ch = character.charCodeAt(0);\n" +
                    "\n" +
                    "            var evt;\n" +
                            "\n" +
                    "            evt = document.createEvent('KeyboardEvent');\n" +
                    "            evt.initKeyboardEvent('keydown', true, true, window, 0, 0, 0, 0, 0, ch);\n" +
                    "            elem.dispatchEvent(evt);\n" +
                    "\n" +
                    "            evt = document.createEvent('KeyboardEvent');\n" +
                    "            evt.initKeyboardEvent('keyup', true, true, window, 0, 0, 0, 0, 0, ch);\n" +
                    "            elem.dispatchEvent(evt);\n" +
                    "\n" +
                    "            evt = document.createEvent('KeyboardEvent');\n" +
                    "            evt.initKeyboardEvent('keypress', true, true, window, 0, 0, 0, 0, 0, ch);\n" +
                    "            elem.dispatchEvent(evt);\n" +
                    "        }\n" +
                    "";

                    functions +=
                            "        function enterTextIntoInputField(elem, text) {\n" +
                            "            for (var i = 0; i < text.length; i++) {\n" +
                            "                var ch = text.charAt(i);\n" +
                            "                elem.value += ch;\n" +
                            "                simulateKeyEvent(elem, ch);\n" +
                            "            }\n" +
                            "        }\n" +
                            "";

                    functions +=
                            "        function fireHTMLEvent(elem, eventName) {\n" +
                            "            var evt = document.createEvent(\"HTMLEvents\");\n" +
                            "            evt.initEvent(eventName, true, true );\n" +
                            "            return !elem.dispatchEvent(evt);\n" +
                            "        }\n" +
                            "";

                    functions +=
                            "        function selectInputField(elem) {\n" +
                            "            elem.click();\n" +
                            "            elem.focus();\n" +
                            "        }\n" +
                            "";

                    functions +=
                            "        function deselectInputField(elem) {\n" +
                                    "            fireHTMLEvent(elem, 'change');\n" +
                                    "            fireHTMLEvent(elem, 'blur');\n" +
                                    "        }\n" +
                                    "";

                    webView.loadUrl("javascript:(function() {" +
                    		functions +
                    		"var elem = document.querySelector(\"" + cssSelector + "\"); selectInputField(elem); enterTextIntoInputField(elem, '" + value + "'); deselectInputField(elem); " +
                            "prompt('calabash:true');" +
                            "})()");
                }
            });
    		
			String r = ccc.getResult();
			System.out.println("enterTextIntoInputField: " + r);
			if ("true".equals(r)) {
				TestHelpers.wait(0.3);
				return Result.successResult();
			}	
		}
		return new Result(false, "");
    }

    @Override
    public String key() {
        return "enter_text_by_selector";
    }

}
