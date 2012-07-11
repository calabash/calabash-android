package sh.calaba.instrumentationbackend.actions.map;

import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;

public class GetMapZoom implements Action {

	@Override
    public Result execute(String... args) {
    	int zoomLevel = InstrumentationBackend.solo.getMapZoom();
        return new Result(true, Integer.toString(zoomLevel));
    }

    @Override
    public String key() {
        return "get_map_zoom";
    }
}
