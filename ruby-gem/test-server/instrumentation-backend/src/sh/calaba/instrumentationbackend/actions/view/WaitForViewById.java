package sh.calaba.instrumentationbackend.actions.view;


import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.TestHelpers;
import sh.calaba.instrumentationbackend.actions.Action;
import android.view.View;


public class WaitForViewById implements Action {

    @Override
    public Result execute(String... args) {
        String viewId = args[0];
        System.out.println("Waiting for view with id '" + viewId + "'");
        long endTime = System.currentTimeMillis() + 60000;
        while (System.currentTimeMillis() < endTime) {
            View view = TestHelpers.getViewById(viewId);
            System.out.println("Waiting for view with id '" + viewId + "' found view " + view);

            if (view != null) {
                System.out.println("Waiting for view with id '" + viewId + "' Success");
                return Result.successResult();
            } else {
                try {
                    System.out.println("Waiting for view with id '" + viewId + "' sleeping...");
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    return Result.fromThrowable(e);
                }
            }
        }
        System.out.println("Waiting for view with id '" + viewId + "' Timed out");
        return new Result(false, "Timed out while waiting for view with id:'" + viewId + "'");
    }

    @Override
    public String key() {
        return "wait_for_view_by_id";
    }
}