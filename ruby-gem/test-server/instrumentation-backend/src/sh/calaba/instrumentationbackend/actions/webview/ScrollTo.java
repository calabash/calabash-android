package sh.calaba.instrumentationbackend.actions.webview;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;
import sh.calaba.instrumentationbackend.actions.Actions;
import sh.calaba.org.codehaus.jackson.map.ObjectMapper;
import sh.calaba.org.codehaus.jackson.type.TypeReference;
import android.test.TouchUtils;
import android.webkit.WebView;


public class ScrollTo implements Action {

    @Override
    public Result execute(String... args) {
    	//TODO: Should do horizontal scrolling if needed
    	String queryResult = QueryHelper.executeJavascriptInWebview("calabash.js", args[1], args[0]);
		CalabashChromeClient calabashChromeClient = CalabashChromeClient.findAndPrepareWebViews().get(0);
		final WebView webView = calabashChromeClient.getWebView();
		webView.scrollTo(0, 0);
		QueryHelper.getScreenCoordinatesForCenter(findFirstRect(queryResult));
		int scrolledTo = webView.getScrollY();
		while (!isVisible(findFirstRect(queryResult), calabashChromeClient)) {
			TouchUtils.dragQuarterScreenUp(Actions.parentTestCase, InstrumentationBackend.solo.getCurrentActivity());
			if (scrolledTo != webView.getScrollY()) {
				scrolledTo = webView.getScrollY();
			} else {
				return new Result(false, "Tried scrolling but the center of the element never became visible.");
			}
		}
	
		return new Result(true, "");
    }



	private Map<String, Object> findFirstRect(String queryResult) {
		try {
			List<HashMap<String,Object>> p = new ObjectMapper().readValue(queryResult, new TypeReference<List<HashMap<String,Object>>>(){});
			
			if (p.isEmpty()) {
				throw new RuntimeException("No element found");
			}
			System.out.println(p);
			final Map<String, Object> firstRect = QueryHelper.findFirstVisibleRectangle(p);
			return firstRect;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}
    
    
    
    private boolean isVisible(Map<String, Object> rectangle, CalabashChromeClient calabashChromeClient) {
    	WebView webView = calabashChromeClient.getWebView();
    	int windowTop = webView.getScrollY();
    	int windowBottom = webView.getScrollY() + webView.getHeight();
    	int centerY = (int) ((Integer)rectangle.get("center_y") * calabashChromeClient.getScale());

    	return windowTop < centerY && centerY < windowBottom;  	
    }

    @Override
    public String key() {
        return "scroll_to";
    }
}
