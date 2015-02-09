package sh.calaba.instrumentationbackend.actions;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.InterruptedException;
import java.lang.Override;
import java.lang.Runnable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import sh.calaba.instrumentationbackend.Command;
import sh.calaba.instrumentationbackend.FranklyResult;
import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.webview.CalabashChromeClient;
import sh.calaba.instrumentationbackend.actions.webview.ExecuteAsyncJavascript;
import sh.calaba.instrumentationbackend.actions.webview.ExecuteJavascript;
import sh.calaba.instrumentationbackend.json.JSONUtils;
import sh.calaba.instrumentationbackend.query.InvocationOperation;
import sh.calaba.instrumentationbackend.query.Operation;
import sh.calaba.instrumentationbackend.query.Query;
import sh.calaba.instrumentationbackend.query.QueryResult;
import sh.calaba.instrumentationbackend.query.WebContainer;
import sh.calaba.org.codehaus.jackson.map.ObjectMapper;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AlphaAnimation;
import android.webkit.WebView;

public class HttpServer extends NanoHTTPD {
	private static final String TAG = "InstrumentationBackend";
	private boolean running = true;
	private boolean ready = false;

	private final Lock lock = new ReentrantLock();
	private final Condition shutdownCondition = lock.newCondition();

	private static HttpServer instance;
	

