package sh.calaba.instrumentationbackend.actions.view;

import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;
import android.app.Activity;
import android.app.TabActivity;

/**
 * @author Nicholas Albion
 */
@SuppressWarnings("deprecation")
public class GetActivityName implements Action {

	@Override
	public Result execute(String... args) {
		Activity currentActivity = InstrumentationBackend.solo.getCurrentActivity();
		
		Result result = new Result(true, currentActivity.getClass().getSimpleName());
		
		if( currentActivity instanceof TabActivity ) {
			result.addBonusInformation( ((TabActivity)currentActivity).getTabHost().getCurrentTabTag() );
		}
		
		return result;
	}

	@Override
	public String key() {
		return "get_activity_name";
	}
}
