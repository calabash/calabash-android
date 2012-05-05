package sh.calaba.instrumentationbackend.actions.contextmenu;


import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;


public class LongPressText implements Action {

    @Override
    public Result execute(String... args) {
        InstrumentationBackend.solo.clickLongOnText(args[0]);
        return Result.successResult();
    }

    @Override
    public String key() {
        return "press_long_on_text";
    }

}
