package sh.calaba.instrumentationbackend.actions.webview;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.os.Build;
import android.webkit.WebView;

public class JavaScriptExecuter {
    private WebView webView;
    private Object provider;

    public JavaScriptExecuter(WebView webView) {
        this.webView = webView;
    }

    public void executeJavaScript(String javaScript) {
        try {
            provider = getProvider();
            loadUrlImpl("javascript:" + javaScript);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void callSwitchOutDrawHistory() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Method method = provider.getClass().getDeclaredMethod("switchOutDrawHistory");
        method.setAccessible(true);
        method.invoke(provider);
    }

    private void loadUrlImpl(String url) throws NoSuchMethodException, IllegalAccessException, NoSuchFieldException, InstantiationException, InvocationTargetException, ClassNotFoundException {
        callSwitchOutDrawHistory();
        sendMessage(url);
    }

    private void sendMessage(String url) throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InstantiationException, InvocationTargetException, ClassNotFoundException {
        Object arg = getUrlData(url);
        Object webViewCore = getWebViewCore();

        int messageIdentifier = loadUrlMessage();

        Method sendMessageMethod = webViewCore.getClass().getDeclaredMethod("sendMessage", int.class, Object.class);
        sendMessageMethod.setAccessible(true);
        sendMessageMethod.invoke(webViewCore, messageIdentifier, arg);
    }

    private final String EVENT_HUB_CLASS_NAME = "android.webkit.WebViewCore$EventHub";

    private int loadUrlMessage() throws NoSuchFieldException, ClassNotFoundException, IllegalAccessException {
        Class<?> eventHubClass = Class.forName(EVENT_HUB_CLASS_NAME);
        Field loadUrlField = eventHubClass.getDeclaredField("LOAD_URL");
        loadUrlField.setAccessible(true);

        return loadUrlField.getInt(String.class);
    }

    private final String GET_URL_DATA_CLASS_NAME = "android.webkit.WebViewCore$GetUrlData";

    private Object getUrlData(String url) throws ClassNotFoundException, NoSuchMethodException, NoSuchFieldException, IllegalAccessException, InvocationTargetException, InstantiationException {
        // For Android OS pre 2.2 the url was a string
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO) {
            return url;
        } else {
            Class<?> getUrlDataClass = Class.forName(GET_URL_DATA_CLASS_NAME);
            Constructor<?> constructor = getUrlDataClass.getDeclaredConstructor();
            constructor.setAccessible(true);

            Field mUrlField = getUrlDataClass.getDeclaredField("mUrl");
            mUrlField.setAccessible(true);
            Field mExtraHeadersField = getUrlDataClass.getDeclaredField("mExtraHeaders");
            mExtraHeadersField.setAccessible(true);

            Object getUrlDataInstance = constructor.newInstance();
            mUrlField.set(getUrlDataInstance, url);
            mExtraHeadersField.set(getUrlDataInstance, null);

            return getUrlDataInstance;
        }
    }

    private Object getProvider() throws NoSuchFieldException, IllegalAccessException {
        // For Android OS pre 4.1 the implementation of loadUrl was within the WebView widget class
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            return webView;
        } else {
            Field fieldProvider = WebView.class.getDeclaredField("mProvider");
            fieldProvider.setAccessible(true);

            return fieldProvider.get(webView);
        }
    }

    private Object getWebViewCore() throws IllegalStateException, IllegalAccessException, NoSuchFieldException {
        if (provider == null) throw new IllegalStateException("Provider is null");

        Field fieldCore = provider.getClass().getDeclaredField("mWebViewCore");
        fieldCore.setAccessible(true);
        Object webViewCore = fieldCore.get(provider);

        return webViewCore;
    }
}