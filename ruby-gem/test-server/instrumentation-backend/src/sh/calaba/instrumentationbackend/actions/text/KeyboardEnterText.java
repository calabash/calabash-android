package sh.calaba.instrumentationbackend.actions.text;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;

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

        final InputConnection inputConnection = InfoMethodUtil.tryGetInputConnection();

        if (inputConnection == null) {
            return Result.failedResult("Could not enter text. No element has focus.");
        }

        final String textToEnter = args[0];
        InstrumentationBackend.solo.runOnMainSync(new Runnable() {
            @Override
            public void run() {
                for (char c : textToEnter.toCharArray()) {
                    inputConnection.commitText(Character.toString(c), 0);
                }
            }
        });

        return Result.successResult();
    }

    @Override
    public String key() {
        return "keyboard_enter_text";
    }
}
