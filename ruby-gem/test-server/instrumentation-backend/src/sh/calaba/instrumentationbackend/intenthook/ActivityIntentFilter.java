package sh.calaba.instrumentationbackend.intenthook;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;

public class ActivityIntentFilter {
    private String action;
    private ComponentName componentName;

    public ActivityIntentFilter(String action, ComponentName componentName) {
        this.action = action;
        this.componentName = componentName;
    }

    public boolean match(Intent intent, Activity targetActivity) {
        return ((action == null || action.equals(intent.getAction()))
                && (componentName == null || componentName.equals(targetActivity.getComponentName())));
    }

    @Override
    public String toString() {
        return "ActivityIntentFilter[action=" + action + ", componentName=" + componentName + "]";
    }
}
