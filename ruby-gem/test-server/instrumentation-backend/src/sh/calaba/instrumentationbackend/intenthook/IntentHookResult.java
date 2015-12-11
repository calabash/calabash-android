package sh.calaba.instrumentationbackend.intenthook;

import android.app.Instrumentation.ActivityResult;
import android.content.Intent;

public class IntentHookResult {
    private ActivityResult activityResult;
    private Intent modifiedIntent;
    private boolean handled;

    public IntentHookResult(ActivityResult activityResult, boolean handled) {
        this(activityResult, handled, null);
    }

    public IntentHookResult(ActivityResult activityResult, boolean handled, Intent modifiedIntent) {
        this.activityResult = activityResult;
        this.handled = handled;
        this.modifiedIntent = modifiedIntent;
    }

    public ActivityResult getActivityResult() {
        return activityResult;
    }

    public boolean isHandled() {
        return handled;
    }

    public Intent getModifiedIntent() {
        return modifiedIntent;
    }
}
