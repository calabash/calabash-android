package com.xamarin.xtcandroidsample;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by john7doe on 04/10/14.
 */
public class ScrollWebViewActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Display display = getWindowManager().getDefaultDisplay();


        try {
            // All the logic is within the same try clause as we do not want to display any visual change on error
            InputStream is = getAssets().open("web_view_scroll.html");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            String content = new String(buffer, "UTF-8");
            DisplayMetrics metrics = new DisplayMetrics();
            display.getMetrics(metrics);
            float twoScaled = 2 / metrics.density;
            content = content.replace("HEIGHT", Integer.toString(Math.round(display.getHeight() * twoScaled)));
            content = content.replace("WIDTH", Integer.toString(Math.round(display.getWidth() * twoScaled)));

            this.setContentView(R.layout.scroll_web_view);

            WebView webView = (WebView)this.findViewById(R.id.webViewScroll);
            // Note: The base URL parameter is not used
            webView.loadDataWithBaseURL("file:///android_assets/web_view.html", content, "text/html", "UTF-8", null);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}