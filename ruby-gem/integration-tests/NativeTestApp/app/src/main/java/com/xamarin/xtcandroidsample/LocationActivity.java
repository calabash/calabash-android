package com.xamarin.xtcandroidsample;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.concurrent.atomic.AtomicReference;


public class LocationActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_activity);

        // Acquire a reference to the system Location Manager
        final LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        final AtomicReference<String> provider = new AtomicReference<String>();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    String providerValue = provider.get();

                    if (providerValue == null) {
                        continue;
                    }

                    onLocationChanged(locationManager.getLastKnownLocation(providerValue));

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();

        findViewById(R.id.wifi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                provider.set(LocationManager.NETWORK_PROVIDER);
            }
        });

        findViewById(R.id.gps).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                provider.set(LocationManager.GPS_PROVIDER);
            }
        });


    }


    public void onLocationChanged(final Location location) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((TextView) findViewById(R.id.latitude)).setText(String.valueOf(location.getLatitude()));
                ((TextView) findViewById(R.id.longitude)).setText(String.valueOf(location.getLongitude()));
                ((TextView) findViewById(R.id.altitude)).setText(String.valueOf(location.getAltitude()));
                ((TextView) findViewById(R.id.bearing)).setText(String.valueOf(location.getBearing()));
                ((TextView) findViewById(R.id.speed)).setText(String.valueOf(location.getSpeed()));
            }
        });
    }
}
