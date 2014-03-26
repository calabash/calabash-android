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
        float speed=0F;
        float bearing=0F;
        float accuracy=0F;
        try{
            int length=args.length;
            if(length==3){
                speed = Float.parseFloat(args[2]);
            }
            if(length==4){
                speed = Float.parseFloat(args[2]);
                bearing = Float.parseFloat(args[3]);
            }
            if(length==5){
                speed = Float.parseFloat(args[2]);
                bearing = Float.parseFloat(args[3]);
                accuracy = Float.parseFloat(args[4]);
            }
        }catch (Exception e){

        }

        if (t != null) {
            t.finish();
        }

        t = new LocationProviderThread(latitude, longitude,speed,bearing,accuracy);

        t.start();

        return Result.successResult();
    }


    private class LocationProviderThread extends Thread {

        private  double latitude;
        private  double longitude;
        private  float speed;
        private  float bearing;
        private  float accuracy;

        boolean finish = false;



        LocationProviderThread(double latitude, double longitude,float speed,float bearing,float accuracy) {
            this.latitude = latitude;
            this.longitude = longitude;
            this.speed=speed;
            this.bearing=bearing;
            this.accuracy=accuracy;
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
                setLocation(locationManager, LocationManager.NETWORK_PROVIDER, latitude, longitude,speed,bearing,accuracy);
                setLocation(locationManager, LocationManager.GPS_PROVIDER, latitude, longitude,speed,bearing,accuracy);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        private void setLocation(LocationManager locationManager, String locationProvider, double latitude, double longitude,float speed,float bearing,float accuracy) {

            Location location = new Location(locationProvider);
            location.setLatitude(latitude);
            location.setLongitude(longitude);
            location.setAccuracy(accuracy);
            location.setSpeed(speed);
            location.setBearing(bearing);
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
