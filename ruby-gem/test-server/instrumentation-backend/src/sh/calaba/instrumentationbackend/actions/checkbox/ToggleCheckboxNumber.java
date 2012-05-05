package sh.calaba.instrumentationbackend.actions.checkbox;


import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;


public class ToggleCheckboxNumber implements Action {

    @Override
    public Result execute(String... args) {
        InstrumentationBackend.solo.clickOnCheckBox(Integer.parseInt(args[0]) - 1);
        return Result.successResult();
    }

    @Override
    public String key() {
        return "toggle_numbered_checkbox";
    }

}
