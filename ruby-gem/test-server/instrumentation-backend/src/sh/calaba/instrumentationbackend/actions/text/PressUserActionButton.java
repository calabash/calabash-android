package sh.calaba.instrumentationbackend.actions.text;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;

import java.lang.Integer;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.HashMap;

import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;

public class PressUserActionButton implements Action {
    private static Map<String,Integer> actionCodeMap;

    static {
        actionCodeMap = new HashMap<String, Integer>();
        actionCodeMap.put("normal", 0);
        actionCodeMap.put("unspecified", 0);
        actionCodeMap.put("none", 1);
        actionCodeMap.put("go", 2);
        actionCodeMap.put("search", 3);
        actionCodeMap.put("send", 4);
        actionCodeMap.put("next", 5);
        actionCodeMap.put("done", 6);
        actionCodeMap.put("previous", 7);
    }

    @Override
    public Result execute(String... args) {
        if (args.length > 1) {
            return Result.failedResult("This action takes zero arguments or one argument ([String] action name)");
        }

        Activity currentActivity = InstrumentationBackend.solo.getCurrentActivity();
        final View view = currentActivity.getCurrentFocus();
        final InputConnection inputConnection = getInputConnection();
        final int imeActionCode;

        if (inputConnection == null) {
            return Result.failedResult("Could not press user action button. No element has focus.");
        }

        if (args.length >= 1) {
            try {
                imeActionCode = findActionCode(args[0]);
            } catch (IllegalArgumentException e) {
                throw new RuntimeException(e);
            }
        } else {
            imeActionCode = findActionCode(view);
        }

        InstrumentationBackend.solo.runOnMainSync(new Runnable() {
            @Override
            public void run() {
                inputConnection.performEditorAction(imeActionCode);
            }
        });

        return Result.successResult();
    }

    @Override
    public String key() {
        return "press_user_action_button";
    }

    InputConnection getInputConnection() {
        Context context = InstrumentationBackend.instrumentation.getTargetContext();

        try {
            InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            Field servedInputConnectionField = InputMethodManager.class.getDeclaredField("mServedInputConnection");
            servedInputConnectionField.setAccessible(true);

            return (InputConnection)servedInputConnectionField.get(inputMethodManager);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private int findActionCode(View view) {
        EditorInfo editorInfo = new EditorInfo();
        view.onCreateInputConnection(editorInfo);

        int actionId = editorInfo.actionId;
        int imeOptions = editorInfo.imeOptions & EditorInfo.IME_MASK_ACTION;

        if (actionId > 0) {
            return actionId;
        } else if (imeOptions > 0) {
            return imeOptions;
        } else {
            return findActionCode("done");
        }
    }

    private int findActionCode(String actionName) throws IllegalArgumentException {
        actionName = actionName.toLowerCase();

        try {
            return actionCodeMap.get(actionName);
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("Action name '" + actionName + "' invalid");
        }
    }
}