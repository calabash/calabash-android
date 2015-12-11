package sh.calaba.instrumentationbackend.intenthook;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

public interface IIntentHook {
    public void onRemoved();

    public IntentHookResult execStartActivity(Context who, IBinder contextThread, IBinder token, Activity target,
                                                       Intent intent, int requestCode, Bundle options);

    public IntentHookResult execStartActivity(Context who, IBinder contextThread, IBinder token, Fragment target,
                                                       Intent intent, int requestCode, Bundle options);
}
