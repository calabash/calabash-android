package sh.calaba.instrumentationbackend.actions.gestures;

import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;

import com.jayway.android.robotium.solo.Solo;

public class Swipe implements Action {

    // TODO: add up/down swipe support via scroll action
    // !!!: iOS swipe supports offset, can't do it via scroll,
    // looks like using Drag for swipe might be an option
    // or, think of the following, if there's no offset and no query - then do simple scroll to side
    // but if there's a query, then use scroll to side with view arg
    // and add support for options that can include offset and scroll position
    // in other words, first implement "right" scroll then use it here

    @Override
    public Result execute(String... args) {
        String direction = args[0];

        if (args.length == 1) {
            if(direction.equalsIgnoreCase("left")) {
                InstrumentationBackend.solo.scrollToSide(Solo.LEFT);
                return Result.successResult();
            } else if(direction.equalsIgnoreCase("right")) {
                InstrumentationBackend.solo.scrollToSide(Solo.RIGHT);
                return Result.successResult();
            }
            return Result.failedResult("Invalid direction to swipe: " + direction);
        }
        return Result.failedResult("You must provide a direction. Either 'left' or 'right'");
    }

    @Override
    public String key() {
        return "swipe";
    }

}
