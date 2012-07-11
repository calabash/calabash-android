package sh.calaba.instrumentationbackend.actions.map;

import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;

/**
 * An optional "step" parameter lets the test script specify the number of pixels to increment down/across when
 * searching for a marker - defaults to 5 
 * 
 * @author Nicholas Albion
 */
public class TapAwayFromMarkers implements Action {

	@Override
	public Result execute(String... args) {
		int step = (args.length == 0) ? 5 : Integer.parseInt(args[0]);
		if( InstrumentationBackend.solo.tapMapAwayFromMarkers(step) ) {
			return Result.successResult();
		}
		return new Result(false, "Could not find any where to tap away from markers");
	}

	@Override
	public String key() {
		return "tap_map_away_from_markers";
	}
}
