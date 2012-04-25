package sh.calaba.instrumentationbackend.actions.time;


import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.TestHelpers;
import sh.calaba.instrumentationbackend.actions.Action;


public class SetTimeByIndex implements Action {

    @Override
    public Result execute(String... args) {
        int[] time = TestHelpers.parseTime(args[0]);
        
        InstrumentationBackend.solo.setTimePicker(Integer.parseInt(args[1]) - 1, time[0], time[1]); 
        
        return Result.successResult();
    }

    @Override
    public String key() {
        return "set_time_with_index";
    }

}
