package sh.calaba.instrumentationbackend.actions.version;

import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;


public class Version implements Action {

	public static final String VERSION="0.4.0.pre12";
    
	@Override
    public Result execute(String... args) {                
        return new Result(true,VERSION);
    }

    @Override
    public String key() {
        return "version";
    }

}
