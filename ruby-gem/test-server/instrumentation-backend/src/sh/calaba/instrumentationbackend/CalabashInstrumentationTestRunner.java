package sh.calaba.instrumentationbackend;

import java.lang.reflect.Method;

import android.Manifest;
import android.app.Activity;
import sh.calaba.instrumentationbackend.actions.HttpServer;
import sh.calaba.instrumentationbackend.intenthook.IIntentHook;
import sh.calaba.instrumentationbackend.intenthook.IntentHookResult;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;

public class CalabashInstrumentationTestRunner extends InstrumentationTestRunnerExecStartActivityExposed {
	@Override
    public void onCreate(Bundle arguments) {
        StatusReporter statusReporter = new StatusReporter(getContext());

        try {
            final String mainActivity;

            if (arguments.containsKey("main_activity")) {
                mainActivity = arguments.getString("main_activity");
            } else {
                PackageManager packageManager = getTargetContext().getPackageManager();
                Intent launchIntent =
                        packageManager.getLaunchIntentForPackage(arguments.getString("target_package"));

                if (launchIntent == null) {
                    statusReporter.reportFailure("E_NO_LAUNCH_INTENT_FOR_PACKAGE");
                    throw new RuntimeException("Not launch intent set for package '" + arguments.getString("target_package") + "'");
                }

                String mainActivityTmpName = launchIntent.getComponent().getClassName();

                try {
                    PackageInfo packageInfo = packageManager.getPackageInfo(arguments.getString("target_package"),
                            PackageManager.GET_ACTIVITIES);
                    ActivityInfo[] activityInfoArr = packageInfo.activities;

                    for (ActivityInfo activityInfo : activityInfoArr) {
                        if (activityInfo.name.equals(mainActivityTmpName) &&
                                activityInfo.targetActivity != null) {
                            mainActivityTmpName = activityInfo.targetActivity;
                            break;
                        }
                    }

                    mainActivity = mainActivityTmpName;
                } catch (PackageManager.NameNotFoundException e) {
                    throw new RuntimeException(e);
                }

                System.out.println("Main activity name automatically set to: " + mainActivity);

                if (mainActivity == null || mainActivity.isEmpty()) {
                    statusReporter.reportFailure("E_COULD_NOT_DETECT_MAIN_ACTIVITY");
                    throw new RuntimeException("Could not detect main activity");
                }
            }

            try {
                Context context = getTargetContext();
                Class<?> c = Class.forName("mono.MonoPackageManager");
                String[] strings = {context.getApplicationInfo().sourceDir};
                try {
                    // 64bit support
                    Method loadApplication = c.getDeclaredMethod("LoadApplication", Context.class, ApplicationInfo.class, String[].class);
                    loadApplication.invoke(null, context, context.getApplicationInfo(), strings);
                } catch (NoSuchMethodException e) {
                    Method loadApplication = c.getDeclaredMethod("LoadApplication", Context.class, String.class, String[].class);
                    loadApplication.invoke(null, context, null, strings);
                }
                System.out.println("Calabash loaded Mono");
                InstrumentationBackend.mainActivity = Class.forName(mainActivity).asSubclass(Activity.class);
            } catch (Exception e) {
                System.out.println("Calabash did not load Mono. This is only a problem if you are trying to test a Mono application");
            }

            try {
                // Start the HttpServer as soon as possible in a not-ready state
                HttpServer.instantiate(Integer.parseInt(arguments.getString("test_server_port")));
            } catch (RuntimeException e) {
                if (getTargetContext().checkCallingOrSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
                    statusReporter.reportFailure("E_NO_INTERNET_PERMISSION");
                }

                throw e;
            }

            InstrumentationBackend.testPackage = arguments.getString("target_package");
            
            if (arguments.containsKey("intent_parcel")) {
                InstrumentationBackend.activityIntent = arguments.getParcelable("intent_parcel");
            }

            Bundle extras = (Bundle) arguments.clone();
            extras.remove("target_package");
            extras.remove("main_activity");
            extras.remove("test_server_port");
            extras.remove("class");

            if (extras.isEmpty()) {
                extras = null;
            }

            InstrumentationBackend.extras = extras;

            try {
                InstrumentationBackend.mainActivityName = mainActivity;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            super.onCreate(arguments);
        } catch (RuntimeException e) {
            if (!statusReporter.hasReportedFailure()) {
                statusReporter.reportFailure(e);
            }

            throw e;
        }
	}

    @Override
    public ActivityResult execStartActivity(
            Context who, IBinder contextThread, IBinder token, Activity target,
            Intent intent, int requestCode, Bundle options) {
        InstrumentationBackend.intents.add(intent);

        if (InstrumentationBackend.shouldFilter(intent, target)) {
            IIntentHook intentHook = InstrumentationBackend.useIntentHookFor(intent, target);
            IntentHookResult intentHookResult = intentHook.execStartActivity(who, contextThread,
                    token, target, intent, requestCode, options);

            if (intentHookResult.isHandled()) {
                return intentHookResult.getActivityResult();
            } else {
                if (intentHookResult.getModifiedIntent() != null) {
                    intent = intentHookResult.getModifiedIntent();
                }
            }
        }

        return super.execStartActivity(who, contextThread, token, target, intent, requestCode, options);
    }
>>>>>>> TS: Add tracking of intents for the most basic execStartAcitivty
}
