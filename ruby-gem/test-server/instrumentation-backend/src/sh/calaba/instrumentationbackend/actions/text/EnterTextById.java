package sh.calaba.instrumentationbackend.actions.text;

import android.view.View;
import android.widget.EditText;
import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.TestHelpers;
import sh.calaba.instrumentationbackend.actions.Action;

public class EnterTextById implements Action {

	@Override
	public Result execute(String... args) {
        if(args[1] == null) {
            return Result.failedResult("Input text cannot be null");
        }
        View view = TestHelpers.getViewById(args[1]);
        if(view == null) {
            return new Result(false, "No view found with id: '" + args[1] + "'");
        } else if (!(view instanceof EditText)) {
            return new Result(false, "Expected EditText found: '" + view.getClass() + "'");
        } else {
            InstrumentationBackend.solo.enterText((EditText)view, args[0]);
            return Result.successResult();
        }
	}

	@Override
	public String key() {
		return "enter_text_into_id_field";
	}

}
