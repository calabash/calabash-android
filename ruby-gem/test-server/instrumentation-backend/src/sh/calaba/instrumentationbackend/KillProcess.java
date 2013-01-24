package sh.calaba.instrumentationbackend;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.os.Bundle;
import android.test.InstrumentationTestRunner;
import static android.os.Process.killProcess;

public class KillProcess extends InstrumentationTestRunner {

    private static void kill(RunningAppProcessInfo process) {
        try {
            killProcess(process.pid);
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(Bundle arguments) {
        final ActivityManager manager = (ActivityManager) getTargetContext()
                .getSystemService(Context.ACTIVITY_SERVICE);

        final String packageName = getTargetContext().getApplicationInfo().packageName;

        for (RunningAppProcessInfo process : manager.getRunningAppProcesses()) {

            if (process.processName.contentEquals(packageName)) {
                kill(process);
                continue;
            }

            // Process name may differ from package name so check all loaded
            // packages in the process.
            if (process.pkgList != null) {
                for (final String pkg : process.pkgList) {
                    if (pkg.contentEquals(packageName)) {
                        kill(process);
                        break;
                    }
                }
            }
        }
    }
}