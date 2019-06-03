package com.xamarin.xtcandroidsample;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.io.IOException;
import java.io.InputStream;

public class WebViewActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        try {
            this.setContentView(R.layout.web_view_sample);

            WebView webView = (WebView)this.findViewById(R.id.webView);
            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);

            Intent intent = getIntent();
            boolean showFrames = intent.getBooleanExtra(MainActivity.FRAMES, false);

            if (showFrames) {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1)
                {
                    webView.loadUrl("https://s3-eu-west-1.amazonaws.com/calabash-files/webpages-for-tests/page.html");
                }
                else {
                    webView.loadData(
                            "<html><body><h1>"
                                    + "Old Android doesn't support the SSL cert used by the frames test page. :("
                                    + "</h1></body></html>",
                            "text/html; charset=UTF-8",
                            null
                    );
                }
            } else {
                // All the logic is within the same try clause as we do not want to display any visual change on error
                InputStream is = getAssets().open("web_view.html");
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                String content = new String(buffer, "UTF-8");
                // Note: The base URL parameter is not used
                webView.loadDataWithBaseURL("file:///android_assets/web_view.html", content, "text/html", "UTF-8", null);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
