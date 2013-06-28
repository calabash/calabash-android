package sh.calaba.instrumentationbackend.actions.wait;

import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;

/**
 * Example:
 *
 * {@code performAction('wait_for_fragment_by_tag', 'my_fragment_tag', '5')}
 *
 * Waits for the fragment to be added to the Fragment Manager with the indicated {@code tag}. If the Fragment is not seen within the timeout, a a
 * failure result is returned. Default timeout is 5 seconds.
 *
 * @author Joe Hansche
 */
public class WaitForFragmentByTag implements Action {
    private static final int DEFAULT_TIMEOUT = 5 * 1000;

    @Override
    public Result execute(String... args) {
        switch (args.length) {
            case 0:
                return new Result(false, "Cannot check for fragment. No fragment tag supplied!");

            case 1:
                return waitForFragmentByTag(args[0], DEFAULT_TIMEOUT);

            case 2:
                // 1st arg is fragment tag, 2nd arg is timeout
                int timeout;

                try {
                    // given seconds; want milliseconds
                    timeout = 1000 * Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    return new Result(false, "Invalid timeout supplied. Should be an integer!");
                }

                return waitForFragmentByTag(args[0], timeout);

            default:
                return new Result(false, "Too many argument supplied to wait_for_fragment_by_tag!");
        }
    }

    private Result waitForFragmentByTag(String tag, int timeout) {
        if (InstrumentationBackend.solo.waitForFragmentByTag(tag, timeout)) {
            return Result.successResult();
        } else {
            Result result = new Result(false, "Fragment with tag \"" + tag + "\" not found.");
            return result;
        }
    }

    @Override
    public String key() {
        return "wait_for_fragment_by_tag";
    }
}
