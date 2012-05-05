package sh.calaba.instrumentationbackend.actions.text;


import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;


public class EnterTextByIndex implements Action {

    @Override
    public Result execute(String... args) {
        InstrumentationBackend.solo.enterText(Integer.parseInt(args[1]) - 1, args[0]);
        return Result.successResult();
    }

    @Override
    public String key() {
        return "enter_text_into_numbered_field";
    }

}
