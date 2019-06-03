package com.xamarin.xtcandroidsample;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.webkit.WebView;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.TextView;

import java.io.InputStream;

public class ScollplicatedActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scollplicated_activty);

        final TextView title = (TextView) findViewById(R.id.textView1);

        int[] buttons = new int[] {R.id.oneButton, R.id.testingButton, R.id.twoButton, R.id.middleButton};


        moveAndResize(R.id.oneButton, 3, 3, 0, 0);
        moveAndResize(R.id.twoButton, 0, 3, 0, 0);
        moveAndResize(R.id.middleButton, 1, 1, 2, 1);


        for(int buttonId: buttons) {
            Button button = (Button) findViewById(buttonId);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    title.setText(view.getResources().getResourceEntryName(view.getId()));
                }
            });

        }
    }

    private void moveAndResize(int id, int x, int y, int w, int h) {
        Display display = getWindowManager().getDefaultDisplay();
        View v = (View) findViewById(id);
        AbsoluteLayout.LayoutParams layoutParams = (AbsoluteLayout.LayoutParams) v.getLayoutParams();
        layoutParams.x = x * display.getWidth();
        layoutParams.y = y * display.getHeight();
        if(w != 0)
        {
           layoutParams.width = w * display.getWidth();
        }
        if(h != 0)
        {
            layoutParams.height = h * display.getHeight();
        }
        v.requestLayout();
    }
}
