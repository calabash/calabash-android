package sh.calaba.instrumentationbackend.actions.gestures;

import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;

import com.jayway.android.robotium.solo.Solo;

public class Swipe implements Action {

    @Override
    public Result execute(String... args) {
        String direction = args[0];
        if(direction.equalsIgnoreCase("left")) {
            InstrumentationBackend.solo.scrollToSide(Solo.LEFT);
        } else {
            InstrumentationBackend.solo.scrollToSide(Solo.RIGHT);

        }
        return Result.successResult();
    }

    @Override
    public String key() {
        return "swipe";
    }

}
