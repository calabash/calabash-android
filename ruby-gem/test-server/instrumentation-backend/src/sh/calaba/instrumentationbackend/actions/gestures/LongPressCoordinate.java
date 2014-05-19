package sh.calaba.instrumentationbackend.actions.gestures;

import android.view.Display;
import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;

/**
 * <p>
 * Action that performs a long press on given coordinates.
 * </p>
 * <p>
 * Parameters:
 * <ul>
 * <li>args[0]: x coordinate (float)</li>
 * <li>args[1]: y coordinate (float)</li>
 * <li>args[2]: length of the long press in millisecond (optional, integer)</li>
 * </ul>
 * </p>
 */
public class LongPressCoordinate implements Action {
    @Override
    public Result execute(String... args) {
        Display display = InstrumentationBackend.solo.getCurrentActivity().getWindowManager().getDefaultDisplay();

        float x = Float.parseFloat(args[0]);
        float y = Float.parseFloat(args[1]);
		if (args.length > 2) {
			int time = Integer.parseInt(args[2]);
			InstrumentationBackend.solo.clickLongOnScreen(x, y, time);
		} else {
			InstrumentationBackend.solo.clickLongOnScreen(x, y);
		}

        return Result.successResult();
    }

    @Override
    public String key() {
        return "long_press_coordinate";
    }
}
