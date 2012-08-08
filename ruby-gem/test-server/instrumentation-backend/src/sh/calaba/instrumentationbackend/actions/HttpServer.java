package sh.calaba.instrumentationbackend.actions;

import java.io.File;
import java.util.Properties;

import sh.calaba.instrumentationbackend.Command;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.org.codehaus.jackson.map.DeserializationConfig.Feature;
import sh.calaba.org.codehaus.jackson.map.ObjectMapper;
import android.util.Log;

public class HttpServer extends NanoHTTPD {
	private static final String TAG = "IntrumentationBackend";
	private boolean running = true;
	
	private final ObjectMapper mapper = createJsonMapper();

	public HttpServer() {
		super(7102, new File("/"));
	}

	public Response serve( String uri, String method, Properties header, Properties params, Properties files )
	{
		System.out.println("URI: " + uri);
		if ("/ping".equals(uri)) {
			return new NanoHTTPD.Response( HTTP_OK, MIME_HTML, "pong");
		} else if ("/kill".equals(uri)) {
			running = false;
			System.out.println("Stopping test server");
			stop();
			return new NanoHTTPD.Response( HTTP_OK, MIME_HTML, "Affirmative!");
		}
		
		String commandString = params.getProperty("command");
		System.out.println("command: "+ commandString);
		String result = toJson(runCommand(commandString));
		System.out.println("result:" + result);
		
		return new NanoHTTPD.Response( HTTP_OK, MIME_HTML, result);
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

	public boolean isRunning() {
		return running;
	}

	public static void log(String message) {
		Log.i(TAG, message);
	}
}
