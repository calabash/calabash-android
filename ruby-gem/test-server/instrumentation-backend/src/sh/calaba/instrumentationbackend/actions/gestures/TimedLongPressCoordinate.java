package sh.calaba.instrumentationbackend.actions.gestures;

import android.view.Display;
import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;

public class TimedLongPressCoordinate implements Action {
    @Override
    public Result execute(String... args) {
        Display display = InstrumentationBackend.solo.getCurrentActivity().getWindowManager().getDefaultDisplay();

        float x = Float.parseFloat(args[0]);
        float y = Float.parseFloat(args[1]);
        int z = Integer.parseInt(args[2]);

        InstrumentationBackend.solo.clickLongOnScreen(x, y, z);

        return Result.successResult();
    }

    @Override
    public String key() {
        return "timed_long_press_coordinate";
    }
}
