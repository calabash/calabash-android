package com.robotium.solo;

import java.util.List;

import android.app.Activity;
import android.app.Instrumentation;

public class SoloEnhanced extends Solo {
	private MapViewUtils mapViewUtils;

	public SoloEnhanced(final Instrumentation instrumentation, final Activity activity) {
		super(instrumentation, activity);
		mapViewUtils = new MapViewUtils(instrumentation, viewFetcher, sleeper, waiter);
	}

	public ActivityUtils getActivityUtils() {
		return activityUtils;
	}

	public void setMapCenter(final double lat, final double lon) {
		mapViewUtils.setCenter(lat, lon);
	}

	/** @return {lat,lon} */
	public double[] getMapCenter() {
		return mapViewUtils.getMapCenter();
	}

	public void panMapTo(final double lat, final double lon) {
		mapViewUtils.panTo(lat, lon);
	}

	public boolean zoomInOnMap() {
		return mapViewUtils.zoomIn();
	}

	public boolean zoomOutOnMap() {
		return mapViewUtils.zoomOut();
	}

	public int setMapZoom(final int zoomLevel) {
		return mapViewUtils.setZoom(zoomLevel);
	}

	public int getMapZoom() {
		return mapViewUtils.getZoom();
	}

	public List<String> getMapMarkerItems() {
		return mapViewUtils.getMarkerItems();
	}

	public String getMapMarkerItem(final String title) {
		return mapViewUtils.getMarkerItem(title);
	}

	/**
	 * @param title
	 * @param timeout
	 *          in ms
	 * @return
	 */
	public boolean tapMapMarkerItem(final String title, final long timeout) {
		return mapViewUtils.tapMarkerItem(title, timeout);
	}

	public boolean tapMapAwayFromMarkers(final int step) {
		return mapViewUtils.tapAwayFromMarkerItems(step);
	}

	/**
	 * @return [top, right, bottom, left] in decimal degrees
	 */
	public List<String> getMapBounds() {
		return mapViewUtils.getBounds();
	}

	public void doubleTapOnScreen(final float x, final float y) {
		clicker.clickOnScreen(x, y);
		clicker.clickOnScreen(x, y);
	}
}
