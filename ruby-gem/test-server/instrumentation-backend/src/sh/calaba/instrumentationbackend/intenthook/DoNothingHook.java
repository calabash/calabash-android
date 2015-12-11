package sh.calaba.instrumentationbackend.intenthook;

import android.app.Activity;
import android.content.Intent;

public class DoNothingHook extends IntentHookWithDefault {
    public DoNothingHook() {
        super();
    }

    @Override
    public IntentHookResult defaultHook(Activity target, Intent intent) {
        return new IntentHookResult(null, true);
    }
}
