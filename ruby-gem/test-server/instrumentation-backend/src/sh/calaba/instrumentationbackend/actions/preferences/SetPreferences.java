package sh.calaba.instrumentationbackend.actions.preferences;

import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;
import android.content.SharedPreferences;

/**
 * Allows writing of SharedPreferences.
 * 
 * See Ruby API docs for more info:
 * https://github.com/calabash/calabash-android/blob/master/documentation/ruby_api.md
 * 
 * @author Juan Delgado (juan@ustwo.co.uk)
 */
public class SetPreferences implements Action {

	@Override
	public Result execute(String... args) {
		
		SharedPreferences preferences = null;
		
		try{
			preferences = PreferencesUtils.getPreferencesFromArgs(args, InstrumentationBackend.instrumentation.getTargetContext());
		} catch(Exception e){
			return Result.fromThrowable(e);
		}
		
		String[] parserdArgs = new String[args.length];
		
		int totalArgs = args.length;
		int added = 0;
		for(int i = 0; i <totalArgs; i++){
			
			String arg = args[i];
			
			// ignoring SharedPreferences name and weird chars 
			if(arg.equals(args[0]) || arg.equals("{") || arg.equals("}")){
				continue;
			}
			
			parserdArgs[added++] = arg;
		}
		
		SharedPreferences.Editor editor = preferences.edit();
		PreferencesUtils.setPreferences(editor, parserdArgs);
		editor.commit();
		
		return Result.successResult();
	}

	@Override
	public String key() {
		return "set_preferences";
	}
}