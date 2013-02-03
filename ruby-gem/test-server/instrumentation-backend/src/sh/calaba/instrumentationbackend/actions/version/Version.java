package sh.calaba.instrumentationbackend.actions.version;

import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;


/**
 * Generated Code.
 * Do not edit.
 *
 */
public class Version implements Action {

	/******
	 * Generate Version Number
	 * DO NOT EDIT
	 * 
	 * Version number is changed by changing SERVER_VERSION
	 * in calabash-android/version.rb
	 * 
	 * When doing so, this version will be copied here when
	 * test server is built.
	 * 
	 */
	public static final String VERSION="0.4.0.pre16";
    
	@Override
    public Result execute(String... args) {                
        return new Result(true,VERSION);
    }

    @Override
    public String key() {
        return "version";
    }

}
