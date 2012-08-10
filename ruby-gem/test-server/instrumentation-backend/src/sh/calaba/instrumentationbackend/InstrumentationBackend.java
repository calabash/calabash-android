package sh.calaba.instrumentationbackend;

import sh.calaba.instrumentationbackend.actions.Actions;
import sh.calaba.instrumentationbackend.actions.HttpServer;
import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.jayway.android.robotium.solo.Solo;

public class InstrumentationBackend extends ActivityInstrumentationTestCase2 {
    public static final String TARGET_PACKAGE = "#ACTIVITY_PACKAGE#"; //Set from Ant at compile time
    private static final String LAUNCHER_ACTIVITY_FULL_CLASSNAME = "#ACTIVITY_QUALIFIED_NAME#"; //Set from Ant at compile time
    
    private static final String TAG = "InstrumentationBackend";
    
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
        
        solo = new Solo(getInstrumentation(), this.getActivity());
        actions = new Actions(getInstrumentation(), this);
        instrumentation = getInstrumentation();
        TestHelpers.loadIds(instrumentation.getContext());
    }

    /**
     * Here to have JUnit3 start the instrumentationBackend
     */
    public void testHook() throws Exception {
        HttpServer httpServer = HttpServer.getInstance();
        httpServer.setReady();
        httpServer.waitUntilShutdown();
    }

    @Override
    public void tearDown() throws Exception {
        HttpServer httpServer = HttpServer.getInstance();
        httpServer.stop();

        System.out.println("Finishing");
        try {
            solo.finalize();
        } catch (Throwable e) {
            e.printStackTrace();
        }

        this.getActivity().finish();
        super.tearDown();

    }

    public static void log(String message) {
        Log.i(TAG, message);
    }

    public static void logError(String message) {
        Log.e(TAG, message);
    }
}
