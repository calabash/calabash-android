package sh.calaba.instrumentationbackend.actions;

import sh.calaba.instrumentationbackend.Result;


public class NullAction implements Action {
		
	private String missingKey;

    @Override
	public Result execute(String... args) {
		return new Result(false, "Could not find implementation for: '" + missingKey + "'");
	}

	@Override
	public String key() {
		return "nullAction";
	}
	
	public void setMissingKey(String key) {
	    this.missingKey = key;
	}
}
