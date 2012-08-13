package sh.calaba.instrumentationbackend.actions.wait;


import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;


public class WaitForText implements Action {

    @Override
    public Result execute(String... args) {
        
        int timeout = 90 * 1000;
        if (args.length > 1) { // a second argument is a timeout
            try {
                // the argument is in seconds but robotium takes milliseconds
                timeout = 1000 * Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                return new Result(false, "Invalid timeout supplied. Should be an integer."); 
            }
        }
 
        boolean timedOut = !InstrumentationBackend.solo.waitForText(args[0], 1, timeout);
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
