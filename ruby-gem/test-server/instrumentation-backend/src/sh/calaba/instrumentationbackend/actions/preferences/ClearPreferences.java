package sh.calaba.instrumentationbackend.actions.preferences;

import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;
import android.content.SharedPreferences;

/**
 * Allows clearing SharedPreferences.
 *
 * See Ruby API docs for more info:
 * https://github.com/calabash/calabash-android/blob/master/documentation/ruby_api.md
 * 
 * @author Juan Delgado (juan@ustwo.co.uk)
 */
public class ClearPreferences implements Action {

	@Override
	public Result execute(String... args) {
		
		try{
			
			SharedPreferences preferences = PreferencesUtils.getPreferencesFromArgs(args, InstrumentationBackend.instrumentation.getTargetContext());
			preferences.edit().clear().commit();
			
			return Result.successResult();
			
		} catch(Exception e) {
			return Result.fromThrowable(e);
		}
	}

	@Override
	public String key() {
		return "clear_preferences";
	}
}
