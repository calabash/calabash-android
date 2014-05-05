package sh.calaba.instrumentationbackend.actions.text;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;

public class EnterText implements Action {
    @Override
    public Result execute(String... args) {
        if (args.length != 1) {
            return Result.failedResult("This action takes one argument");
        }

        Activity currentActivity = InstrumentationBackend.solo.getCurrentActivity();
        final View view = currentActivity.getCurrentFocus();

        if (!(view != null && view instanceof TextView)) {
            return Result.failedResult("The focused view must be an instance of TextView");
        }

        String textToEnter = args[0];

        InstrumentationBackend.solo.enterText(textToEnter);

        return Result.successResult();
    }

    @Override
    public String key() {
        return "enter_text";
    }
}