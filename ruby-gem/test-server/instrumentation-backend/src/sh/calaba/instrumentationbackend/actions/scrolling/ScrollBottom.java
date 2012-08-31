package sh.calaba.instrumentationbackend.actions.scrolling;


import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;


public class ScrollBottom implements Action {

    @Override
    public Result execute(String... args) {
        while(InstrumentationBackend.solo.scrollDown());
        return Result.successResult();
    }

    @Override
    public String key() {
        return "scroll_bottom";
    }

}
