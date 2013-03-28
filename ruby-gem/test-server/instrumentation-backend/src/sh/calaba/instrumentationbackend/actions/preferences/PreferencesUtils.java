package sh.calaba.instrumentationbackend.actions.preferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Utility methods for SharedPreferences actions.
 * 
 * See Ruby API docs for more info:
 * https://github.com/calabash/calabash-android/blob/master/documentation/ruby_api.md
 * 
 * @author Juan Delgado (juan@ustwo.co.uk)
 */
public class PreferencesUtils {
	
	private final static String MISSING_NAME = "Missing SharedPreferences name";
	
	public static SharedPreferences getPreferences(String[] args, Context context){
		
		if(args == null){
			throw new IllegalArgumentException(MISSING_NAME);
		}
		
		String name = args[0];
		
		if(name == null){
			throw new IllegalArgumentException(MISSING_NAME);
		}
		
		return context.getSharedPreferences(name, Context.MODE_PRIVATE);
	}
}
