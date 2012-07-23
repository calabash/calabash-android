package sh.calaba.instrumentationbackend.actions;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import sh.calaba.instrumentationbackend.Command;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.org.codehaus.jackson.map.DeserializationConfig.Feature;
import sh.calaba.org.codehaus.jackson.map.ObjectMapper;
import android.util.Log;

public class HttpServer extends NanoHTTPD {
	private static final String TAG = "IntrumentationBackend";
	
	private ObjectMapper mapper;

	public HttpServer() {
		super(7170, new File("/"));

		mapper = createJsonMapper();
	}

	public Response serve( String uri, String method, Properties header, Properties params, Properties files )
	{
		String commandString = params.getProperty("command");

		return new NanoHTTPD.Response( HTTP_OK, MIME_HTML, toJson(runCommand(commandString)));
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
			System.out.println("CommandString:" + commandString);
			Command command = mapper.readValue(commandString, Command.class);
			log("Got command:'" + command);
			return command.execute();
			//	           String result = mapper.writeValueAsString(actions.lookup(command.getCommand()).execute(command.getArguments()));
		} catch (Throwable t) { // Robotium throws AssertionErrors on occations,
			// we need to catch these and map them to json
			// results
			// TODO: Create result from Throwable
			t.printStackTrace();
			// String jsonResult =
			// mapper.writeValueAsString(Result.fromThrowable(t));
			// logError("Returning error:" + jsonResult);
			// output.println(jsonResult);
			// output.flush();
			return Result.fromThrowable(t);
		}
	}

	public static void log(String message) {
		Log.i(TAG, message);
	}



}
