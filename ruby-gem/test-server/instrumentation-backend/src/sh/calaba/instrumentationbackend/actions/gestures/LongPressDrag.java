package sh.calaba.instrumentationbackend.actions.gestures;

import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;

import android.util.DisplayMetrics;

public class LongPressDrag implements Action {

	@Override
	public Result execute(String... args) {

		Float fromX = new Float(args[1]);
		Float toX = new Float(args[2]);
		Float fromY = new Float(args[3]);
		Float toY = new Float(args[4]);
		Integer stepCount = new Integer(args[5]);

		DisplayMetrics dm = new DisplayMetrics();
		InstrumentationBackend.solo.getCurrentActivity().getWindowManager()
				.getDefaultDisplay().getMetrics(dm);
		Integer windowWidth = dm.widthPixels;
		Integer windowHeight = dm.heightPixels;

		final View view = TestHelpers.getViewById(args[0]);

        if(view == null) {
            return new Result(false, "Could not find view with id: '" + args[0] + "'");
        }
        InstrumentationBackend.solo.clickLongOnView(view);

		InstrumentationBackend.solo.drag(fromX / 100 * windowWidth, 
				toX / 100 * windowWidth, 
				fromY / 100 * windowHeight, 
				toY / 100 * windowHeight, 
				stepCount);

		return Result.successResult();
	}

	@Override
	public String key() {
		return "long_press_drag";
	}

}
