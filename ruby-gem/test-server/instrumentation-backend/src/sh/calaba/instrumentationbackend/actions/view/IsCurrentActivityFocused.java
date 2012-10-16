package sh.calaba.instrumentationbackend.actions.view;

import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;
import android.app.Activity;

/**
 * This action checks that the current activity under test has the focus.
 * 
 * This is useful to run tests across different applications (for example,
 * your application launching the browser). Since the instrumentation can
 * only work with the application under test, then all we can assert is that
 * said application has lost the focus.
 * 
 * @author Gianpiero Puleo (gianpi@ustwo.co.uk)
 * @author Juan Delgado (juan@ustwo.co.uk)
 */
@SuppressWarnings("deprecation")
public class IsCurrentActivityFocused implements Action {

	@Override
	public Result execute(String... args) {
		
		Activity currentActivity = InstrumentationBackend.solo.getCurrentActivity();
		
		String hasFocus = "false";
		
		if (currentActivity.hasWindowFocus()) {
			hasFocus = "true";
		}
		
		return new Result(true, hasFocus);	
	}

	@Override
	public String key() {
		return "is_current_activity_focused";
	}
}