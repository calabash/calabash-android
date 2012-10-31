package sh.calaba.instrumentationbackend;

import sh.calaba.instrumentationbackend.actions.Actions;
import sh.calaba.instrumentationbackend.actions.HttpServer;
import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;
import android.location.LocationManager;
import android.util.Log;

import com.jayway.android.robotium.solo.SoloEnhanced;

public class InstrumentationBackend extends ActivityInstrumentationTestCase2 {
    public static String testPackage;
    public static Class mainActivity;
    
    private static final String TAG = "InstrumentationBackend";
    
    public static Instrumentation instrumentation;
    public static SoloEnhanced solo;
    public static Actions actions;

    public InstrumentationBackend() {
        super(testPackage, mainActivity);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        
        solo = new SoloEnhanced(getInstrumentation(), this.getActivity());
        actions = new Actions(getInstrumentation(), this);
        instrumentation = getInstrumentation();
    }

    /**
     * Here to have JUnit3 start the instrumentationBackend
     */
    public void testHook() throws Exception {
        HttpServer httpServer = HttpServer.getInstance();
        httpServer.setReady();
        httpServer.waitUntilShutdown();
        solo.finishOpenedActivities();
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

        removeTestLocationProviders(this.getActivity());

        this.getActivity().finish();
        super.tearDown();

    }

    public static void log(String message) {
        Log.i(TAG, message);
    }

    public static void logError(String message) {
        Log.e(TAG, message);
    }

    private static void removeTestLocationProviders(Activity activity) {
        final LocationManager locationService =
            (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        for (final String provider : locationService.getAllProviders()) {
            locationService.removeTestProvider(provider);
        }
    }
}
