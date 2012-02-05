package sh.calaba.instrumentationbackend.actions.list;


import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;


public class PressListItems implements Action {

    @Override
    public Result execute(String... args) {
        InstrumentationBackend.solo.clickInList(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        return Result.successResult();
    }

    @Override
    public String key() {
        return "press_list_item";
    }

}
