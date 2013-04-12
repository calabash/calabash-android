package sh.calaba.instrumentationbackend;

import java.util.Set;

import sh.calaba.instrumentationbackend.actions.preferences.PreferencesUtils;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.test.InstrumentationTestRunner;
import android.util.Log;

/**
 * Allows writing of SharedPreferences.
 * 
 * See Ruby API docs for more info:
 * https://github.com/calabash/calabash-android/blob/master/documentation/ruby_api.md
 * 
 * @author Juan Delgado (juan@ustwo.co.uk)
 */
public class SetPreferences extends InstrumentationTestRunner {

	@Override
    public void onCreate(Bundle arguments) {
		
		SharedPreferences preferences = null;

		try{
			preferences = PreferencesUtils.getPreferencesFromBundle(arguments, getTargetContext());
		} catch(Exception e){
			e.printStackTrace();
			return;
		}
		
	    Set<String> keys = arguments.keySet();
	    
	    String[] parserdArgs = new String[keys.size() * 2];
	    
	    int added = 0;
	    for (String key : keys) {
	    	
	    	if(key.equals(PreferencesUtils.BUNDLE_LOGCAT_ID) || key.equals(PreferencesUtils.BUNDLE_NAME)){
	    		continue;
	    	}
	    	
	    	parserdArgs[added++] = key;
	    	parserdArgs[added++] = arguments.get(key).toString();
	    }
		
		SharedPreferences.Editor editor = preferences.edit();
		
		PreferencesUtils.setPreferences(editor, parserdArgs);
		editor.commit();
	    
		String logcat = arguments.getString(PreferencesUtils.BUNDLE_LOGCAT_ID);
		Log.d(logcat, PreferencesUtils.resultToJson(Result.successResult()));
	}
}
