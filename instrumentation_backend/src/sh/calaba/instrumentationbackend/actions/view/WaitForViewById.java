package sh.calaba.instrumentationbackend.actions.view;


import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.TestHelpers;
import sh.calaba.instrumentationbackend.actions.Action;
import android.view.View;


public class WaitForViewById implements Action {

    @Override
    public Result execute(String... args) {
        String viewId = args[0];
        long endTime = System.currentTimeMillis() + 60000;
        while (System.currentTimeMillis() < endTime) {
            final View view = TestHelpers.getViewById(args[0]);

            if (view != null) {
                return Result.successResult();
            } else {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    return Result.fromThrowable(e);
                }
            }
        }
        return new Result(false, "Timed out while waiting for view with id:'" + viewId + "'");
    }

    @Override
    public String key() {
        return "wait_for_view_by_id";
    }
}