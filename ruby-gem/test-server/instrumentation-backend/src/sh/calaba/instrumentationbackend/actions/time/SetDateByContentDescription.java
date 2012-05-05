package sh.calaba.instrumentationbackend.actions.time;


import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.TestHelpers;
import sh.calaba.instrumentationbackend.actions.Action;
import android.view.View;
import android.widget.DatePicker;


public class SetDateByContentDescription implements Action {

    @Override
    public Result execute(String... args) {
        String contentDescription = args[0];
        View view = TestHelpers.getViewByDescription(contentDescription);
        int[] date = TestHelpers.parseDate(args[1]);

        if(view != null && view instanceof DatePicker) {
            InstrumentationBackend.solo.setDatePicker((DatePicker)view, date[2], date[1], date[0]); 
        } else {
            return new Result(false, "Failed to find TimePicker with content description:'" + contentDescription + "'"); 
        }
        return Result.successResult();
    }

    @Override
    public String key() {
        return "set_date_with_description";
    }

}
