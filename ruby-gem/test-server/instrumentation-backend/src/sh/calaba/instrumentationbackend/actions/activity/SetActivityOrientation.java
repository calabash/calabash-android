package sh.calaba.instrumentationbackend.actions.activity;

import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;

public class SetActivityOrientation implements Action {

    @Override
    public Result execute(String... args) {
        if (args == null || args.length == 0) {
            throw new IllegalArgumentException("No orientation provided. Use 'landscape' or 'portrait'");
        }
        String orientation = args[0].toLowerCase();

        if (orientation.equals("landscape")) {
            InstrumentationBackend.solo.setActivityOrientation(0);
        } else if(orientation.equals("portrait")) {
            InstrumentationBackend.solo.setActivityOrientation(1);
        } else {
            throw new IllegalArgumentException("Invalid orientation '" + orientation + "'. Use 'landscape' or 'portrait'");
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
