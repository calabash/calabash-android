package sh.calaba.instrumentationbackend.intenthook;

import android.app.Instrumentation.ActivityResult;

public class IntentHookResult {
    private ActivityResult activityResult;
    private boolean handled;

    public IntentHookResult(ActivityResult activityResult, boolean handled) {
        this.activityResult = activityResult;
        this.handled = handled;
    }

    public ActivityResult getActivityResult() {
        return activityResult;
    }

    public boolean isHandled() {
        return handled;
    }
}
