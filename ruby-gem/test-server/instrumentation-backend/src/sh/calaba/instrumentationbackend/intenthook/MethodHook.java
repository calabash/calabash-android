package sh.calaba.instrumentationbackend.intenthook;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

import java.lang.reflect.Method;

import sh.calaba.instrumentationbackend.InstrumentationBackend;

public class MethodHook extends IntentHook {
    private Method method;

    public MethodHook(String methodName) {
        super();

        throw new UnsupportedOperationException("methodHook is not implemented");
    }

    @Override
    public IntentHookResult execStartActivity(Context who, IBinder contextThread, IBinder token,
                                              Activity target, Intent intent, int requestCode, Bundle options) {
        return null;
    }
}
