package sh.calaba.instrumentationbackend.actions.activity;

import android.content.Context;
import android.content.res.Configuration;

import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;

public class GetActivityOrientation implements Action {

    @Override
    public Result execute(String... args) {
        Context context = InstrumentationBackend.instrumentation.getTargetContext();
        int orientation = context.getResources().getConfiguration().orientation;

        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            return new Result(true, "landscape");
        } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            return new Result(true, "portrait");
        } else {
            return Result.failedResult("Invalid orientation '" + orientation + "'");
        }
    }

    @Override
    public String key() {
        return "get_activity_orientation";
    }
}
