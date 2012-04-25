package sh.calaba.instrumentationbackend.actions.text;


import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;


public class ClickOnText implements Action {

    @Override
    public Result execute(String... args) {
        InstrumentationBackend.solo.clickOnText(args[0]);
        return Result.successResult();
    }

    @Override
    public String key() {
        return "click_on_text";
    }

}
