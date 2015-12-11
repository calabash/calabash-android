package sh.calaba.instrumentationbackend.intenthook;

import android.app.Activity;
import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Logger;

public class InstrumentationHook extends IntentHook {
    private ComponentName componentName;
    int testServerPort;
    String targetPackageName;
    String clazz;
    String mainActivity;

    public InstrumentationHook(ComponentName componentName, int testServerPort,
                               String targetPackageName, String clazz, String mainActivity) {
        super();

        this.componentName = componentName;
        this.testServerPort = testServerPort;
        this.targetPackageName = targetPackageName;
        this.clazz = clazz;
        this.mainActivity = mainActivity;

        if (this.mainActivity == null) {
            PackageManager packageManager =
                    InstrumentationBackend.instrumentation.getTargetContext().getPackageManager();
            ComponentName component =
                    packageManager.getLaunchIntentForPackage(targetPackageName).getComponent();
            this.mainActivity = component.getClassName();
        }
    }

    @Override
    public IntentHookResult execStartActivity(Context who, IBinder contextThread, IBinder token,
                                              Activity target, Intent intent, int requestCode,
                                              Bundle options) {
        try {
            /*public boolean startInstrumentation(ComponentName className, String profileFile,
                        int flags, Bundle arguments, IInstrumentationWatcher watcher,
                                IUiAutomationConnection connection, int userId) throws RemoteException;*/

            Method methodStartInstrumentation = activityManagerNativeClass.getMethod("startInstrumentation",
                    ComponentName.class,
                    String.class,
                    int.class,
                    Bundle.class,
                    Class.forName("android.app.IInstrumentationWatcher"),
                    Class.forName("android.app.IUiAutomationConnection"),
                    int.class);

            Bundle arguments = new Bundle();

            arguments.putString("target_package", targetPackageName);
            arguments.putString("main_activity", mainActivity);
            arguments.putString("test_server_port", String.valueOf(testServerPort));
            arguments.putString("class", clazz);
            arguments.putParcelable("intent_parcel", intent);

            Logger.debug("Instrumenting componentName: " + componentName + ", mainActivity: " +
                    mainActivity + ", targetPackageName" + targetPackageName);

            methodStartInstrumentation.invoke(
                    activityManagerNative, componentName, null, 0, arguments, null, null, 0);

            return new IntentHookResult(null, true);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public IntentHookResult execStartActivity(Context who, IBinder contextThread, IBinder token, Fragment target, Intent intent, int requestCode, Bundle options) {
        return null;
    }
}
