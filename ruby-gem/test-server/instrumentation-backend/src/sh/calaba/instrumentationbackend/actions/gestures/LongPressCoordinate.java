package sh.calaba.instrumentationbackend.actions.gestures;

import android.view.Display;
import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;

public class LongPressCoordinate implements Action {
    @Override
    public Result execute(String... args) {
        Display display = InstrumentationBackend.solo.getCurrentActivity().getWindowManager().getDefaultDisplay();

        float x = Float.parseFloat(args[0]);
        float y = Float.parseFloat(args[1]);

        InstrumentationBackend.solo.clickLongOnScreen(x, y);

        return Result.successResult();
    }

    @Override
    public String key() {
        return "long_press_coordinate";
    }
}
