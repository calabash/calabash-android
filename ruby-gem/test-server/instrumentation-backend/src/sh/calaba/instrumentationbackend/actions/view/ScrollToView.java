package sh.calaba.instrumentationbackend.actions.view;

import android.graphics.Rect;
import android.view.View;
import android.webkit.WebView;

import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;
import sh.calaba.instrumentationbackend.actions.webview.QueryHelper;
import sh.calaba.instrumentationbackend.query.Operation;
import sh.calaba.instrumentationbackend.query.Query;
import sh.calaba.instrumentationbackend.query.QueryResult;
import sh.calaba.instrumentationbackend.query.ast.InvalidUIQueryException;
import sh.calaba.instrumentationbackend.query.ast.UIQueryUtils;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Bring views identified by query
 */

public class ScrollToView implements Action {
    @Override
    public Result execute(String... args) {
        if (args.length != 1) {
            return Result.failedResult("Query for identifying view must be provided.");
        }

        RememberFirst rememberFirst = new RememberFirst();
        try {
            Query query = new Query(args[0], Arrays.asList(rememberFirst));
            QueryResult queryResult = query.executeQuery();

            if (queryResult.isEmpty()) {
                return Result.failedResult("Query found no view(s).");
            }
            scrollTo(rememberFirst.first);
        } catch (InvalidUIQueryException e) {
            return Result.fromThrowable(e);
        }
        return new Result(true);
    }

    @Override
    public String key() {
        return "scroll_to_view";
    }


    private class RememberFirst implements Operation {
        Object first = null;

        @Override
        public Object apply(Object o) throws Exception {
            if (first == null) {
                first = o;
            }
            return o;
        }

        @Override
        public String getName() {
            return "First";
        }
    }

    private void scrollTo(Object o) {
        if (o instanceof View) {
            final View view = (View) o;
            UIQueryUtils.runOnViewThread(view, new Runnable() {
                @Override
                public void run() {
                    View rootView = view.getRootView();
                    Rect rect = new Rect();

                    if (view.getWidth() < rootView.getWidth()) { // smaller than parent
                        rect.left = 0;
                        rect.right = view.getWidth();
                    } else {
                        int delta = view.getWidth() - rootView.getWidth();
                        rect.left = delta / 2;
                        rect.right = rect.left + rootView.getWidth();
                    }

                    if (view.getHeight() < rootView.getHeight()) { // smaller than parent
                        rect.top = 0;
                        rect.bottom = view.getHeight();
                    } else {
                        int delta = view.getHeight() - rootView.getHeight();
                        rect.top = delta / 2;
                        rect.bottom = rect.top + view.getHeight();
                    }
                    view.requestRectangleOnScreen(rect, true);

                }
            });
        } else if (o instanceof Map) {
            Map m = (Map) o;
            final WebView webView = (WebView) m.get("webView");
            if (webView != null) {
                Map rectMap = (Map) m.get("rect");
                final int x = (Integer)rectMap.get("x");
                final int y = (Integer)rectMap.get("y");
                final int height = (Integer)rectMap.get("height");
                final int width = (Integer) rectMap.get("width");

                UIQueryUtils.runOnViewThread(webView, new Runnable() {
                    @Override
                    public void run() {
                        int[] webViewLocation = new int[2];
                        webView.getLocationOnScreen(webViewLocation);

                        int webViewHeight = webView.getHeight();
                        int webViewWidth = webView.getWidth();
                        int webviewY = webViewLocation[1];
                        int webviewX = webViewLocation[0];

                        int offsetY;
                        if(webViewHeight > height) {
                            int webviewBottom = webviewY + webViewHeight;
                            int elementBottom = y + height;
                            if(elementBottom > webviewBottom) {
                                offsetY = elementBottom - webviewBottom;
                            } else if(y < webviewY) {
                                offsetY = y - webviewY;
                            } else {
                                offsetY = 0;
                            }
                        } else {
                            int delta = height - webViewHeight;
                            offsetY = y-webviewY + delta/2;
                        }

                        int offsetX;
                        if(webViewWidth > width) {
                            int webviewRight = webviewX + webViewWidth;
                            int elementRight = x + width;
                            if(elementRight > webviewRight) {
                                offsetX = elementRight - webviewRight;
                            } else if(x < webviewX) {
                                offsetX = x - webviewX;
                            } else {
                                offsetX = 0;
                            }
                        } else {
                            int delta = width - webViewWidth;
                            offsetX = x-webviewX + delta/2;
                        }
                        webView.scrollBy(offsetX, offsetY);
                    }
                });
            }
        }
    }
}
