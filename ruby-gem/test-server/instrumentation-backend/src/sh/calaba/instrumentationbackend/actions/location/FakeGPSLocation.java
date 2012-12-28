package sh.calaba.instrumentationbackend.actions.location;


import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;

import java.lang.reflect.Method;


public class FakeGPSLocation implements Action {
	
	static LocationProviderThread t;

    @Override
    public Result execute(String... args) {
        
        if(!doesDeviceProvideGPS()) {
            return new Result(false, "This devices does not support GPS");
        }
        
        final double latitude = Double.parseDouble(args[0]);
        final double longitude = Double.parseDouble(args[1]);

        
        if (t != null) {
        	t.finish();
        }
    	
    	t = new LocationProviderThread(latitude, longitude);
    	
    	t.start();
    	
        return Result.successResult();
    }
    
   
    private class LocationProviderThread extends Thread {
    	
    	private final double latitude;
		private final double longitude;
		
		boolean finish = false;

		LocationProviderThread(double latitude, double longitude) {
			this.latitude = latitude;
			this.longitude = longitude;
    	}
    	
    	@Override
		public void run() {
    		LocationManager locationManager = (LocationManager) InstrumentationBackend.solo.getCurrentActivity().getSystemService(Context.LOCATION_SERVICE);
    		locationManager.addTestProvider(LocationManager.NETWORK_PROVIDER, false, false, false, false, false, false, false, Criteria.POWER_LOW, Criteria.ACCURACY_FINE);
    		locationManager.addTestProvider(LocationManager.GPS_PROVIDER, false, false, false, false, false, false, false, Criteria.POWER_LOW, Criteria.ACCURACY_FINE);

    		locationManager.setTestProviderEnabled(LocationManager.NETWORK_PROVIDER, true);
    		locationManager.setTestProviderEnabled(LocationManager.GPS_PROVIDER, true);

    		while(!finish) {
				System.out.println("Mocking location to: (" + latitude + ", " + longitude + ")");
				setLocation(locationManager, LocationManager.NETWORK_PROVIDER, latitude, longitude);
				setLocation(locationManager, LocationManager.GPS_PROVIDER, latitude, longitude);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
			}
		}
    	
    	private void setLocation(LocationManager locationManager, String locationProvider, double latitude, double longitude) {

    		Location location = new Location(locationProvider);
    		location.setLatitude(latitude);
    		location.setLongitude(longitude);
    		location.setAccuracy(1);
    		location.setTime(System.currentTimeMillis());

            try {
                Method makeComplete = Location.class.getMethod("makeComplete");
                if (makeComplete != null) {
                    makeComplete.invoke(location);
                }
            } catch (Exception e) {
                //Method only available in Jelly Bean
            }

            locationManager.setTestProviderLocation(locationProvider, location);
    	}

    	public void finish() {
    		finish = true;
    	}
    }

    @Override
    public String key() {
        return "set_gps_coordinates";
    }

    private boolean doesDeviceProvideGPS() {
    LocationManager locationManager = (LocationManager) InstrumentationBackend.solo.getCurrentActivity().getSystemService(Context.LOCATION_SERVICE);
    if (locationManager.getProvider(LocationManager.GPS_PROVIDER) == null) {
        return false;
    } else {
        return true;
    }
    
    }
    
    /**
     * Adds new LocationTestProvider matching actual provider on device.
     * 
     * @param currentProvider
     * @param providerType
     */
    private void addTestProvider(LocationProvider currentProvider, String providerType) {
    LocationManager locationManager = (LocationManager) InstrumentationBackend.solo.getCurrentActivity().getSystemService(Context.LOCATION_SERVICE);
    
    locationManager.addTestProvider(providerType, 
                    currentProvider.requiresNetwork(), 
                    currentProvider.requiresSatellite(), 
                    currentProvider.requiresCell(), 
                    currentProvider.hasMonetaryCost(), 
                    currentProvider.supportsAltitude(), 
                    currentProvider.supportsSpeed(), 
                    currentProvider.supportsBearing(), 
                    currentProvider.getPowerRequirement(), 
                    currentProvider.getAccuracy());
    }


}
