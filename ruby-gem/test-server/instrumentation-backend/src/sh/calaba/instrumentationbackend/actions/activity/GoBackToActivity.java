package sh.calaba.instrumentationbackend.actions.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.view.KeyEvent;
import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;
import sh.calaba.instrumentationbackend.actions.Actions;

public class GoBackToActivity implements Action {

    @Override
    public Result execute(String... args) {

        if (args == null) {
            return Result
                    .failedResult("Target activity name must not be null.");
        }

        if (args.length != 1) {
            return Result.failedResult("Must pass exactly one argument.");
        }

        if (args[0].trim().length() == 0) {
            return Result.failedResult("Argument must not be whitespace.");
        }

        final String targetActivityName = args[0];

        final ArrayList<Activity> activities = InstrumentationBackend.solo
                .getAllOpenedActivities();
        boolean success = false;

        final ArrayList<String> opened = new ArrayList<String>(
                activities.size());

        for (final Activity activity : activities) {
            final String name = activity.getClass().getSimpleName();
            if (name.contentEquals(targetActivityName)) {
                success = true;
            }
            opened.add(name);
        }

        if (!success) {
            return new Result(false, opened);
        }

        while (!InstrumentationBackend.solo.getCurrentActivity().getClass()
                .getSimpleName().contentEquals(targetActivityName)) {
            try {
                Actions.parentInstrumentation
                        .sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
            } catch (Exception exception) {
            }
        }

        return new Result(true, opened);
    }

    @Override
    public String key() {
        return "go_back_to_activity";
    }
}