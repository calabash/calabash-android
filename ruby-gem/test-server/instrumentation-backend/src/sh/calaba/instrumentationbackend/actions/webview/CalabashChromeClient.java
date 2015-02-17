package sh.calaba.instrumentationbackend.actions.webview;

import java.io.IOException;
import java.lang.RuntimeException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import android.graphics.Bitmap;
import android.os.Looper;
import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.query.WebContainer;
import sh.calaba.instrumentationbackend.query.ast.UIQueryUtils;
import sh.calaba.org.codehaus.jackson.map.ObjectMapper;
import sh.calaba.org.codehaus.jackson.type.TypeReference;

import android.os.Build;
import android.os.ConditionVariable;
import android.os.Message;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.GeolocationPermissions;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebStorage;
import android.webkit.WebView;

public class CalabashChromeClient extends WebChromeClient {
	private WebChromeClient mWebChromeClient;
	private final WebView webView;
	private final WebFuture scriptFuture;

	public CalabashChromeClient(final WebView webView) {
		this.webView = webView;
		this.scriptFuture = new WebFuture(new WebContainer(webView));
		if (Build.VERSION.SDK_INT < 16) { // jelly bean
			try {
				Method methodGetConfiguration = webView.getClass().getMethod(
						"getWebChromeClient");
				mWebChromeClient = (WebChromeClient) methodGetConfiguration
						.invoke(webView);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}  else {
				
				/*
				 * pick up the chromeClient from the webView
				 * this is required because in Jelly Bean there is no getWebChromeClient
				 * above sdk 16.This will help HTML5 hybrid application (Cordova) to pass the prompt messages 
				 * to Native code. Previously the CalabashChromeClient did not populate the mWebChromeClient
				 * which lead to the Cordova.exec messages not forward to the Cordova ChromeClient.
				 */
				Field field = getChromeClientField(webView.getClass());
				if (field == null) {
					mWebChromeClient = null;
				}
				else {
					try {
						field.setAccessible(true);
						mWebChromeClient = (WebChromeClient) field.get(webView);
					} catch (IllegalArgumentException e) {					
						e.printStackTrace();
						throw new UnableToFindChromeClientException(e, webView);
					} catch (IllegalAccessException e) {
						
						e.printStackTrace();
						throw new UnableToFindChromeClientException(e, webView);
					}	
				}
	
							
		}

        Runnable setWebChromeClientRunnable = new Runnable() {
            @Override
            public void run() {
                Class<?> webViewClass = webView.getClass();
                boolean isCordovaWebView = superClassEquals(webViewClass, "org.apache.cordova.CordovaWebView");

                // Cordova web view changed its implementation of setWebChromeClient.
                //   it will now try to cast the given WebChromeClient to a CordovaChromeClient,
                //   thus failing
                if (isCordovaWebView) {
                    try {
                        CalabashChromeClient.this.webViewSetWebChromeClient(CalabashChromeClient.this);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    webView.setWebChromeClient(CalabashChromeClient.this);
                }
            }
        };

        UIQueryUtils.runOnViewThread(webView, setWebChromeClientRunnable);
	}

    private boolean superClassEquals(Class clazz, String className) {
        do {
            if (clazz.getCanonicalName().equals(className)) {
                return true;
            }
        } while((clazz = clazz.getSuperclass()) != Object.class);

        return false;
    }

    private void webViewSetWebChromeClient(WebChromeClient webChromeClient) throws NoSuchFieldException, IllegalAccessException,
            NoSuchMethodException, InvocationTargetException {
        String fieldName;

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            fieldName = "mProvider";
        } else {
            fieldName = "mCallbackProxy";
        }

        Field field = android.webkit.WebView.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        Object object = field.get(webView);

        String methodName = "setWebChromeClient";
        Method method = object.getClass().getMethod(methodName, WebChromeClient.class);
        method.invoke(object, webChromeClient);
    }

	/*
	 * returns the chromeClient from the WebView.
	 * recursively moves up to its superClass to get the chromeClient 
	 * if there is no chromeClient it returns null.
	 */
	private Field getChromeClientField(Class currentClass) {
		if (currentClass == null)
			return null;
		try {
			return currentClass.getDeclaredField("chromeClient");
		} catch (NoSuchFieldException e) {
			return getChromeClientField(currentClass.getSuperclass());
		}
	}

	@Override
	public boolean onJsPrompt(WebView view, String url, String message,
			String defaultValue, JsPromptResult r) {
		if (message != null && message.startsWith("calabash:")) {
			r.confirm("CALABASH_ACK");
			System.out.println("onJsPrompt: " + message.subSequence(0, Math.min(message.length(), 100)));
			String response = message.replaceFirst("calabash:", "");
			scriptFuture.setResult(response);
			return true;
		} else {
			if (mWebChromeClient == null) {
				r.confirm("CALABASH_ERROR");
				scriptFuture.complete();
				return true;
			} else {
				// TODO I'm not what this case does...
				return mWebChromeClient.onJsPrompt(view, url, message,
						defaultValue, r);
			}
		}
	}

	public WebView getWebView() {
		return webView;
	}

	public static CalabashChromeClient prepareWebView(WebView webView) {
		CalabashChromeClient calabashChromeClient = new CalabashChromeClient(
				webView);
		webView.getSettings().setJavaScriptEnabled(true);
		return calabashChromeClient;
	}

	public static List<CalabashChromeClient> findAndPrepareWebViews() {
		List<CalabashChromeClient> webViews = new ArrayList<CalabashChromeClient>();
		ArrayList<View> views = InstrumentationBackend.solo.getCurrentViews();
		for (View view : views) {
			if (view instanceof WebView) {
				WebView webView = (WebView) view;
				webViews.add(prepareWebView(webView));
			}
		}
		return webViews;

	}

	public WebFuture getResult() {
		return scriptFuture;
	}

    /* Overwrite all methods to delegate to previous webChromeClient */

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        if (mWebChromeClient != null) {
            mWebChromeClient.onProgressChanged(view, newProgress);
        }
    }

