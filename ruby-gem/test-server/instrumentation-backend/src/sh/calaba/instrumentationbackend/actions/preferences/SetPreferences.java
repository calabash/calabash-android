package sh.calaba.instrumentationbackend.actions.preferences;

import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;
import android.content.SharedPreferences;

/**
 * Allows writing of SharedPreferences.
 * 
 * Saves preferences as either int, float, 
 * boolean or string if all of the above fails (in that order).
 * 
 * The name of the SharedPreferences file is required as the first 
 * value of the arguments passed.
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
			preferences = PreferencesUtils.getPreferences(args, InstrumentationBackend.instrumentation.getTargetContext());
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
		
		// Since the Ruby side passes a Hash, we expect
		// here key/value pairs passed one after the other such as:
		// key1 value1 key2 value2....
		// So we go through them with a 2 step loop
		int totalParsedArgs = parserdArgs.length;
		for(int i = 0; i <totalParsedArgs; i += 2){
			
			if(parserdArgs[i] == null){
				break;
			}
			
			String key = parserdArgs[i];
			String value = parserdArgs[i+1];
			
			try {
				
				int x = Integer.parseInt(value);
				editor.putInt(key, x);
				
			} catch (NumberFormatException e){
				
				try {
					
					float y = Float.parseFloat(value);
					editor.putFloat(key, y);
					
				} catch (NumberFormatException e1) {
					
					if(value.equals("true") || value.equals("false")){
						editor.putBoolean(key, Boolean.parseBoolean(value));
					} else {
						editor.putString(key, value);
					}
				}
			} 
		}
		
		editor.commit();
		
		return Result.successResult();
	}

	@Override
	public String key() {
		return "set_preferences";
	}
}