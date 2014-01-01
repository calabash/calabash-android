package sh.calaba.instrumentationbackend.actions.webview;


import java.util.List;
import java.util.Map;

import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;
import sh.calaba.instrumentationbackend.actions.Actions;
import sh.calaba.instrumentationbackend.query.QueryResult;

import android.test.TouchUtils;
import android.webkit.WebView;


public class ScrollTo implements Action {
    
	@Override
    public Result execute(String... args) {
    	//TODO: Should do horizontal scrolling if needed
    	final String uiQuery = "android.webkit.WebView " + args[0] + ":'"+ args[1] + "'";

        CalabashChromeClient calabashChromeClient = CalabashChromeClient.findAndPrepareWebViews().get(0);
        WebView webView = calabashChromeClient.getWebView();

        scrollToTop(webView);

        while (keepScrolling(uiQuery, webView)) {
            TouchUtils.dragQuarterScreenUp(Actions.parentTestCase, InstrumentationBackend.solo.getCurrentActivity());
        }

		return new Result(isVisible(uiQuery, webView), "");
    }

    private void scrollToTop(final WebView webView) {
        InstrumentationBackend.instrumentation.runOnMainSync(new Runnable() {
            @Override
            public void run() {
                webView.scrollTo(0, 0);
            }
        });
    }

    private boolean keepScrolling(String uiQuery, WebView webView) {
        int centerY = getCenterY(uiQuery, webView);
        System.out.println("Keep scrolling centerY: "+  centerY);

        int[] location = new int[2];
        webView.getLocationOnScreen(location);
        int top = location[1];
        int bottom = top + webView.getHeight();

        System.out.println("isVisible top: "+  top);
        System.out.println("isVisible bottom: "+  bottom);

        return centerY > bottom;
    }

    private boolean isVisible(String uiQuery, WebView webView) {
        int centerY = getCenterY(uiQuery, webView);
        System.out.println("isVisible centerY: "+  centerY);

        int[] location = new int[2];
        webView.getLocationOnScreen(location);
        int top = location[1];
        int bottom = top + webView.getHeight();
        System.out.println("isVisible top: "+  top);
        System.out.println("isVisible bottom: "+  bottom);

        return top < centerY && centerY < bottom;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	private int getCenterY(String uiQuery, WebView webView) {
        QueryResult queryResult = new sh.calaba.instrumentationbackend.query.Query(uiQuery).executeQuery();
        if (queryResult.isEmpty()) {
            throw new RuntimeException("Query found no elements");
        }
        final Map<String, Object> firstVisibleRectangle = QueryHelper.findFirstVisibleRectangle(queryResult.asList());

        return Math.round((Float)firstVisibleRectangle.get("center_y"));
    }

    @Override
    public String key() {
        return "scroll_to";
    }
}
