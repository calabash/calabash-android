package sh.calaba.instrumentationbackend.intenthook;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class IntentHookWithDefault extends IntentHook {
    public abstract IntentHookResult defaultHook(Activity target, Intent intent);

    @Override
    public IntentHookResult execStartActivity(Context who, IBinder contextThread, IBinder token, Activity target, Intent intent, int requestCode, Bundle options) {
        return defaultHook(target, intent);
    }

    @Override
    public IntentHookResult execStartActivity(Context who, IBinder contextThread, IBinder token, Fragment target, Intent intent, int requestCode, Bundle options) {
        if (target == null) {
            return defaultHook(null, intent);
        } else {
            return defaultHook(target.getActivity(), intent);
        }
    }
}