    @Override
    public void onReceivedTitle(WebView view, String title) {
        if (mWebChromeClient != null) {
            mWebChromeClient.onReceivedTitle(view, title);
        }
    }

    @Override
    public void onReceivedIcon(WebView view, Bitmap icon) {
        if (mWebChromeClient != null) {
            mWebChromeClient.onReceivedIcon(view, icon);
        }
    }

    @Override
    public void onReceivedTouchIconUrl(WebView view, String url, boolean precomposed) {
        if (mWebChromeClient != null) {
            mWebChromeClient.onReceivedTouchIconUrl(view, url, precomposed);
        }
    }

    @Override
    public void onShowCustomView(View view, CustomViewCallback callback) {
        if (mWebChromeClient != null) {
            mWebChromeClient.onShowCustomView(view, callback);
        }
    }

    @Override
    public void onShowCustomView(View view, int requestedOrientation, CustomViewCallback callback) {
        if (mWebChromeClient != null) {
            mWebChromeClient.onShowCustomView(view, requestedOrientation, callback);
        }
    }

    @Override
    public void onHideCustomView() {
        if (mWebChromeClient != null) {
            mWebChromeClient.onHideCustomView();
        }
    }

    @Override
    public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
        if (mWebChromeClient != null) {
            return mWebChromeClient.onCreateWindow(view, isDialog, isUserGesture, resultMsg);
        }

