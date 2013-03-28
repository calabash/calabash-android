package sh.calaba.instrumentationbackend.actions.preferences;

import java.util.Map;
import java.util.Map.Entry;

import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;
import android.content.SharedPreferences;

/**
 * Allows reading of SharedPreferences.
 * 
 * The name of the SharedPreferences file is required as the first 
 * value of the arguments passed.
 *
 * See Ruby API docs for more info:
 * https://github.com/calabash/calabash-android/blob/master/documentation/ruby_api.md
 * 
 * @author Juan Delgado (juan@ustwo.co.uk)
 */
public class GetPreferences implements Action {

	@Override
	public Result execute(String... args) {
		
		SharedPreferences preferences = null;
		
		try{
			preferences = PreferencesUtils.getPreferences(args, InstrumentationBackend.instrumentation.getTargetContext());
		} catch(Exception e){
			return Result.fromThrowable(e);
		}
		
		Result result = new Result(true);
		
		Map<String, ?> map = preferences.getAll();
		
		for(Entry<String, ?> entry : map.entrySet()){
			
			StringBuilder json = new StringBuilder();
			String value = null;
			
			json.append("{");
			
			if(entry.getValue() instanceof Integer ||
				entry.getValue() instanceof Float || 
				entry.getValue() instanceof Boolean){
				
				value = String.valueOf(entry.getValue());
				
			} else {
				value = "\"" + entry.getValue() + "\"";
			}
			
			json.append("\"key\": \"" + entry.getKey() + "\", ");
			json.append("\"value\": " + value);
			json.append("}");
			
			result.addBonusInformation(json.toString());
		}	

		return result;
	}
	
	@Override
	public String key() {
		return "get_preferences";
	}
}