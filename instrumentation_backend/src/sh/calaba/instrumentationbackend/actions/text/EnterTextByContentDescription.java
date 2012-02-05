package sh.calaba.instrumentationbackend.actions.text;


import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.TestHelpers;
import sh.calaba.instrumentationbackend.actions.Action;
import android.view.View;
import android.widget.EditText;


public class EnterTextByContentDescription implements Action {

    @Override
    public Result execute(String... args) {
        View view = TestHelpers.getTextViewByDescription(args[1]);
        if(view == null) {
            return new Result(false, "No view found with content description: '" + args[1] + "'");
        } else if (!(view instanceof EditText)) {
            return new Result(false, "Expected EditText found: '" + view.getClass() + "'");
        } else {
            InstrumentationBackend.solo.enterText((EditText)view, args[0]);
            return Result.successResult();
        }
    }

    @Override
    public String key() {
        return "enter_text_into_named_field";
    }

}
