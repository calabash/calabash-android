package sh.calaba.instrumentationbackend.query;

import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.webkit.WebView;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import sh.calaba.instrumentationbackend.actions.webview.CalabashChromeClient;
import sh.calaba.instrumentationbackend.actions.webview.JavaScriptExecuter;
import sh.calaba.instrumentationbackend.query.ast.UIQueryUtils;
import sh.calaba.org.codehaus.jackson.map.ObjectMapper;

public class WebContainer {
    private View view;

    public WebContainer(View webContainer) {
        this.view = webContainer;
    }

    public static boolean isValidWebContainer(View view) {
        WebContainer webContainer = new WebContainer(view);

        if (webContainer.isAndroidWebView()) {
            return true;
        } else if (webContainer.isCrossWalk()) {
            return true;
        } else {
            return false;
        }
    }

    public CalabashChromeClient.WebFuture evaluateAsyncJavaScript(String javaScript) {
        return evaluateAsyncJavaScript(javaScript, false);
    }

    public CalabashChromeClient.WebFuture evaluateAsyncJavaScript(String javaScript, boolean catchJavaScriptExceptions) {
        if (catchJavaScriptExceptions) {
            // Catch all JS exceptions
            javaScript =
                    "(function() {"
                            + "try {"
                            + javaScript
                            + "} catch (exception) {"
                            + "  return \"" + CalabashChromeClient.WebFuture.JS_ERROR_IDENTIFIER + "\" + exception;"
                            + "}"
                            + "})();";
        }

        if (isAndroidWebView()) {
            WebView webView = (WebView) getView();

            if (Build.VERSION.SDK_INT < 19) { // < Android 4.4
                CalabashChromeClient chromeClient = CalabashChromeClient.prepareWebView(webView);
                JavaScriptExecuter javaScriptExecuter = new JavaScriptExecuter(webView);

                // We have to wrap the method call in a function to allow calabash_result = xxx
                javaScript = "calabash_result = " + javaScript + ";prompt('calabash:' + calabash_result);";

                javaScriptExecuter.executeJavaScript(javaScript);

                return chromeClient.getResult() ;
            } else {
                webView.getSettings().setJavaScriptEnabled(true);

                final CalabashChromeClient.WebFuture webFuture =
                        new CalabashChromeClient.WebFuture(this);

                webView.evaluateJavascript(javaScript, new ValueCallback<String>() {
                    public void onReceiveValue(String response) {
                        ObjectMapper mapper = new ObjectMapper();

                        try {
                            Object value = mapper.readValue(response, Object.class);
                            webFuture.setResult("" + value);
                        } catch (IOException e) {
                            webFuture.completeExceptionally(e);
                        }
                    }
                });

                return webFuture;
            }
        } else if (isCrossWalk()) {
            Class<?> webContainerClass = getView().getClass();
            final CalabashChromeClient.WebFuture webFuture =
                    new CalabashChromeClient.WebFuture(this);

            View xWalkContent = getView();

            while (!superClassEquals(xWalkContent.getClass(), "org.xwalk.core.internal.XWalkContent")) {
                xWalkContent = getChildOf(xWalkContent);
            }

            // Enable javascript
            try {
                Method methodGetSettings = xWalkContent.getClass().getMethod("getSettings");
                Object xWalkSettings = methodGetSettings.invoke(xWalkContent);

                Method methodSetJavaScriptEnabled = xWalkSettings.getClass().
                        getMethod("setJavaScriptEnabled", boolean.class);

                methodSetJavaScriptEnabled.invoke(xWalkSettings, true);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }

            try {
                Method methodEvaluateJavascript =
                        webContainerClass.getMethod("evaluateJavascript",
                        String.class, android.webkit.ValueCallback.class);

                methodEvaluateJavascript.invoke(getView(), javaScript, new ValueCallback<String>() {
                    public void onReceiveValue(String response) {
                        ObjectMapper mapper = new ObjectMapper();

                        try {
                            Object value = mapper.readValue(response, Object.class);
                            webFuture.setResult("" + value);
                        } catch (IOException e) {
                            webFuture.completeExceptionally(e);
                        }
                    }
                });

                return webFuture;
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new RuntimeException(getView().getClass().getCanonicalName() + " is not recognized a valid web view.");
        }
    }

    public String evaluateSyncJavaScript(final String javaScript) throws ExecutionException {
        return evaluateSyncJavaScript(javaScript, false);
    }

    public String evaluateSyncJavaScript(final String javaScript,
                                         final boolean catchJavaScriptExceptions) throws ExecutionException {
        Callable<CalabashChromeClient.WebFuture> callable =
                new Callable<CalabashChromeClient.WebFuture>() {
            public CalabashChromeClient.WebFuture call() throws Exception {
                return evaluateAsyncJavaScript(javaScript, catchJavaScriptExceptions);
            }
        };

        try {
            Map value = (Map) UIQueryUtils.evaluateAsyncInMainThread(callable)
                    .get(10, TimeUnit.SECONDS);

            return (String) value.get("result");
        } catch (ExecutionException e) {
            throw e;
        } catch (InterruptedException e) {
            throw new ExecutionException("Timed out waiting for javascript execution", e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public float getScale() {
        if (isAndroidWebView()) {
            WebView webView = (WebView) getView();

            return webView.getScale();
        } else if (isCrossWalk()) {
            return getView().getContext().getResources().getDisplayMetrics().density;
        } else {
            throw new RuntimeException(getView().getClass().getCanonicalName() + " is not recognized a valid web view.");
        }
    }

    public Map<String, Integer> translateRectToScreenCoordinates(Map<String, Integer> rectangle) {
        try {
            float scale = getScale();

            int[] webviewLocation = new int[2];
            getView().getLocationOnScreen(webviewLocation);
            //center_x, center_y
            //left, top, width, height
            int center_x = (int)translateCoordToScreen(webviewLocation[0], scale, rectangle.get("center_x"));
            int center_y = (int)translateCoordToScreen(webviewLocation[1], scale, rectangle.get("center_y"));

            int x = (int)translateCoordToScreen(webviewLocation[0], scale, rectangle.get("left"));
            int y = (int)translateCoordToScreen(webviewLocation[1], scale, rectangle.get("top"));

            int width = (int)translateCoordToScreen(0, scale, rectangle.get("width"));
            int height = (int)translateCoordToScreen(0, scale, rectangle.get("height"));

            Map<String,Integer> result = new HashMap<String, Integer>(rectangle);

            result.put("x", x);
            result.put("y", y);
            result.put("center_x", center_x);
            result.put("center_y", center_y);

            result.put("width", width);
            result.put("height", height);

            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public View getView() {
        return view;
    }

    private boolean isAndroidWebView() {
        return (view instanceof android.webkit.WebView);
    }

    private boolean isCrossWalk() {
        return superClassEquals(getView().getClass(), "org.xwalk.core.internal.XWalkContent") ||
                superClassEquals(getView().getClass(), "org.xwalk.core.XWalkView");
    }

    private boolean superClassEquals(Class clazz, String className) {
        do {
            if (className.equals(clazz.getCanonicalName())) {
                return true;
            }
        } while((clazz = clazz.getSuperclass()) != Object.class);

        return false;
    }

    private View getChildOf(View view) {
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;

            return viewGroup.getChildAt(0);
        } else {
            return null;
        }
    }

    private float translateCoordToScreen(int offset, float scale, Object point) {
        return offset + ((Number)point).floatValue() *scale;
    }
}
