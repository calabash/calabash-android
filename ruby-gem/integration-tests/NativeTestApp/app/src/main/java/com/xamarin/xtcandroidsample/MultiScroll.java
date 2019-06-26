package com.xamarin.xtcandroidsample;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import java.io.InputStream;


public class MultiScroll extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_scroll);
        Display display = getWindowManager().getDefaultDisplay();
        int viewHeight = display.getHeight()/3;

        try {
            // All the logic is within the same try clause as we do not want to display any visual change on error
            InputStream is = getAssets().open("web_view_multiscroll.html");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            String content = new String(buffer, "UTF-8");

            DisplayMetrics metrics = new DisplayMetrics();
            display.getMetrics(metrics);

            content = content.replace("HEIGHT", Integer.toString(Math.round(viewHeight / metrics.density)));

            WebView webView = (WebView)this.findViewById(R.id.scrollViewCenter);
            // Note: The base URL parameter is not used
            webView.loadDataWithBaseURL("file:///android_assets/web_view_multiscroll.html", content, "text/html", "UTF-8", null);
        } catch (Exception e) {
            // Ignore
        }


        int[] viewToResize = new int[] {R.id.textView, R.id.textView1, R.id.textViewBottom,
                R.id.t2extView, R.id.t2extView1, R.id.t2extViewBottom  };

        for (int id : viewToResize) {
            View stretch = findViewById(id);
            stretch.getLayoutParams().height = viewHeight;
            stretch.requestLayout();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.multi_scroll, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
