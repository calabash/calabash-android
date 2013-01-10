package sh.calaba.instrumentationbackend.actions.webview;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;
import sh.calaba.instrumentationbackend.actions.webview.CalabashChromeClient.WebFuture;
import sh.calaba.instrumentationbackend.query.ast.UIQueryUtils;
import android.webkit.WebView;

@Deprecated
public class EnterTextByCssSelector implements Action {

    @SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
    public Result execute(String... args) {
    	final String cssSelector = args[0];
    	final String value = args[1];
    	
    	List<WebFuture> webResults = (List<WebFuture>) UIQueryUtils.evaluateSyncInMainThread(new Callable() {			
			
			public Object call() throws Exception {
				
				List<WebFuture> webResults = new ArrayList();
				for (CalabashChromeClient ccc : CalabashChromeClient.findAndPrepareWebViews()) {
		    		WebView webView = ccc.getWebView();

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
		    						
		            webResults.add(ccc.getResult());
				}
			
				return webResults;
				
			}
		});
    	
    	List<String> allResults = new ArrayList<String>(webResults.size());
    	boolean allSucceed = true;
    	for (WebFuture f : webResults) {
    		String result = f.getAsString();
			allResults.add(result);
    		if (!"true".equals(result)) {
				allSucceed = false;
			}
    	}
    		
    	
    	if (allResults.size() == 0) {
    		return new Result(false, "No WebView found");	
    	}
    	else {
    		return new Result(allSucceed, allResults);
    	}
    	
    }

    @Override
    public String key() {
        return "enter_text_by_selector";
    }

}
