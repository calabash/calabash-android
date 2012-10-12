package sh.calaba.instrumentationbackend;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

/**
 * @author casidiablo
 * @version 1.0
 */
public class WakeUp extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                WakeUp.this.finish();
            }
        }, 200);
    }
}