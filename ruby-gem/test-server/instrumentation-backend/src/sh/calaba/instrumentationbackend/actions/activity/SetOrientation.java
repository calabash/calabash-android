package sh.calaba.instrumentationbackend.actions.map;

import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;

public class SetOrientation implements Action {

    @Override
    public Result execute(String... args) {
        String orientation = "";
        if (args != null && args.length >= 1) {
            orientation = args[0].toLowerCase();
        }

        int changed = -1;

        if( "landscape".contentEquals(orientation) ) {
            changed = 0;
        } else if( "portrait".contentEquals(orientation) ) {
            changed = 1;
        }

        if (changed != -1) {
            InstrumentationBackend.solo.setActivityOrientation(changed);
            // Wait 100ms for orientation to happen.
            try { Thread.sleep(100); } catch (Exception e) {}
            // setActivityOrientation returns void so assume success.
            return Result.successResult();
        }

        return new Result(false, "Requested invalid orientation: '" + orientation + "' Expected 'landscape' or 'portrait'.");
    }

    @Override
    public String key() {
        return "set_orientation";
    }
}
