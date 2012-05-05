package sh.calaba.instrumentationbackend.actions.wait;


import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;


public class WaitForDialogClose implements Action {

    @Override
    public Result execute(String... args) {
        InstrumentationBackend.solo.waitForDialogToClose(30000);
        return Result.successResult();
    }

    @Override
    public String key() {
        return "wait_for_dialog_to_close";
    }
}
