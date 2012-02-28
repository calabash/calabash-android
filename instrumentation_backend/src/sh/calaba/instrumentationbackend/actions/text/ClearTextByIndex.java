package sh.calaba.instrumentationbackend.actions.text;


import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;


public class ClearTextByIndex implements Action {

    @Override
    public Result execute(String... args) {
        InstrumentationBackend.solo.clearEditText(Integer.parseInt(args[0]) - 1);
        return Result.successResult();
    }

    @Override
    public String key() {
        return "clear_numbered_field";
    }

}
