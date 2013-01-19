package sh.calaba.instrumentationbackend;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.test.InstrumentationTestRunner;


public class KillBackgroundProcess extends InstrumentationTestRunner {
	@SuppressWarnings("deprecation")
    @Override
    public void onCreate(Bundle arguments) {
        final String packageName = getTargetContext().getApplicationInfo().packageName;
        final ActivityManager manager =
                (ActivityManager)getTargetContext().getSystemService(Context.ACTIVITY_SERVICE);
        // Wrapper for killBackgroundProcesses
        // http://developer.android.com/reference/android/app/ActivityManager.html#restartPackage(java.lang.String)
        manager.restartPackage(packageName);
	}
}