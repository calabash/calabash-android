package com.xamarin.xtcandroidsample;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.view.ContextMenu;
import android.view.View;
import android.widget.TextView;

import java.text.DateFormat;

public class ViewDataActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_data);

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = null;

        for (String provider : locationManager.getProviders(true)) {
            Location newLocation = locationManager.getLastKnownLocation(provider);
            if (newLocation != null) {
                if (location == null || location.getAccuracy() > newLocation.getAccuracy()) {
                    location = newLocation;
                }
            }
        }

        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(this.getApplicationContext());
        DateFormat timeFormat = android.text.format.DateFormat.getTimeFormat(this.getApplicationContext());

        TextView textViewDate = (TextView)this.findViewById(R.id.textViewDate);
        textViewDate.setText(dateFormat.format(Settings.pickedDate));

        TextView textViewTime = (TextView)this.findViewById(R.id.textViewTime);
        textViewTime.setText(timeFormat.format(Settings.pickedDate));

        if (location != null) {
            TextView textViewLatitude = (TextView) this.findViewById(R.id.textViewLatitude);
            textViewLatitude.setText(String.valueOf(location.getLatitude()));

            TextView textViewLongitude = (TextView) this.findViewById(R.id.textViewLongitude);
            textViewLongitude.setText(String.valueOf(location.getLongitude()));
        }

        TextView textViewSpellCheckedInput = (TextView)this.findViewById(R.id.textViewSpellCheckedInput);
        textViewSpellCheckedInput.setText(Settings.spellCheckedInput);

        TextView textViewRadio = (TextView)this.findViewById(R.id.textViewRadio);
        textViewRadio.setText(Settings.radioSelectedValue);

        TextView textViewCheckBox = (TextView)this.findViewById(R.id.textViewCheckBox);
        textViewCheckBox.setText(Settings.checkBoxChecked ? "T" : "F");

        TextView textViewSeekBar = (TextView)this.findViewById(R.id.textViewSeekBar);
        textViewSeekBar.setText(String.valueOf(Settings.seekBarProgress));

        registerForContextMenu(this.findViewById(R.id.textViewCM));
    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
        super.onCreateContextMenu(contextMenu, view, contextMenuInfo);
        // Create your context menu here
        contextMenu.setHeaderTitle("Context Menu");
        contextMenu.add(0, view.getId(), 0, "Dogs");
        contextMenu.add(0, view.getId(), 1, "Cats");
    }
}
