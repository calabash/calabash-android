package sh.calaba.instrumentationbackend.actions.wait;

import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.TestHelpers;
import sh.calaba.instrumentationbackend.actions.Action;

/**
 * Example:
 *
 * {@code performAction('wait_for_fragment_by_id', 'my_fragment', '5')}
 *
 * Waits for the {@code R.id.my_fragment} fragment to be added to the Fragment Manager. If the Fragment is not added within 5 seconds, a timeout
 * occurs. Default timeout is 5 seconds.
 *
 * Supports both built-in ({@link android.app.Fragment}) and the compatibility/support library ({@link android.support.v4.app.Fragment}) fragments.
 *
 * @author Joe Hansche
 */
public class WaitForFragmentById implements Action {
    private static final int DEFAULT_TIMEOUT = 5 * 1000;

    @Override
    public Result execute(String... args) {
        int id;
        int timeout;

        switch (args.length) {
            case 0:
                return new Result(false, "Cannot check for fragment. No fragment id supplied!");

            case 1:
                id = TestHelpers.getIdFromString(args[0]);
                timeout = DEFAULT_TIMEOUT;
                break;

            case 2:
                // 1st arg is fragment id, 2nd arg is timeout
                id = TestHelpers.getIdFromString(args[0]);

                try {
                    // given seconds; want milliseconds
                    timeout = 1000 * Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    return new Result(false, "Invalid timeout supplied. Should be an integer!");
                }

                break;

            default:
                return new Result(false, "Too many argument supplied to wait_for_fragment_by_id!");
        }

        if (id == 0) {
            return new Result(false, "Unable to look up identifier by resource name: R.id." + args[0] + "!");
        }

        if (InstrumentationBackend.solo.waitForFragmentById(id, timeout)) {
            return Result.successResult();
        }

        return new Result(false, "Could not locate fragment by id " + args[0] + "!");
    }

    @Override
    public String key() {
        return "wait_for_fragment_by_id";
    }
}
