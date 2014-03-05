package sh.calaba.instrumentationbackend;

import android.os.Looper;
import android.os.MessageQueue;
import sh.calaba.instrumentationbackend.actions.Actions;
import sh.calaba.instrumentationbackend.actions.HttpServer;
import android.Manifest;
import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.robotium.solo.PublicViewFetcher;
import com.robotium.solo.SoloEnhanced;

import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class InstrumentationBackend extends ActivityInstrumentationTestCase2<Activity> {
    public static String testPackage;
    public static Class<? extends Activity> mainActivity;
    public static Bundle extras;
    
    private static final String TAG = "InstrumentationBackend";
    
    public static Instrumentation instrumentation;
    public static SoloEnhanced solo;
    public static PublicViewFetcher viewFetcher;
    public static Actions actions;

    @SuppressWarnings({ "deprecation", "unchecked" })
    public InstrumentationBackend() {
        super(testPackage, (Class<Activity>) mainActivity);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Intent i = new Intent();
        i.setClassName(testPackage, mainActivity.getName());
        i.putExtras(extras);
        setActivityIntent(i);

        actions = new Actions(getInstrumentation(), this);
        instrumentation = getInstrumentation();
    }

    /**
     * Here to have JUnit3 start the instrumentationBackend
     */

    public void testHook() throws Exception {

        final AtomicReference<Activity> activityReference = new AtomicReference<Activity>();
        Thread activityStarter = new Thread() {
            public void run() {
                activityReference.set(getActivity());
            }
        };
        activityStarter.start();
        activityStarter.join(10000);

        Activity activity = null;
        if (activityReference.get() != null) {
            activity = activityReference.get();
            System.out.println("testHook: Activity set to: " + activity);
        } else {
            System.out.println("testHook: Activity not set");
            try {

                Field mQueue = Looper.getMainLooper().getClass().getDeclaredField("mQueue");
                mQueue.setAccessible(true);
                MessageQueue messageQueue = (MessageQueue)mQueue.get(Looper.getMainLooper());

                Field f = messageQueue.getClass().getDeclaredField("mIdleHandlers");
                f.setAccessible(true);
                List<?> waiters = (List<?>)f.get(messageQueue);
                for(Object o : waiters) {
                    Class<?> activityGoingClazz = o.getClass();
                    if (!activityGoingClazz.getName().equals("android.app.Instrumentation$ActivityGoing")) {
                        continue;
                    }


                    Field mWaiterField = activityGoingClazz.getDeclaredField("mWaiter");
                    mWaiterField.setAccessible(true);
                    Object waiter = mWaiterField.get(o);
                    Class<?> activityWaiterClazz = waiter.getClass();

                    Field activityField = activityWaiterClazz.getDeclaredField("activity");
                    activityField.setAccessible(true);
                    activity = (Activity)activityField.get(waiter);

                    instrumentation.addMonitor(new Instrumentation.ActivityMonitor(activity.getClass().getName(), null, false));

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (activity != null) {
            solo = new SoloEnhanced(getInstrumentation(), activity);
            setActivity(activity);

            viewFetcher = new PublicViewFetcher(solo.getActivityUtils());

            HttpServer httpServer = HttpServer.getInstance();
            httpServer.setReady();
            httpServer.waitUntilShutdown();
            solo.finishOpenedActivities();
            System.exit(0);
        } else {
            throw new RuntimeException("Could not get detect the first Activity");
        }
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

    private void removeTestLocationProviders(Activity activity) {
        int hasPermission = solo.getCurrentActivity().checkCallingOrSelfPermission(Manifest.permission.ACCESS_MOCK_LOCATION);

        if (hasPermission == PackageManager.PERMISSION_GRANTED) {
            LocationManager locationService = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
            for (final String provider : locationService.getAllProviders()) {
                locationService.removeTestProvider(provider);
            }
        }
    }
}
