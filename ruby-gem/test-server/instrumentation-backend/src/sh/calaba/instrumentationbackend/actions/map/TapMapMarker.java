package sh.calaba.instrumentationbackend.actions.map;

import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;

/**
 * eg: tap_map_marker_by_title, "my marker"
 * eg: tap_map_marker_by_title, "my marker", "10000"   (keep trying for 10 seconds)
 * 
 * @author Nicholas Albion
 */
public class TapMapMarker implements Action {

	@Override
	public Result execute(String... args) {
		String title = args[0];
		long timeout = (args.length > 1) ? Long.parseLong(args[1]) : 10000;
		if( InstrumentationBackend.solo.tapMapMarkerItem(title, timeout) ) {
			return Result.successResult();
		}
		return new Result(false, "Could not find marker '" + title + "' to tap after waiting " + timeout + " ms");
	}

	@Override
	public String key() {
		return "tap_map_marker_by_title";
	}
}
