package sh.calaba.instrumentationbackend.actions.map;

import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;

/**
 * @author Nicholas Albion
 */
public class SetMapZoom implements Action {

    @Override
    public Result execute(String... args) {
    	if( "in".equals(args[0]) ) {
    		return new Result( InstrumentationBackend.solo.zoomInOnMap() );
    	} else if( "out".equals(args[0]) ) {
    		return new Result( InstrumentationBackend.solo.zoomOutOnMap() );
    	}
    	
    	int zoomLevel = Integer.parseInt(args[0]);
        int newZoom = InstrumentationBackend.solo.setMapZoom( zoomLevel );
        
        if( newZoom == zoomLevel ) {
        	return Result.successResult();
        } else {
        	return new Result(false, "Requested zoom level: " + zoomLevel + " but current zoom level is " + newZoom);
        }
    }

    @Override
    public String key() {
        return "set_map_zoom";
    }
}