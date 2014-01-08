package com.jayway.android.robotium.solo;

import java.util.List;

import android.app.Activity;
import android.app.Instrumentation;
import android.graphics.PointF;

public class SoloEnhanced extends Solo {
	private MapViewUtils mapViewUtils;

	public SoloEnhanced(Instrumentation instrumentation, Activity activity) {
		super(instrumentation, activity);
		this.mapViewUtils = new MapViewUtils(instrumentation, viewFetcher, sleeper, waiter);
	}
    public ActivityUtils getActivityUtils() {
        return activityUtils;
    }

	public void setMapCenter( double lat, double lon ) {
		mapViewUtils.setCenter(lat, lon);
	}

	/** @return {lat,lon} */
	public double[] getMapCenter() {
		return mapViewUtils.getMapCenter();
	}

	public void panMapTo( double lat, double lon ) {
		mapViewUtils.panTo(lat, lon);
	}

	public boolean zoomInOnMap() {
		return mapViewUtils.zoomIn();
	}

	public boolean zoomOutOnMap() {
		return mapViewUtils.zoomOut();
	}

	public int setMapZoom( int zoomLevel ) {
		return mapViewUtils.setZoom(zoomLevel);
	}

	public int getMapZoom() {
		return mapViewUtils.getZoom();
	}

	public List<String> getMapMarkerItems() {
		return mapViewUtils.getMarkerItems();
	}

	public String getMapMarkerItem( String title ) {
		return mapViewUtils.getMarkerItem( title );
	}

	/**
	 * @param title
	 * @param timeout in ms
	 * @return
	 */
	public boolean tapMapMarkerItem( String title, long timeout ) {
		return mapViewUtils.tapMarkerItem( title, timeout );
	}

	public boolean tapMapAwayFromMarkers( int step ) {
		return mapViewUtils.tapAwayFromMarkerItems( step );
	}

	/**
	 * @return [top, right, bottom, left] in decimal degrees
	 */
	public List<String> getMapBounds() {
		return mapViewUtils.getBounds();
	}

    public void doubleTapOnScreen(float x, float y) {
        clicker.clickOnScreen(x,y);
        clicker.clickOnScreen(x,y);
    }
}
