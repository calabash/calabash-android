package sh.calaba.instrumentationbackend.actions.text;


import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.TestHelpers;
import sh.calaba.instrumentationbackend.actions.Action;
import android.view.View;
import android.widget.EditText;


public class ClearTextFieldByContentDescription implements Action {

    @Override
    public Result execute(String... args) {
        String contentDescription = args[0];
        View view = TestHelpers.getTextViewByDescription(contentDescription);
        if(view == null) {
            return new Result(false, "No view found with content description: '" + contentDescription + "'");
        } else if (!(view instanceof EditText)) {
            return new Result(false, "Expected EditText found: '" + view.getClass() + "'");
        } else {
            InstrumentationBackend.solo.clearEditText((EditText)view);
            return Result.successResult();
        }
    }

    @Override
    public String key() {
        return "clear_named_field";
    }

}
