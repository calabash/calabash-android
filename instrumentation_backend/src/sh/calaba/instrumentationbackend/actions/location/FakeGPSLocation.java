package sh.calaba.instrumentationbackend.actions.location;


import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;


public class FakeGPSLocation implements Action {

    @Override
    public Result execute(String... args) {
        
        LocationManager locationManager = (LocationManager) InstrumentationBackend.solo.getCurrentActivity().getSystemService(Context.LOCATION_SERVICE);
        
        if(!doesDeviceProvideGPS()) {
            return new Result(false, "This devices does not support GPS");
        }
        
        LocationProvider currentGPSProvider = locationManager.getProvider(LocationManager.GPS_PROVIDER);
        addTestProvider(currentGPSProvider, LocationManager.GPS_PROVIDER);
            
        Location location = new Location(LocationManager.GPS_PROVIDER);
        location.setLatitude(Double.parseDouble(args[0]));
        location.setLongitude(Double.parseDouble(args[1]));
        location.setTime(System.currentTimeMillis());
        locationManager.setTestProviderLocation(LocationManager.GPS_PROVIDER, location);
        
        return Result.successResult();
    }

    @Override
    public String key() {
        return "set_gps_coordinates";
    }
    
    /**
     * Causes error if Device does not support given provider
     * 
     * @param provider
     * @return 
     */
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
