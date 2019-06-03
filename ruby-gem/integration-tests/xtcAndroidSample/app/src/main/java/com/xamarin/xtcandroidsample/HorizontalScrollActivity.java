package com.xamarin.xtcandroidsample;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.FrameLayout;


public class HorizontalScrollActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horizontal_scroll);

        Display display = getWindowManager().getDefaultDisplay();

        AbsoluteLayout absoluteLayout = (AbsoluteLayout) findViewById(R.id.horizontalScrollAbs);
        Button start = (Button) findViewById(R.id.buttonStart);
        Button center = (Button) findViewById(R.id.buttonCenter);
        Button end = (Button) findViewById(R.id.buttonEnd);

        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) absoluteLayout.getLayoutParams();
        lp.width = display.getWidth() * 2 + 100;
        changeX(display.getWidth(), 0, start);
        changeX(display.getWidth(), 1, center);
        changeX(display.getWidth(), 2, end);
        absoluteLayout.requestLayout();
    }

    private void changeX(int width, int i, Button button) {
        AbsoluteLayout.LayoutParams lp = (AbsoluteLayout.LayoutParams) button.getLayoutParams();
        lp.x = i * width;
    }

}
