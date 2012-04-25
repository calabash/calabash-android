package sh.calaba.instrumentationbackend.actions.contextmenu;


import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;


public class LongPressTextAndSelectFromMenuByIndex implements Action {

    @Override
    public Result execute(String... args) {
        InstrumentationBackend.solo.clickLongOnTextAndPress(args[0], Integer.parseInt(args[1]) - 1);
        return Result.successResult();
    }

    @Override
    public String key() {
        return "press_long_on_text_and_select_with_index";
    }

}
