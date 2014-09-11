package sh.calaba.instrumentationbackend.actions.text;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;

import java.lang.Integer;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.HashMap;

import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;

public class PressUserActionButton implements Action {
    private static Map<String,Integer> actionCodeMap;

    static {
        actionCodeMap = new HashMap<String, Integer>();
        actionCodeMap.put("normal", EditorInfo.IME_ACTION_UNSPECIFIED);
        actionCodeMap.put("unspecified", EditorInfo.IME_ACTION_UNSPECIFIED);
        actionCodeMap.put("none", EditorInfo.IME_ACTION_NONE);
        actionCodeMap.put("go", EditorInfo.IME_ACTION_GO);
        actionCodeMap.put("search", EditorInfo.IME_ACTION_SEARCH);
        actionCodeMap.put("send", EditorInfo.IME_ACTION_SEND);
        actionCodeMap.put("next", EditorInfo.IME_ACTION_NEXT);
        actionCodeMap.put("done", EditorInfo.IME_ACTION_DONE);
        actionCodeMap.put("previous", EditorInfo.IME_ACTION_PREVIOUS);
    }

    @Override
    public Result execute(String... args) {
        if (args.length > 1) {
            return Result.failedResult("This action takes zero arguments or one argument ([String] action name)");
        }

        final View view = InfoMethodUtil.getServedView();
        final InputConnection inputConnection = InfoMethodUtil.getInputConnection();
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