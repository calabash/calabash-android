package com.xamarin.xtcandroidsample;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class ScrollShortActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_short);
        Display display = getWindowManager().getDefaultDisplay();
        View stretch = findViewById(R.id.textViewStretch);

        stretch.getLayoutParams().height = display.getHeight();
        stretch.requestLayout();
    }
}
