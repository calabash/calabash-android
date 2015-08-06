package sh.calaba.instrumentationbackend;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.content.pm.ResolveInfo;

import java.util.List;

public class ResumeTaskActivity extends Activity {
    public static final String EXTRA_PACKAGE_NAME = "packageName";

    public static final String EXTRA_TASK_BASE_COMPONENT = "taskBaseComponent";
    public static final String EXTRA_TASK_ID = "taskId";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        if (getIntent() == null) {
            throw new RuntimeException("Intent cannot be null");
        }

        if (getIntent().getExtras() == null) {
            throw new RuntimeException("Intent must have extras");
        }

        Bundle extras = getIntent().getExtras();
        String packageName = (String) extras.get(EXTRA_PACKAGE_NAME);

        ActivityManager.RecentTaskInfo task = getRecentTask(packageName);

        if (task == null) {
            setResult(Activity.RESULT_OK, null);
        } else {
            Intent result = new Intent();
            result.putExtra(EXTRA_TASK_BASE_COMPONENT, task.origActivity);
            result.putExtra(EXTRA_TASK_ID, task.id);

            resumeTask(task);

            setResult(Activity.RESULT_OK, result);
        }

        finish();
    }

    private ActivityManager.RecentTaskInfo getRecentTask(String packageName) {
        PackageManager packageManager = getPackageManager();

        for (ActivityManager.RecentTaskInfo task : getRecentTasks()) {
            Intent baseIntent = task.baseIntent;

            if (task.origActivity != null) {
                baseIntent.setComponent(task.origActivity);
            }

            baseIntent.setFlags((baseIntent.getFlags() & ~Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED) | Intent.FLAG_ACTIVITY_NEW_TASK);

            ResolveInfo resolveInfo = packageManager.resolveActivity(baseIntent, 0);

            if (resolveInfo != null) {
                if (packageName.equals(resolveInfo.activityInfo.packageName)) {
                    return task;
                }
            }
        }

        return null;
    }

    private List<ActivityManager.RecentTaskInfo> getRecentTasks() {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        return activityManager.getRecentTasks(100, ActivityManager.RECENT_IGNORE_UNAVAILABLE);
    }

    private void resumeTask(ActivityManager.RecentTaskInfo task) {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        if (task.id  >= 0) {
            activityManager.moveTaskToFront(task.id, ActivityManager.MOVE_TASK_WITH_HOME);
        } else {
            Intent baseIntent = task.baseIntent;

            if (task.origActivity != null) {
                baseIntent.setComponent(task.origActivity);
            }

            baseIntent.setFlags((baseIntent.getFlags()&~Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED) | Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(baseIntent);
        }
    }
}
