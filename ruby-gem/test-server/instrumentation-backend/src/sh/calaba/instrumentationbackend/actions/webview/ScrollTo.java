package sh.calaba.instrumentationbackend.actions.webview;


import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;
import sh.calaba.instrumentationbackend.actions.Actions;
import sh.calaba.instrumentationbackend.query.ast.UIQueryUtils;
import android.test.TouchUtils;
import android.webkit.WebView;


public class ScrollTo implements Action {

    @SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
    public Result execute(String... args) {
    	//TODO: Should do horizontal scrolling if needed
    	final String uiQuery = "webView " + args[0] + ":'"+ args[1] + "'";
		List queryResult = new sh.calaba.instrumentationbackend.query.Query(uiQuery).executeQuery();
		if (queryResult.isEmpty()) {
			return new Result(false,"Query found no elements: "+Arrays.asList(args));
		}
		final Map<String, Object> firstVisibleRectangle = QueryHelper.findFirstVisibleRectangle(queryResult);
		
		boolean success = (Boolean) UIQueryUtils.evaluateSyncInMainThread(new Callable() {
			public Object call() throws Exception {
				CalabashChromeClient calabashChromeClient = CalabashChromeClient.findAndPrepareWebViews().get(0);
				final WebView webView = calabashChromeClient.getWebView();
				webView.scrollTo(0, 0);		
				int scrolledTo = webView.getScrollY();
				while (!isVisible(firstVisibleRectangle, calabashChromeClient)) {
					TouchUtils.dragQuarterScreenUp(Actions.parentTestCase, InstrumentationBackend.solo.getCurrentActivity());
					if (scrolledTo != webView.getScrollY()) {
						scrolledTo = webView.getScrollY();
					} else {
						return false;
					}
				}
				return true;
			}
		});
		
		
	
		return new Result(success, "");
    }


    private boolean isVisible(Map<String, Object> rectangle, CalabashChromeClient calabashChromeClient) {
    	WebView webView = calabashChromeClient.getWebView();
    	int windowTop = webView.getScrollY();
    	int windowBottom = webView.getScrollY() + webView.getHeight();
    	int centerY = (int) ((Integer) (Math.round((Float)rectangle.get("center_y"))) * webView.getScale());

    	return windowTop < centerY && centerY < windowBottom;  	
    }

    @Override
    public String key() {
        return "scroll_to";
    }
}
