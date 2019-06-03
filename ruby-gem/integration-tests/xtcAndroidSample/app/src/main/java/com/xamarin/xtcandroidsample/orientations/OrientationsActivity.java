package com.xamarin.xtcandroidsample.orientations;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.xamarin.xtcandroidsample.R;

public class OrientationsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orientations);
    }

    public void goToPortraitActivity(View view) {
        startActivity(new Intent(this, PortraitActivity.class));
    }

    public void goToReversePortraitActivity(View view) {
        startActivity(new Intent(this, ReversePortraitActivity.class));
    }

    public void goToLandscapeActivity(View view) {
        startActivity(new Intent(this, LandscapeActivity.class));
    }

    public void goToReverseLandscapeActivity(View view) {
        startActivity(new Intent(this, ReverseLandscapeActivity.class));
    }
}
