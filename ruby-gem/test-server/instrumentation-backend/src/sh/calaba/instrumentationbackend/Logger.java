package sh.calaba.instrumentationbackend;

import android.util.Log;

public final class Logger {
    public final static String TAG = "calabash";

    public static void info(String message) {
        Log.i(TAG, message);
    }

    public static void debug(String message) {
        Log.d(TAG, message);
    }

    public static void verbose(String message) {
        Log.v(TAG, message);
    }

    public static void warn(String message) {
        Log.w(TAG, message);
    }

    public static void error(String message) {
        Log.e(TAG, message);
    }
}
