package sh.calaba.instrumentationbackend.actions.preferences;

import java.util.Map;
import java.util.Map.Entry;

import sh.calaba.instrumentationbackend.Result;
import sh.calaba.org.codehaus.jackson.map.DeserializationConfig.Feature;
import sh.calaba.org.codehaus.jackson.map.ObjectMapper;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;

/**
 * Utility methods for SharedPreferences actions and instrumentation.
 * 
 * See Ruby API docs for more info:
 * https://github.com/calabash/calabash-android/blob/master/documentation/ruby_api.md
 * 
 * @author Juan Delgado (juan@ustwo.co.uk)
 */
public class PreferencesUtils {
	
	private static final String MISSING_CONTEXT = "Missing context";
	private static final String MISSING_BUNDLE = "Missing bundle";
	private static final String MISSING_NAME = "Missing SharedPreferences name";
	private static final String MISSING_LOGCAT_ID = "Missing LogCat ID";
	private static final String MISSING_PREFERENCES = "Missing preferences";
	private static final String MISSING_RESULT = "Missing result";
	private static final String MISSING_EDITOR = "Missing editor";
	private static final String MISSING_VALUES = "Missing values";
	
	public static final String BUNDLE_NAME = "name";
	public static final String BUNDLE_LOGCAT_ID = "logcat";
	
	private final static ObjectMapper mapper = createJsonMapper();
	
	/**
	 * Returns a SharedPreferences object based on arguments and context. 
	 * 
	 * @param args First item on the array is the name expected to retrieve the SharedPreferences.
	 * @param context Typically the context of the tested application.
	 * @return A SharedPreferences instance.
	 * 
	 * @throws IllegalArgumentException if missing args, name or context.
	 */
	public static SharedPreferences getPreferencesFromArgs(String[] args, Context context){
		
		if(args == null){
			throw new IllegalArgumentException(MISSING_NAME);
		}
		
		if(context == null){
			throw new IllegalArgumentException(MISSING_CONTEXT);
		}
		
		String name = args[0];
		
		if(name == null){
			throw new IllegalArgumentException(MISSING_NAME);
		}
		
		return context.getSharedPreferences(name, Context.MODE_PRIVATE);
	}
	
	/**
	 * Returns a SharedPreferences object based on arguments and context.
	 * 
	 * @param bundle Expected to contain "name" and "logcat" properties.
	 * @param context Typically the context of the tested application.
	 * @return A SharedPreferences instance.
	 * 
	 * @throws IllegalArgumentException if missing bundle, name, logcat or context.
	 */
	public static SharedPreferences getPreferencesFromBundle(Bundle bundle, Context context){
		
		if(bundle == null){
			throw new IllegalArgumentException(MISSING_BUNDLE);
		}
		
		if(context == null){
			throw new IllegalArgumentException(MISSING_CONTEXT);
		}
		
		String logcat = bundle.getString(PreferencesUtils.BUNDLE_LOGCAT_ID);
		String name = bundle.getString(PreferencesUtils.BUNDLE_NAME);
		
		if(logcat == null){
			throw new IllegalArgumentException(MISSING_LOGCAT_ID);
		}		
		
		if(name == null){
			throw new IllegalArgumentException(MISSING_NAME);
		}
		
		return context.getSharedPreferences(name, Context.MODE_PRIVATE);
	}
	
	/**
	 * Iterates over the values stored in SharedPrefereces and adds them as JSON
	 * key:value pairs to the bonusInformation of a result.
	 * 
	 * @param preferences SharedPreferences object we want to add.
	 * @param result The Result object we want to add to.
	 * 
	 * @throws IllegalArgumentException if missing preferences or result.
	 */
	public static void addPreferencesToResult(SharedPreferences preferences, Result result){
		
		if(preferences == null){
			throw new IllegalArgumentException(MISSING_PREFERENCES);
		}
		
		if(result == null){
			throw new IllegalArgumentException(MISSING_RESULT);
		}
		
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
	}
	
	/**
	 * Adds values to a SharedPreferences Editor object. Key,value
	 * pairs are expected to come in the values array one after another
	 * such as [key1, value1, key2, value2], etc. This is a side effect
	 * of the Ruby to Java communication through ADB.
	 * 
	 * @param editor Editor to which add the values to.
	 * @param values Array of key/value pairs.
	 * 
	 * @throws IllegalArgumentException if missing editor or values.
	 */
	public static void setPreferences(Editor editor, String[] values){
		
		if(editor == null){
			throw new IllegalArgumentException(MISSING_EDITOR);
		}
		
		if(values == null){
			throw new IllegalArgumentException(MISSING_VALUES);
		}
		
		// we expect key/value pairs passed one after another such as:
		// [key1 value1 key2 value2], etc
		// So we go through them with a 2 step loop
		int totalParsedArgs = values.length;
		for(int i = 0; i <totalParsedArgs; i += 2){
			
			if(values[i] == null){
				break;
			}
			
			String key = values[i];
			String value = values[i+1];
			
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
	}
	
	/**
	 * Formats a Result object as a JSON string.
	 * 
	 * @param result The result that has to be formatted. 
	 * @return The result formatted as a JSON string
	 * 
	 * @throws IllegalArgumentException if missing result.
	 */
	public static String resultToJson(Result result) {
		
		if(result == null){
			throw new IllegalArgumentException(MISSING_RESULT);
		}
		
		try {
			return mapper.writeValueAsString(result);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private static ObjectMapper createJsonMapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, true);
		return mapper;
	}
}