	/**
	 * Creates and returns the singleton instance for HttpServer.
	 * 
	 * Can only be called once. Otherwise, you'll get an IllegalStateException.
	 */
	public synchronized static HttpServer instantiate(int testServerPort) {
		if (instance != null) {
			throw new IllegalStateException("Can only instantiate once!");
		}
		try {
			instance = new HttpServer(testServerPort);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return instance;
	}

	public synchronized static HttpServer getInstance() {
		if (instance == null) {
			throw new IllegalStateException("Must be initialized!");
		}
		return instance;
	}

	private HttpServer(int testServerPort) throws IOException {
		super(testServerPort, new File("/"));
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Response serve(String uri, String method, Properties header,
			Properties params, Properties files) {
		System.out.println("URI: " + uri);
		System.out.println("params: " + params);

		if (uri.endsWith("/ping")) {
			return new NanoHTTPD.Response(HTTP_OK, MIME_HTML, "pong");

		}
		else if (uri.endsWith("/dump")) {
			FranklyResult errorResult = null;
			try {
				
				
				String json = params.getProperty("json");
				
				
				if (json == null)
				{					
					Map<?,?> dumpTree = new ViewDump().dumpWithoutElements();
					return new NanoHTTPD.Response(HTTP_OK, "application/json;charset=utf-8", JSONUtils.asJson(dumpTree));
				}
				else 
				{
					ObjectMapper mapper = new ObjectMapper();
					Map dumpSpec = mapper.readValue(json, Map.class);
														
					List<Integer> path = (List<Integer>) dumpSpec.get("path");
					if (path == null)
					{
						Map<?,?> dumpTree = new ViewDump().dumpWithoutElements();
						return new NanoHTTPD.Response(HTTP_OK, "application/json;charset=utf-8", JSONUtils.asJson(dumpTree));					
					}
					Map<?,?> dumpTree = new ViewDump().dumpPathWithoutElements(path);
					if (dumpTree == null) {
						return new NanoHTTPD.Response(HTTP_NOTFOUND, "application/json;charset=utf-8", "{}");	
					}
					else {
						return new NanoHTTPD.Response(HTTP_OK, "application/json;charset=utf-8", JSONUtils.asJson(dumpTree));	
					}
					
					
				}

				
			} catch (Exception e ) {
				e.printStackTrace();
                errorResult = FranklyResult.fromThrowable(e);
            }
            return new NanoHTTPD.Response(HTTP_INTERNALERROR, "application/json;charset=utf-8", errorResult.asJson());
		}
        else if (uri.endsWith("/broadcast-intent")) {
            try {
                String json = params.getProperty("json");
                ObjectMapper mapper = new ObjectMapper();
                Map data = mapper.readValue(json, Map.class);

                Intent intent = new Intent();

                if (data.containsKey("action")) {
                    intent.setAction((String) data.get("action"));
                }

                Activity activity = InstrumentationBackend.solo.getCurrentActivity();

                Log.d(TAG, "Broadcasting intent " + intent);
                activity.sendBroadcast(intent);

                return new NanoHTTPD.Response(HTTP_OK, "application/json;charset=utf-8", "");
            } catch (Exception e) {
                e.printStackTrace();
                Exception ex = new Exception("Could not invoke method", e);

                return new NanoHTTPD.Response(HTTP_OK, "application/json;charset=utf-8", FranklyResult.fromThrowable(ex).asJson());
            }
        }
        else if (uri.endsWith("/backdoor")) {
            try {
                String json = params.getProperty("json");
                ObjectMapper mapper = new ObjectMapper();
                Map backdoorMethod = mapper.readValue(json, Map.class);

                String methodName = (String) backdoorMethod.get("method_name");
                List arguments = (List) backdoorMethod.get("arguments");
                Operation operation = new InvocationOperation(methodName, arguments);

                Application application = InstrumentationBackend.solo.getCurrentActivity().getApplication();
                Object invocationResult;

                invocationResult = operation.apply(application);

                if (invocationResult instanceof Map && ((Map) invocationResult).containsKey("error")) {
                    Context context = InstrumentationBackend.solo.getCurrentActivity();
                    invocationResult = operation.apply(context);
                }

                if (invocationResult instanceof Map && ((Map) invocationResult).containsKey("error")) {
                    Context context = getRootView().getContext();
                    invocationResult = operation.apply(context);
                }

                Map<String, String> result = new HashMap<String, String>();

                if (invocationResult instanceof Map && ((Map) invocationResult).containsKey("error")) {
                    result.put("outcome", "ERROR");
                    result.put("result", (String) ((Map) invocationResult).get("error"));
                    result.put("details", invocationResult.toString());
                } else {
                    result.put("outcome", "SUCCESS");
                    result.put("result", String.valueOf(invocationResult));
                }

                ObjectMapper resultMapper = new ObjectMapper();

                return new NanoHTTPD.Response(HTTP_OK, "application/json;charset=utf-8", resultMapper.writeValueAsString(result));
            } catch (Exception e) {
                e.printStackTrace();
                Exception ex = new Exception("Could not invoke method", e);

                return new NanoHTTPD.Response(HTTP_OK, "application/json;charset=utf-8", FranklyResult.fromThrowable(ex).asJson());
            }
        }
		else if (uri.endsWith("/map")) {
			FranklyResult errorResult = null;
			try {
				String commandString = params.getProperty("json");
				ObjectMapper mapper = new ObjectMapper();
				Map command = mapper.readValue(commandString, Map.class);
				
				String uiQuery = (String) command.get("query");
				uiQuery = uiQuery.trim();
				Map op = (Map) command.get("operation");
                String methodName = (String) op.get("method_name");
                List arguments = (List) op.get("arguments");

                if (methodName.equals("flash")) {
                    QueryResult queryResult = new Query(uiQuery, java.util.Collections.emptyList()).executeQuery();
                    List<View> views = queryResult.getResult();

                    if (views.isEmpty()) {
                        return new NanoHTTPD.Response(HTTP_OK, "application/json;charset=utf-8",
                                FranklyResult.failedResult("Could not find view to flash", "").asJson());
                    }

                    final Object firstItem = views.get(0);

                    if (!(firstItem instanceof View)) {
                        return new NanoHTTPD.Response(HTTP_OK, "application/json;charset=utf-8",
                                FranklyResult.failedResult("Only views can be flashed", "").asJson());
                    }

                    for (final View view : views) {
                        InstrumentationBackend.solo.runOnMainSync(new Runnable() {
                            @Override
                            public void run() {
                                Animation animation = new AlphaAnimation(1, 0);
                                animation.setRepeatMode(Animation.REVERSE);
                                animation.setDuration(200);
                                animation.setRepeatCount(5);
                                view.startAnimation(animation);
                            }
                        });

                        try {
                            Thread.sleep(1200);
                        } catch (InterruptedException e) {
                            return new NanoHTTPD.Response(HTTP_OK, "application/json;charset=utf-8",
                                    FranklyResult.failedResult("Interrupted while flashing", "").asJson());
                        }
                    }

                    return new NanoHTTPD.Response(HTTP_OK, "application/json;charset=utf-8",
                            FranklyResult.successResult(queryResult).asJson());
                }
                else if (methodName.equals("execute-javascript")) {
                    String javascript = (String) command.get("javascript");
                    List queryResult = new Query(uiQuery).executeQuery().getResult();
                    List<CalabashChromeClient.WebFuture> webFutures = new ArrayList<CalabashChromeClient.WebFuture>();

                    List<String> webFutureResults = new ArrayList<String>(webFutures.size());
                    boolean catchAllJavaScriptExceptions = true;
                    boolean success = true;

                    for (Object object : queryResult) {
                        String result;

                        if (object instanceof View) {
                            if (WebContainer.isValidWebContainer((View) object)) {
                                WebContainer webContainer = new WebContainer((View) object);

                                try {
                                    result = webContainer.evaluateSyncJavaScript(javascript, catchAllJavaScriptExceptions);
                                    success = true;
                                } catch (ExecutionException e) {
                                    result = e.getMessage();
                                    success = false;
                                }
                            } else {
                                result = "Error: " + object.getClass().getCanonicalName() + " is not recognized a valid web view.";
                                success = false;
                            }
                        } else {
                            result = "Error: will only call javascript on views, not " + object.getClass().getSimpleName();
                            success = false;
                        }

                        webFutureResults.add(result);
                    }

                    QueryResult jsQueryResults = new QueryResult(webFutureResults);
                    FranklyResult result = new FranklyResult(success, jsQueryResults, "", "");

                    return new NanoHTTPD.Response(HTTP_OK, "application/json;charset=utf-8",
                            result.asJson());
                }
               else {
                    QueryResult queryResult = new Query(uiQuery,arguments).executeQuery();

                    return new NanoHTTPD.Response(HTTP_OK, "application/json;charset=utf-8",
                            FranklyResult.successResult(queryResult).asJson());
                }
			} catch (Exception e ) {
				e.printStackTrace();
                errorResult = FranklyResult.fromThrowable(e);
            }
            return new NanoHTTPD.Response(HTTP_OK, "application/json;charset=utf-8", errorResult.asJson());
		} else if (uri.endsWith("/query")) {
			return new Response(HTTP_BADREQUEST, MIME_PLAINTEXT,
					"/query endpoint is discontinued - use /map with operation query");
        } else if (uri.endsWith("/gesture")) {
            FranklyResult errorResult;

            try {
                String json = params.getProperty("json");
                ObjectMapper mapper = new ObjectMapper();
                Map gesture = mapper.readValue(json, Map.class);

                (new MultiTouchGesture(gesture)).perform();

                return new NanoHTTPD.Response(HTTP_OK, "application/json;charset=utf-8",
                        FranklyResult.successResult(new QueryResult(Collections.emptyList())).asJson());

            } catch (Exception e ) {
                e.printStackTrace();
                errorResult = FranklyResult.fromThrowable(e);
            }

            return new NanoHTTPD.Response(HTTP_OK, "application/json;charset=utf-8", errorResult.asJson());
        } else if (uri.endsWith("/kill")) {
			lock.lock();
			try {
				running = false;
				System.out.println("Stopping test server");
				stop();

				shutdownCondition.signal();
				return new NanoHTTPD.Response(HTTP_OK, MIME_HTML,
						"Affirmative!");
			} finally {
				lock.unlock();
			}

		} else if (uri.endsWith("/ready")) {
			return new Response(HTTP_OK, MIME_HTML, Boolean.toString(ready));

		} else if (uri.endsWith("/screenshot")) {
			try {
				Bitmap bitmap;
				View rootView = getRootView();
				rootView.setDrawingCacheEnabled(true);
				rootView.buildDrawingCache(true);
				bitmap = Bitmap.createBitmap(rootView.getDrawingCache());
				rootView.setDrawingCacheEnabled(false);

				ByteArrayOutputStream out = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
				return new NanoHTTPD.Response(HTTP_OK, "image/png",
						new ByteArrayInputStream(out.toByteArray()));
			} catch (Exception e) {
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				e.printStackTrace(pw);
				return new NanoHTTPD.Response(HTTP_INTERNALERROR, null,
						sw.toString());
			}
		}

		System.out.println("header: " + header);
		System.out.println("params: " + params);
		Enumeration<String> propertyNames = (Enumeration<String>) params.propertyNames();
		while (propertyNames.hasMoreElements())
		{
			String s = propertyNames.nextElement();
			System.out.println("ProP "+s+" = "+params.getProperty(s));
		}
		System.out.println("files: " + files);

		String commandString = params.getProperty("json");
		System.out.println("command: " + commandString);
		String result = toJson(runCommand(commandString));
		System.out.println("result:" + result);

		return new NanoHTTPD.Response(HTTP_OK, MIME_HTML, result);
	}

	private View getRootView() {
		for (int i = 0; i < 25; i++) {
			try {
				View decorView = InstrumentationBackend.solo
						.getCurrentActivity().getWindow().getDecorView();
				if (decorView != null) {
					View rootView = decorView
							.findViewById(android.R.id.content);
					if (rootView != null) {
						return rootView;
					}
				}
				System.out.println("Retry: " + i);

				Thread.sleep(200);
			} catch (Exception e) {
			}
		}

		throw new RuntimeException("Could not find any views");
	}


	private String toJson(Result result) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			return mapper.writeValueAsString(result);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private Result runCommand(String commandString) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			Command command = mapper.readValue(commandString, Command.class);
			log("Got command:'" + command);
			return command.execute();
		} catch (Throwable t) {
			t.printStackTrace();
			return Result.fromThrowable(t);
		}
	}

	public void waitUntilShutdown() throws InterruptedException {
		lock.lock();
		try {
			while (running) {
				shutdownCondition.await();
			}
		} finally {
			lock.unlock();
		}
	}

	public static void log(String message) {
		Log.i(TAG, message);
	}

	public void setReady() {
		ready = true;
	}
}
