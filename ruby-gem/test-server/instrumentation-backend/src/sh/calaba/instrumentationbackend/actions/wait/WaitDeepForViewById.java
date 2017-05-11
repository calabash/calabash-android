package sh.calaba.instrumentationbackend.actions.wait;


import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.TestHelpers;
import sh.calaba.instrumentationbackend.actions.Action;
import android.view.View;


public class WaitDeepForViewById implements Action {

    @Override
    public Result execute(String... args) {
        final View view = TestHelpers.getViewById(args[0]);

        if(view == null) {
          return new Result(false, "Could not find view with id: '" + args[0] + "'");
        } 

        if ( InstrumentationBackend.solo.waitForView(view) ) {
          return Result.successResult();
        } else {
          return new Result(false, "solo.waitForView() timed out waiting for view");
        }
    }

    @Override
    public String key() {
        return "wait_deep_for_view_by_id";
    }
}
