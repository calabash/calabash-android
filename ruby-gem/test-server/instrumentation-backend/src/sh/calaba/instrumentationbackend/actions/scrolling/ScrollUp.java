package sh.calaba.instrumentationbackend.actions.scrolling;


import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;
import sh.calaba.instrumentationbackend.actions.Actions;
import android.test.TouchUtils;


public class ScrollUp implements Action {

    @Override
    public Result execute(String... args) {
        TouchUtils.dragQuarterScreenDown(Actions.parentTestCase, InstrumentationBackend.solo.getCurrentActivity());
        TouchUtils.dragQuarterScreenDown(Actions.parentTestCase, InstrumentationBackend.solo.getCurrentActivity());
        TouchUtils.dragQuarterScreenDown(Actions.parentTestCase, InstrumentationBackend.solo.getCurrentActivity());
        TouchUtils.dragQuarterScreenDown(Actions.parentTestCase, InstrumentationBackend.solo.getCurrentActivity());

        return Result.successResult();
    }

    @Override
    public String key() {
        return "scroll_up";
    }

}
