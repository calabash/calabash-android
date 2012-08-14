package sh.calaba.instrumentationbackend.actions.map;

import java.util.List;

import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;

/**
 * A succesful response includes bonusInformation: [top, right, bottom, left] in decimal degrees
 * @author Nicholas Albion
 */
public class GetMapBounds implements Action {

	@Override
	public Result execute(String... args) {
		List<String> bounds = InstrumentationBackend.solo.getMapBounds();
		Result result = new Result(true);
		result.setExtras( bounds );
		return result;
	}

	@Override
	public String key() {
		return "get_map_bounds";
	}
}
