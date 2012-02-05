package sh.calaba.instrumentationbackend.actions.softkey;


import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;


public class PressMenu implements Action {

    @Override
    public Result execute(String... args) {
        InstrumentationBackend.solo.clickOnMenuItem(args[0]);
        return Result.successResult();
    }

    @Override
    public String key() {
        return "select_from_menu";
    }

}
