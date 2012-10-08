package sh.calaba.instrumentationbackend.actions;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Properties;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import android.graphics.Bitmap;
import android.view.View;
import sh.calaba.instrumentationbackend.Command;
import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.org.codehaus.jackson.map.DeserializationConfig.Feature;
import sh.calaba.org.codehaus.jackson.map.ObjectMapper;
import android.util.Log;

public class HttpServer extends NanoHTTPD {
	private static final String TAG = "InstrumentationBackend";
    private boolean running = true;
    private boolean ready = false;

    private final Lock lock = new ReentrantLock();
    private final Condition shutdownCondition = lock.newCondition();

	private final ObjectMapper mapper = createJsonMapper();

    private static HttpServer instance;

    /**
     * Creates and returns the singleton instance for HttpServer.
     *
     * Can only be called once. Otherwise, you'll get an IllegalStateException.
     */
    public synchronized static HttpServer instantiate() {
        if(instance != null) {
            throw new IllegalStateException("Can only instantiate once!");
        }
        instance = new HttpServer();
        return instance;
    }

    /**
     * Returns the singleton instance for HttpServer.
     *
     * If {@link #instantiate()} hasn't already been called, an IllegalStateException is thrown.
     */
    public synchronized static HttpServer getInstance() {
        if(instance == null) {
            throw new IllegalStateException("Must be initialized!");
        }
        return instance;
    }

	private HttpServer() {
		super(7102, new File("/"));
	}

	public Response serve( String uri, String method, Properties header, Properties params, Properties files )
	{
		System.out.println("URI: " + uri);
		if (uri.endsWith("/ping")) {
			return new NanoHTTPD.Response( HTTP_OK, MIME_HTML, "pong");

        } else if (uri.endsWith("/kill")) {
            lock.lock();
            try {
                running = false;
                System.out.println("Stopping test server");
                stop();

                shutdownCondition.signal();
                return new NanoHTTPD.Response( HTTP_OK, MIME_HTML, "Affirmative!");
            } finally {
                lock.unlock();
            }

        } else if (uri.endsWith("/ready")) {
            return new Response(HTTP_OK, MIME_HTML, Boolean.toString(ready));


		} else if (uri.endsWith("/screenshot")) {
            Bitmap bitmap;
            View rootView = getRootView();
            rootView.setDrawingCacheEnabled(true);
            rootView.buildDrawingCache(true);
            bitmap = Bitmap.createBitmap(rootView.getDrawingCache());
            rootView.setDrawingCacheEnabled(false);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            return new NanoHTTPD.Response( HTTP_OK, "image/png", new ByteArrayInputStream(out.toByteArray()));
        }
		
        System.out.println("header: "+ header);
        System.out.println("params: "+ params);
        System.out.println("files: "+ files);


        String commandString = params.getProperty("json");
		System.out.println("command: "+ commandString);
		String result = toJson(runCommand(commandString));
		System.out.println("result:" + result);
		
		return new NanoHTTPD.Response( HTTP_OK, MIME_HTML, result);
	}

    private View getRootView() {
        for ( int i = 0; i < 25; i++) {
            try {
                View rootView = InstrumentationBackend.solo.getCurrentActivity().getWindow().getDecorView();
                if (rootView != null) {
                    return rootView;
                }
                System.out.println("Retry: " + i);
            
                Thread.sleep(200);
            } catch (Exception e) {
            }
        }
        
        throw new RuntimeException("Could not find any views");
    }

	private ObjectMapper createJsonMapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, true);
		return mapper;
	}

	private String toJson(Result result) {
		try {
			return mapper.writeValueAsString(result);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private Result runCommand(String commandString) {
		try {
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
            while(running) {
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
