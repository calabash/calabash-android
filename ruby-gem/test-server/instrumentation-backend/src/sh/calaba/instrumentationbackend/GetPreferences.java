package sh.calaba.instrumentationbackend;

import sh.calaba.instrumentationbackend.actions.preferences.PreferencesUtils;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.test.InstrumentationTestRunner;
import android.util.Log;

/**
 * Allows reading of SharedPreferences.
 * 
 * See Ruby API docs for more info:
 * https://github.com/calabash/calabash-android/blob/master/documentation/ruby_api.md
 * 
 * @author Juan Delgado (juan@ustwo.co.uk)
 */
public class GetPreferences extends InstrumentationTestRunner {

	@Override
    public void onCreate(Bundle arguments) {
		
		SharedPreferences preferences = null;

		try{
			preferences = PreferencesUtils.getPreferencesFromBundle(arguments, getTargetContext());
		} catch(Exception e){
			e.printStackTrace();
			return;
		}
		
		Result result = Result.successResult();
		PreferencesUtils.addPreferencesToResult(preferences, result);
		
		String logcat = arguments.getString(PreferencesUtils.BUNDLE_LOGCAT_ID);
		Log.d(logcat, PreferencesUtils.resultToJson(result));
	}
}
