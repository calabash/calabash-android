package sh.calaba.instrumentationbackend.actions.wait;


import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;


public class WaitForText implements Action {

    @Override
    public Result execute(String... args) {
        boolean timedOut = !InstrumentationBackend.solo.waitForText(args[0], 1, 90000);
        if(timedOut) {
            return new Result(false, "Time out while waiting for text:" + args[0]);
        } else {
            return Result.successResult();
        }
            
    }

    @Override
    public String key() {
        return "wait_for_text";
    }
}
