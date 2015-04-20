package sh.calaba.instrumentationbackend;

import java.lang.reflect.Method;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import sh.calaba.instrumentationbackend.actions.HttpServer;
import sh.calaba.instrumentationbackend.actions.version.Version;
import sh.calaba.instrumentationbackend.intenthook.IIntentHook;
import sh.calaba.instrumentationbackend.intenthook.IntentHookResult;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.UserHandle;

public class CalabashInstrumentationTestRunner extends InstrumentationTestRunnerExecStartActivityExposed {
	@Override
    public void onCreate(Bundle arguments) {
        StatusReporter statusReporter = new StatusReporter(getContext());
        Logger.info("Server version: " + Version.VERSION);

        final String mainActivity;

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
    public void execStartActivities(Context who, IBinder contextThread, IBinder token, Activity target, Intent[] intents, Bundle options) {
        Logger.info("execStartActivity 1");
        // We have no hooks for this
        super.execStartActivities(who, contextThread, token, target, intents, options);
    }

    @Override
    public void execStartActivitiesAsUser(Context who, IBinder contextThread, IBinder token, Activity target, Intent[] intents, Bundle options, int userId) {
        Logger.info("execStartActivity 2");
        // We have no hooks for this
        super.execStartActivitiesAsUser(who, contextThread, token, target, intents, options, userId);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public ActivityResult execStartActivity(final Context who, final IBinder contextThread, final IBinder token, final Fragment target, final Intent intent, final int requestCode, final Bundle options) {
        Logger.info("execStartActivity 3");
        Activity activity;

        if (target == null) {
            activity = null;
        } else {
            activity = target.getActivity();
        }

        return handleExecStartActivity(intent, activity, new ExecStartActivityHandler() {
            @Override
            public IntentHookResult whenFiltered(IIntentHook intentHook) {
                return intentHook.execStartActivity(who, contextThread, token, target, intent, requestCode, options);
            }

            @Override
            public ActivityResult whenUnhandled(Intent modifiedIntent) {
                return CalabashInstrumentationTestRunner.super.execStartActivity(who, contextThread, token, target, modifiedIntent, requestCode, options);
            }
        });
    }

    @Override
    public ActivityResult execStartActivity(final Context who, final IBinder contextThread, final IBinder token, final Activity target, final Intent intent, final int requestCode, final Bundle options) {
        Logger.info("execStartActivity 4");

        return handleExecStartActivity(intent, target, new ExecStartActivityHandler() {
            @Override
            public IntentHookResult whenFiltered(IIntentHook intentHook) {
                return intentHook.execStartActivity(who, contextThread,
                        token, target, intent, requestCode, options);
            }

            @Override
            public ActivityResult whenUnhandled(Intent modifiedIntent) {
                return CalabashInstrumentationTestRunner.super.execStartActivity(who, contextThread, token, target, modifiedIntent, requestCode, options);
            }
        });
    }

    @Override
    public ActivityResult execStartActivity(Context who, IBinder contextThread, IBinder token, Activity target, Intent intent, int requestCode, Bundle options, UserHandle user) {
        Logger.info("execStartActivity 5");
        // We have no hooks for this
        return super.execStartActivity(who, contextThread, token, target, intent, requestCode, options, user);
    }

    @Override
    public ActivityResult execStartActivityAsCaller(Context who, IBinder contextThread, IBinder token, Activity target, Intent intent, int requestCode, Bundle options, int userId) {
        Logger.info("execStartActivity 6");
        // We have no hooks for this
        return super.execStartActivityAsCaller(who, contextThread, token, target, intent, requestCode, options, userId);
    }

    public ActivityResult handleExecStartActivity(final Intent intent, final Activity target, ExecStartActivityHandler handler) {
        InstrumentationBackend.intents.add(intent);

        if (InstrumentationBackend.shouldFilter(intent, target)) {
            IIntentHook intentHook = InstrumentationBackend.useIntentHookFor(intent, target);
            IntentHookResult intentHookResult = handler.whenFiltered(intentHook);

            if (intentHookResult.isHandled()) {
                return intentHookResult.getActivityResult();
            } else {
                final Intent modifiedIntent;

                if (intentHookResult.getModifiedIntent() != null) {
                    modifiedIntent = intentHookResult.getModifiedIntent();
                } else {
                    modifiedIntent = intent;
                }

                return handler.whenUnhandled(modifiedIntent);
            }
        }

        return handler.whenUnhandled(intent);
    }


    private interface ExecStartActivityHandler {
        public IntentHookResult whenFiltered(IIntentHook intentHook);

        public ActivityResult whenUnhandled(Intent modifiedIntent);
    }
}
