package sh.calaba.instrumentationbackend;

import android.os.Looper;
import android.os.MessageQueue;
import sh.calaba.instrumentationbackend.actions.Actions;
import sh.calaba.instrumentationbackend.actions.HttpServer;
import sh.calaba.instrumentationbackend.intenthook.ActivityIntentFilter;
import sh.calaba.instrumentationbackend.intenthook.IIntentHook;

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

import com.jayway.android.robotium.solo.PublicViewFetcher;
import com.jayway.android.robotium.solo.SoloEnhanced;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class InstrumentationBackend extends ActivityInstrumentationTestCase2<Activity> {
    public static String testPackage;
    public static String mainActivityName;
    public static Class<? extends Activity> mainActivity;
    public static Intent activityIntent;
    public static Bundle extras;
    
    private static final String TAG = "InstrumentationBackend";
    
    public static Instrumentation instrumentation;
    public static SoloEnhanced solo;
    public static PublicViewFetcher viewFetcher;
    public static Actions actions;
    public static List<Intent> intents = new ArrayList<Intent>();
    private static Map<ActivityIntentFilter, IntentHookWithCount> intentHooks =
            new HashMap<ActivityIntentFilter, IntentHookWithCount>();

    public InstrumentationBackend() {
        super((Class<Activity>)mainActivity);
    }

    @Override
    public Activity getActivity() {
        if (mainActivity != null) {
            return super.getActivity();
        }

        try {
            setMainActivity(Class.forName(mainActivityName).asSubclass(Activity.class));
            return super.getActivity();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void setMainActivity(Class<? extends Activity> mainActivity) {
        try {
            Field mActivityClass = ActivityInstrumentationTestCase2.class.getDeclaredField("mActivityClass");
            mActivityClass.setAccessible(true);
            mActivityClass.set(this, mainActivity);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        if (activityIntent != null) {
            // Extras are not passed if intent is given
            setActivityIntent(activityIntent);
        } else {
            Intent i = new Intent(Intent.ACTION_MAIN);
            i.setClassName(testPackage, mainActivityName);
            i.addCategory("android.intent.category.LAUNCHER");
            i.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);

            if (extras != null) {
                i.putExtras(extras);
            }

            setActivityIntent(i);
        }

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

    public static void putIntentHook(ActivityIntentFilter activityIntentFilter, IIntentHook intentHook,
                                     int hookUsageCount) {
        Logger.debug("Adding intent hook '" + intentHook + "' for '" + activityIntentFilter + "'");
        intentHooks.put(activityIntentFilter, new IntentHookWithCount(intentHook, hookUsageCount));
    }

    public static void removeIntentHook(ActivityIntentFilter activityIntentFilter) {
        Logger.debug("Removing intent hook for '" + activityIntentFilter + "'");

        IntentHookWithCount intentHookWithCount = intentHooks.get(activityIntentFilter);

        if (intentHookWithCount != null) {
            intentHookWithCount.getIntentHook().onRemoved();
        }

        intentHooks.remove(activityIntentFilter);
    }

    public static ActivityIntentFilter getFilterFor(Intent intent, Activity targetActivity) {
        for (ActivityIntentFilter activityIntentFilter : intentHooks.keySet()) {
            if (activityIntentFilter.match(intent, targetActivity)) {
                return activityIntentFilter;
            }
        }

        return null;
    }

    public static IIntentHook useIntentHookFor(Intent intent, Activity targetActivity) {
        ActivityIntentFilter activityIntentFilter = getFilterFor(intent, targetActivity);

        if (activityIntentFilter == null) {
            return null;
        } else {
            IntentHookWithCount intentHookWithCount = intentHooks.get(activityIntentFilter);

            intentHookWithCount.use();

            if (intentHookWithCount.shouldRemove()) {
                removeIntentHook(activityIntentFilter);
            }

            return intentHookWithCount.getIntentHook();
        }
    }

    public static boolean shouldFilter(Intent intent, Activity targetActivity) {
        ActivityIntentFilter activityIntentFilter = getFilterFor(intent, targetActivity);

        return (activityIntentFilter != null);
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

    private static class IntentHookWithCount {
        private IIntentHook intentHook;
        private int count;

        private IntentHookWithCount(IIntentHook intentHook, int count) {
            this.intentHook = intentHook;
            this.count = count;
        }

        public IIntentHook getIntentHook() {
            return intentHook;
        }

        public int getCount() {
            return count;
        }

        public void use() {
            if (count != -1) {
                count--;
            }
        }

        public boolean shouldRemove() {
            return (count == 0);
        }
    }
}
