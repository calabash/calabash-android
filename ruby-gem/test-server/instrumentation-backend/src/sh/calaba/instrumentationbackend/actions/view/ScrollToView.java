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
import sh.calaba.instrumentationbackend.query.WebContainer;
import sh.calaba.instrumentationbackend.query.ast.InvalidUIQueryException;
import sh.calaba.instrumentationbackend.query.ast.UIQueryUtils;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Bring views identified by query onto screen
 */

public class ScrollToView implements Action, IOnViewAction {
    @Override
    public Result execute(String... args) {
        ExecuteOnView executeOnView = new ExecuteOnView();
        return executeOnView.execute(this, args);
    }

    @Override
    public String key() {
        return "scroll_to_view";
    }

    @Override
    public String doOnView(View view) {
        scrollTo(view);
        return "success";
    }

    @Override
    public String doOnView(Map viewMap) {
        scrollTo(viewMap);
        return "success";
    }

    private void scrollTo(final View view) {
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
        }

    private void scrollTo(Map viewMap) {
            final WebContainer webContainer = (WebContainer) viewMap.get("calabashWebContainer");
            if (webContainer != null) {
                final View view = webContainer.getView();
                Map rectMap = (Map) viewMap.get("rect");
                final int x = (Integer)rectMap.get("x");
                final int y = (Integer)rectMap.get("y");
                final int height = (Integer)rectMap.get("height");
                final int width = (Integer) rectMap.get("width");

                UIQueryUtils.runOnViewThread(view, new Runnable() {
                    @Override
                    public void run() {
                        int[] webViewLocation = UIQueryUtils.getViewLocationOnScreen(view);

                        int webViewHeight = view.getHeight();
                        int webViewWidth = view.getWidth();
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
                        view.scrollBy(offsetX, offsetY);
                    }
                });
            }
        }
}
