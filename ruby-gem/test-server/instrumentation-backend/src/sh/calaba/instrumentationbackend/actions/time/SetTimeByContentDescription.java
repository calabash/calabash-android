package sh.calaba.instrumentationbackend.actions.time;


import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.TestHelpers;
import sh.calaba.instrumentationbackend.actions.Action;
import android.view.View;
import android.widget.TimePicker;


public class SetTimeByContentDescription implements Action {

    @Override
    public Result execute(String... args) {
        int[] time = TestHelpers.parseTime(args[1]);
        String contentDescription = args[0];
        View view = TestHelpers.getViewByDescription(contentDescription);
        
        if(view != null && view instanceof TimePicker) {
            InstrumentationBackend.solo.setTimePicker((TimePicker)view, time[0], time[1]); 
        } else {
            return new Result(false, "Failed to find TimePicker with content description:'" + contentDescription + "'"); 
        }
        
        return Result.successResult();
    }

    @Override
    public String key() {
        return "set_time_with_description";
    }

}
