package sh.calaba.instrumentationbackend;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;


import sh.calaba.instrumentationbackend.actions.Actions;
import sh.calaba.org.codehaus.jackson.JsonGenerationException;
import sh.calaba.org.codehaus.jackson.map.JsonMappingException;
import sh.calaba.org.codehaus.jackson.map.ObjectMapper;
import sh.calaba.org.codehaus.jackson.map.DeserializationConfig.Feature;
import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.jayway.android.robotium.solo.Solo;

public class InstrumentationBackend extends ActivityInstrumentationTestCase2 {
    public static final String TARGET_PACKAGE = "#ACTIVITY_PACKAGE#"; //Set from Ant at compile time
    private static final String LAUNCHER_ACTIVITY_FULL_CLASSNAME = "#ACTIVITY_QUALIFIED_NAME#"; //Set from Ant at compile time
    
    private static final String TAG = "IntrumentationBackend";
    
    private ServerSocket myService;
    private Socket serviceSocket = null;
    private PrintStream resultStream;
    private BufferedReader commandStream;
    
    private ObjectMapper mapper;
    public static Instrumentation instrumentation;
    public static Solo solo;
    public static Actions actions;
    
    private static Class getActivityClass() {
    	try {
			return Class.forName(LAUNCHER_ACTIVITY_FULL_CLASSNAME);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
    }
    
    public InstrumentationBackend() {
        super(TARGET_PACKAGE, getActivityClass());
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        
        mapper = createJsonMapper();
        createSockets();
        solo = new Solo(getInstrumentation(), this.getActivity());
        actions = new Actions(getInstrumentation(), this);
        instrumentation = getInstrumentation();
        TestHelpers.loadIds(instrumentation.getContext());
    }


    /**
     * Here to have JUnit3 start the instrumentationBackend
     */
    public void testHook() throws Exception {
        startInstrumentationBackend();
        
    }

    @Override
    public void tearDown() throws Exception {
        System.out.println("Finishing");
        try {
            solo.finalize();
        } catch (Throwable e) {
            e.printStackTrace();
        }

        this.getActivity().finish();
        super.tearDown();

    }

    // TODO: create tearDown method. Should close sockets and streams
    // TODO: create setupUp method. create sockets, streams and jsonMapper,
    // build Action set.
    // TODO: Clean up waitForCucumberConnect
    // TODO: load actions

    public void startInstrumentationBackend() throws Exception {
        
        acceptConnectionFromCucumber();

        // listen for commands
        String commandString;
        while ((commandString = commandStream.readLine()) != null) {
            returnResult(runCommand(commandString));
        }

        // TODO: Move to teardown
        log("Cucumber disconnected");
        try {
            commandStream.close();
            resultStream.close();
            serviceSocket.close();
            myService.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void returnResult(Result result) throws JsonGenerationException, JsonMappingException, IOException {
        log("Result:" + result);
        resultStream.println(mapper.writeValueAsString(result));
        resultStream.flush();
    }

    private Result runCommand(String commandString) {
        try {
            System.out.println("CommandString:" + commandString);
            Command command = mapper.readValue(commandString, Command.class);
            log("Got command:'" + command);
            return command.execute();
//           String result = mapper.writeValueAsString(actions.lookup(command.getCommand()).execute(command.getArguments()));
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

    private void createResultStream() throws IOException {
        resultStream = new PrintStream(serviceSocket.getOutputStream());
    }

    private void createCommandStream() throws UnsupportedEncodingException, IOException {
        commandStream = new BufferedReader(new InputStreamReader(serviceSocket.getInputStream(), "UTF8"));
    }

    private ObjectMapper createJsonMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, true);
        return mapper;
    }

    private void acceptConnectionFromCucumber() throws Exception {
        log("Waiting for connection from Cucumber");

        try {
            serviceSocket = myService.accept();
        } catch (Exception e) {
            log("Timeout waiting for connection");
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        createCommandStream();
        createResultStream();

        String s;
        while ((s = commandStream.readLine()) != null) {
            if (s.equals("Ping!")) { // Cucumber connected
                resultStream.println("Pong!"); // Reply with acknowledgement
                resultStream.flush();
                break;
            }
        }
    }
    
    private void createSockets() throws IOException {
    	try {
    	    myService = new ServerSocket(7101);
    	    myService.setSoTimeout(120000);
    	} catch (SocketException e) {
    	    throw new RuntimeException("Could not create socket. Did you set the android.permission.INTERNET permission in your AndroidManifest.xml", e);
    	}
    }
    
    public static void log(String message) {
        Log.i(TAG, message);
    }

    public static void logError(String message) {
        Log.e(TAG, message);
    }
}
