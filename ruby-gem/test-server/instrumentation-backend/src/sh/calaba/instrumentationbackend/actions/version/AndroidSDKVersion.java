 package sh.calaba.instrumentationbackend.actions.version;

 import sh.calaba.instrumentationbackend.Result;
 import sh.calaba.instrumentationbackend.actions.Action;
 import android.os.Build;


public class AndroidSDKVersion implements Action {
    public static final int VERSION = Build.VERSION.SDK_INT;

    @Override
    public Result execute(String... args) {
        return new Result(true, Integer.toString(VERSION));
    }

    @Override
    public String key() {
        return "android_sdk_version";
    }

}