package sh.calaba.instrumentationbackend.actions.activity;

import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;

import android.app.Activity;
import android.content.res.Configuration;


public class GetActivityOrientation implements Action {

    @Override
    public Result execute(String... args) {
        // don't really know how to detect reverse configurations for activity
        int orientation = InstrumentationBackend.solo.getCurrentActivity().getResources().getConfiguration().orientation;
        String payload = (orientation == Configuration.ORIENTATION_LANDSCAPE ? "landscape" : "portrait");

        // use bonus information to return orientation
        Result result = Result.successResult();
        result.addBonusInformation(payload);
        return result;
    }

    @Override
    public String key() {
        return "get_activity_orientation";
    }
}
