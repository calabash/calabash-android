package sh.calaba.instrumentationbackend.actions.text;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.lang.reflect.Field;

import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;

public class HideSoftKeyboard implements Action {
    @Override
    public Result execute(String... args) {
        Context context = InstrumentationBackend.instrumentation.getTargetContext();
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

        Activity activity = InstrumentationBackend.solo.getCurrentActivity();
        View view;

        view = InfoMethodUtil.getServedView();

        if (view == null) {
            view = activity.getCurrentFocus();
        }

        if (view == null) {
            view = new View(activity);
        }

        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);

        return Result.successResult();
    }

    @Override
    public String key() {
        return "hide_soft_keyboard";
    }
}