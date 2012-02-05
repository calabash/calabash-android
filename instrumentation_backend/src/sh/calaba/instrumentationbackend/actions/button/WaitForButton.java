package sh.calaba.instrumentationbackend.actions.button;


import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.TestHelpers;
import sh.calaba.instrumentationbackend.actions.Action;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;


public class WaitForButton implements Action {

    @Override
    public Result execute(String... args) {
        String text = args[0];
        long endTime = System.currentTimeMillis() + 60000;
        while (System.currentTimeMillis() < endTime) {
            if (InstrumentationBackend.solo.searchButton(text) || searchForButtonWithContentDescription(text)) {
                return Result.successResult();
            } else {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    return Result.fromThrowable(e);
                }
            }
        }
        return new Result(false, "Timed out while waiting for button with text or contentDescription:'" + text + "'");
    }

    @Override
    public String key() {
        return "wait_for_button";
    }

    private boolean searchForButtonWithContentDescription(String description) {
        View view = TestHelpers.getViewByDescription(description);
        if (view != null && (view instanceof Button || view instanceof ImageButton)) {
            return true;
        } else {
            return false;
        }
    }

}