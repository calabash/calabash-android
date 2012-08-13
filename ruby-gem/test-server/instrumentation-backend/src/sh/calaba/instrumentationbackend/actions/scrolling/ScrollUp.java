package sh.calaba.instrumentationbackend.actions.scrolling;


import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;


public class ScrollUp implements Action {

    @Override
    public Result execute(String... args) {
    	InstrumentationBackend.solo.scrollUp();
        return Result.successResult();
    }

    @Override
    public String key() {
        return "scroll_up";
    }

}
