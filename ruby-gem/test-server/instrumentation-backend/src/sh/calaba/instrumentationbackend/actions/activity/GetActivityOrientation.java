package sh.calaba.instrumentationbackend.actions.activity;

import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;


public class GetActivityOrientation implements Action {

    @Override
    public Result execute(String... args) {

        Context app = InstrumentationBackend.solo.getCurrentActivity().getApplication();
        int orientation = app.getResources().getConfiguration().orientation;
        String message = (orientation == Configuration.ORIENTATION_LANDSCAPE ? "landscape" : "portrait");

        return Result.successResult(message);
    }

    @Override
    public String key() {
        return "get_activity_orientation";
    }
}
