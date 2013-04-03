package sh.calaba.instrumentationbackend.actions.wait;

import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;

public class WaitForScreen implements Action {
	private static final int DEFAULT_TIMEOUT = 5 * 1000;

    @Override
    public Result execute(String... args) {
        switch (args.length) {
        case 0:
            return new Result(false, "Cannot check for correct screen. No Activity name supplied!");
        case 1: {
            if (InstrumentationBackend.solo.waitForActivity(args[0], DEFAULT_TIMEOUT)) {
                return Result.successResult();
            } else {
            	String currentActivity = InstrumentationBackend.solo.getCurrentActivity().getClass().getSimpleName();
            	Result result = new Result(false, "Screen " + args[0] + " not found.  Current activity is " + currentActivity);
            	result.addBonusInformation(currentActivity);
            	return result;
            }
        }
        case 2: { // 1st arg is Activity name, 2nd arg is timeout
            int timeout;
            
            try {
                // given seconds; want milliseconds
                timeout = 1000 * Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                return new Result(false, "Invalid timeout supplied. Should be an integer!"); 
            }
            
            if (InstrumentationBackend.solo.waitForActivity(args[0], timeout)) {
                return Result.successResult();
            } else {
                String currentActivity = InstrumentationBackend.solo.getCurrentActivity().getClass().getSimpleName();
            	Result result = new Result(false, "Screen " + args[0] + " not found.  Current activity is " + currentActivity);
            	result.addBonusInformation(currentActivity);
            	return result;
            }
        }
        default:
            return new Result(false, "Too many argument supplied to wait_for_screen!");
        }
    }

    @Override
    public String key() {
        return "wait_for_screen";
    }

}
