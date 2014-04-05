package sh.calaba.instrumentationbackend.actions.text;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import java.lang.Character;
import java.lang.reflect.Field;

import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;

public class KeyboardEnterText implements Action {
    @Override
    public Result execute(String... args) {
        if (args.length != 1) {
            return Result.failedResult("This action takes one argument ([String] text).");
        }

        final String textToEnter = args[0];
        final char[] textToEnterCharArray = textToEnter.toCharArray();

        Activity currentActivity = InstrumentationBackend.solo.getCurrentActivity();
        final View view = currentActivity.getCurrentFocus();
        final InputConnection inputConnection;

        try {
            Context context = view.getContext();

            if (context == null) {
                context = currentActivity.getApplicationContext();
            }

            InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            Field servedInputConnectionField = InputMethodManager.class.getDeclaredField("mServedInputConnection");
            servedInputConnectionField.setAccessible(true);
            inputConnection = (InputConnection)servedInputConnectionField.get(inputMethodManager);
        } catch (NoSuchFieldException e) {
            return Result.failedResult(e.getMessage());
        } catch (IllegalAccessException e) {
            return Result.failedResult(e.getMessage());
        }

        InstrumentationBackend.solo.runOnMainSync(new Runnable() {
            @Override
            public void run() {
                for (char c : textToEnterCharArray) {
                    inputConnection.commitText(Character.toString(c), 0);
                }
            }
        });

        return new Result(true, "Entered text '" + textToEnter + "'");
    }

    @Override
    public String key() {
        return "keyboard_enter_text";
    }
}