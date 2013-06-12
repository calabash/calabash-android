package com.jayway.android.robotium.solo;

import java.util.List;

import android.app.Instrumentation;

public class SoloEnhanced extends Solo {
//	private Pincher pincher;
	private MapViewUtils mapViewUtils;

	public SoloEnhanced(Instrumentation instrumentation) {
		super(instrumentation);
		this.mapViewUtils = new MapViewUtils(instrumentation, viewFetcher, sleeper, waiter);
//		this.pincher = new Pincher(instrumentation, viewFetcher);
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
	
//	/**
//	 * Sorry, doesn't work yet
//	 * @param togetherOrApart - {@link Pincher#TOGETHER} or {@link Pincher#APART}
//	 */
//	public void pinch(int togetherOrApart) {
//		switch( togetherOrApart ) {
//		case Pincher.TOGETHER: pincher.pinch(Pincher.Direction.TOGETHER); break;
//		case Pincher.APART: pincher.pinch(Pincher.Direction.APART); break;
//		}
//	}
//	
//	/**
//	 * Sorry, doesn't work yet
//	 * @param togetherOrApart - {@link Pincher#TOGETHER} or {@link Pincher#APART}
//	 * @param view
//	 */
//	public void pinch(int togetherOrApart, View view) {
//		switch( togetherOrApart ) {
//		case Pincher.TOGETHER: pincher.pinch(Pincher.Direction.TOGETHER, view); break;
//		case Pincher.APART: pincher.pinch(Pincher.Direction.APART, view); break;
//		}
//	}
}
