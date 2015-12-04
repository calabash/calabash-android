package sh.calaba.instrumentationbackend.actions.gestures;

import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;

import com.robotium.solo.Solo;

public class Swipe implements Action {

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
