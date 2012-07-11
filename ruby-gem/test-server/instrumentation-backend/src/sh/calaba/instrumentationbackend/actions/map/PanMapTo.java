package sh.calaba.instrumentationbackend.actions.map;

import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;

/**
 * Center on lat, lon
 * @author Nicholas Albion
 */
public class PanMapTo implements Action {

    @Override
    public Result execute(String... args) {
        InstrumentationBackend.solo.panMapTo( Double.parseDouble(args[0]), Double.parseDouble(args[1]) );
        return Result.successResult();
    }

    @Override
    public String key() {
        return "pan_map_to";
    }
}
