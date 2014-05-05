package sh.calaba.instrumentationbackend.actions.activity;

import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;

import android.content.res.Configuration;

public class SetActivityOrientation implements Action {

    @Override
    public Result execute(String... args) {
        if (args == null || args.length == 0) {
            throw new IllegalArgumentException("No orientation provided. Use 'landscape', 'left', 'right', 'portrait', 'up' or 'down'");
        }
        String orientation = args[0].toLowerCase();

        if (orientation.equals("landscape") || orientation.equals("left") || orientation.equals("right")) {
            InstrumentationBackend.solo.setActivityOrientation(Configuration.ORIENTATION_LANDSCAPE);
        } else if(orientation.equals("portrait") || orientation.equals("up") || orientation.equals("down")) {
            InstrumentationBackend.solo.setActivityOrientation(Configuration.ORIENTATION_PORTRAIT);
        } else {
            throw new IllegalArgumentException("Invalid orientation '" + orientation + "'. Use 'landscape', 'left', 'right', 'portrait', 'up' or 'down'");
        }
        // Wait 100ms for orientation change to happen.
        sleep(100);

        return Result.successResult();
    }

    private void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String key() {
        return "set_activity_orientation";
    }
}
