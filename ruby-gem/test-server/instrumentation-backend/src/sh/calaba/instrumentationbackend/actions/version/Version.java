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
	 */
	public static final String VERSION="####VERSION####";

	@Override
    public Result execute(String... args) {
        return new Result(true,VERSION);
    }

    @Override
    public String key() {
        return "version";
    }

}
