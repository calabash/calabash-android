package sh.calaba.instrumentationbackend.actions.activity;

import java.util.ArrayList;

import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;
import android.app.Activity;

public class GetOpenedActivities implements Action {

    @Override
    public Result execute(String... args) {
        final ArrayList<Activity> activities = InstrumentationBackend.solo
                .getAllOpenedActivities();
        final ArrayList<String> opened = new ArrayList<String>(
                activities.size());

        for (final Activity activity : activities) {
            opened.add(activity.getClass().getSimpleName());
        }

        // opened is attached to bonusInformation
        return new Result(true, opened);
    }

    @Override
    public String key() {
        return "get_opened_activities";
    }
}