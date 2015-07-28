package sh.calaba.instrumentationbackend;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

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

        ActivityManager.RunningTaskInfo task = getRunningTask(packageName);

        if (task == null) {
            setResult(Activity.RESULT_OK, null);
        } else {
            Intent result = new Intent();
            result.putExtra(EXTRA_TASK_BASE_COMPONENT, task.baseActivity);
            result.putExtra(EXTRA_TASK_ID, task.id);

            resumeTask(task.id);

            setResult(Activity.RESULT_OK, result);
        }

        finish();
    }

    private ActivityManager.RunningTaskInfo getRunningTask(String packageName) {
        for (ActivityManager.RunningTaskInfo task : getRunningTasks()) {
            if (packageName.equals(task.baseActivity.getPackageName())) {
                return task;
            }
        }

        return null;
    }

    private List<ActivityManager.RunningTaskInfo> getRunningTasks() {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        return activityManager.getRunningTasks(100);
    }

    private void resumeTask(int taskId) {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        activityManager.moveTaskToFront(taskId, ActivityManager.MOVE_TASK_WITH_HOME);
    }
}
