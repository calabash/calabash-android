package sh.calaba.instrumentationbackend.actions.list;


import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;


public class TimedLongPressListItems implements Action {

    @Override
    public Result execute(String... args) {
				InstrumentationBackend.solo.clickLongInList(Integer.parseInt(args[0]) - 1, Integer.parseInt(args[1]), Integer.parseInt(args[2]));
        return Result.successResult();
    }

    @Override
    public String key() {
        return "timed_long_press_list_item";
    }

}