        return false;
    }

    @Override
    public void onRequestFocus(WebView view) {
        if (mWebChromeClient != null) {
            mWebChromeClient.onRequestFocus(view);
        }
    }

    @Override
    public void onCloseWindow(WebView window) {
        if (mWebChromeClient != null) {
            mWebChromeClient.onCloseWindow(window);
        }
    }

    @Override
    public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
        if (mWebChromeClient != null) {
            return mWebChromeClient.onJsAlert(view, url, message, result);
        }

        return false;
    }

    @Override
    public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
        if (mWebChromeClient != null) {
            return mWebChromeClient.onJsConfirm(view, url, message, result);
        }

        return false;
    }

    @Override
    public boolean onJsBeforeUnload(WebView view, String url, String message, JsResult result) {
        if (mWebChromeClient != null) {
            return mWebChromeClient.onJsBeforeUnload(view, url, message, result);
        }

        return false;
    }

    @Override
    public void onExceededDatabaseQuota(String url, String databaseIdentifier, long quota, long estimatedDatabaseSize, long totalQuota, WebStorage.QuotaUpdater quotaUpdater) {
        if (mWebChromeClient != null) {
            mWebChromeClient.onExceededDatabaseQuota(url, databaseIdentifier, quota, estimatedDatabaseSize, totalQuota, quotaUpdater);
        }
    }

    @Override
    public void onReachedMaxAppCacheSize(long requiredStorage, long quota, WebStorage.QuotaUpdater quotaUpdater) {
        if (mWebChromeClient != null) {
            mWebChromeClient.onReachedMaxAppCacheSize(requiredStorage, quota, quotaUpdater);
        }
    }

    @Override
    public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
        if (mWebChromeClient != null) {
            mWebChromeClient.onGeolocationPermissionsShowPrompt(origin, callback);
        }
    }

    @Override
    public void onGeolocationPermissionsHidePrompt() {
        if (mWebChromeClient != null) {
            mWebChromeClient.onGeolocationPermissionsHidePrompt();
        }
    }

    @Override
    public boolean onJsTimeout() {
        if (mWebChromeClient != null) {
            return mWebChromeClient.onJsTimeout();
        }

        return false;
    }

    @Override
    public void onConsoleMessage(String message, int lineNumber, String sourceID) {
        if (mWebChromeClient != null) {
            mWebChromeClient.onConsoleMessage(message, lineNumber, sourceID);
        }
    }

    @Override
    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
        if (mWebChromeClient != null) {
            return mWebChromeClient.onConsoleMessage(consoleMessage);
        }

        return false;
    }

    @Override
    public Bitmap getDefaultVideoPoster() {
        if (mWebChromeClient != null) {
            return mWebChromeClient.getDefaultVideoPoster();
        }

        return null;
    }

    @Override
    public View getVideoLoadingProgressView() {
        if (mWebChromeClient != null) {
            return mWebChromeClient.getVideoLoadingProgressView();
        }

        return null;
    }

    @Override
    public void getVisitedHistory(ValueCallback<String[]> callback) {
        if (mWebChromeClient != null) {
            mWebChromeClient.getVisitedHistory(callback);
        }
    }

    @SuppressWarnings("rawtypes")
	public static class WebFuture implements Future<Map> {
        public static final String JS_ERROR_IDENTIFIER = "CalabashJSError:";
		private final ConditionVariable eventHandled;
		private volatile boolean complete;
        private Throwable throwable;
		private String result;
		private final WebContainer webContainer;

		public WebContainer getWebContainer() {
            return webContainer;
        }

		public void complete() {
			this.complete = true;
			this.eventHandled.open();
		}

		public WebFuture(WebContainer webContainer) {
            this.webContainer = webContainer;
            eventHandled = new ConditionVariable();
            result = null;
            throwable = null;
        }

        public synchronized void completeExceptionally(Throwable ex) {
            throwable = ex;
            complete();
        }

		public synchronized void setResult(String result) {
            if (result != null && result.startsWith(JS_ERROR_IDENTIFIER)) {
                String text = result.substring(JS_ERROR_IDENTIFIER.length());
                this.result = text;
                completeExceptionally(new ExecutionException(text, null));
            } else {
                this.result = result;
                complete();
            }
		}

		public synchronized String getResult() {
			return this.result;
		}

		public boolean cancel(boolean mayInterruptIfRunning) {
			return false;
		}

		@Override
		public Map get() throws InterruptedException, ExecutionException {
			eventHandled.block();

            if(throwable != null) {
                throw new ExecutionException(throwable);
            }

			return asMap();
		}

		@Override
		public Map get(long timeout, TimeUnit unit)
				throws InterruptedException, ExecutionException,
				TimeoutException {
			eventHandled.block(unit.toMillis(timeout));

            if(throwable != null) {
                throw new ExecutionException(throwable);
            }

            if(!complete) {
                throw new TimeoutException("Timeout while waiting for value");
            }

			return asMap();
		}

		@Override
		public boolean isCancelled() {
			return false;
		}

		@Override
		public boolean isDone() {
			return complete;
		}

		public String getAsString() {				
			try {
				get(10,TimeUnit.SECONDS);
				return getResult();
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}			
		}
		
		@SuppressWarnings("unchecked")
		public Map asMap() {			
			HashMap m = new HashMap();
			m.put("calabashWebContainer", webContainer);
			m.put("result",getResult());			
			return m;
		}
	}
}
