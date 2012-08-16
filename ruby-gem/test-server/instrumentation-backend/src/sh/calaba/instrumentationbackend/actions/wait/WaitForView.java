package sh.calaba.instrumentationbackend.actions.wait;

import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.TestHelpers;
import sh.calaba.instrumentationbackend.actions.Action;
import android.view.View;

public class WaitForView implements Action {

    @Override
    public Result execute(String... args) {
        String text = args[0];
        long endTime = System.currentTimeMillis() + 60000;
        while (System.currentTimeMillis() < endTime) {
            if (InstrumentationBackend.solo.searchButton(text) || searchForViewWithContentDescription(text)) {
                return Result.successResult();
            } else {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    return Result.fromThrowable(e);
                }
            }
        }
        return new Result(false, "Timed out while waiting for view with text or contentDescription:'" + text + "'");
    }

    @Override
    public String key() {
        return "wait_for_view";
    }

    private boolean searchForViewWithContentDescription(String description) {
        View view = TestHelpers.getViewByDescription(description);
        if (view != null) {
            return true;
        } else {
            return false;
        }
    }

}