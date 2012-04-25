package sh.calaba.instrumentationbackend.actions.time;


import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.TestHelpers;
import sh.calaba.instrumentationbackend.actions.Action;


public class SetDateByIndex implements Action {

    @Override
    public Result execute(String... args) {
        int[] date = TestHelpers.parseDate(args[0]);
        InstrumentationBackend.solo.setDatePicker(Integer.parseInt(args[1]) - 1, date[2], date[1], date[0]);
        return Result.successResult();
    }

    @Override
    public String key() {
        return "set_date_with_index";
    }

}
