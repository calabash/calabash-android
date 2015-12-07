package sh.calaba.instrumentationbackend.actions.text;

import android.text.Editable;
import android.view.inputmethod.BaseInputConnection;
import android.view.inputmethod.InputConnection;
import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class SetSelection implements Action {

    private static final String SELECTION_END   = "SELECTION_END";
    private static final String USAGE           = "This action takes 2 arguments:\n([int] position, [int] length)";

    @Override
    public Result execute(final String... args) {
        if (args.length != 2) {
            return Result.failedResult(USAGE);
        } 

        final BaseInputConnection connection = getConnection();
        if (connection == null) {
            return Result.failedResult("Unable to set selection, no element has focus");
        }

        final Editable editable = connection.getEditable();
        if (editable == null) {
            return Result.failedResult("Unable to set selection, not editable");
        }

        InstrumentationBackend.solo.runOnMainSync(new Runnable() {
            @Override
            public void run() {
                int position, length;
                if (args[0].equals(SELECTION_END)) { position = editable.length(); }
                else { position = Integer.parseInt(args[0]); }

                if (args[1].equals(SELECTION_END)) { length = editable.length(); }
                else { length = Integer.parseInt(args[1]); }

                connection.setSelection(position, length);
            }
        });
        return Result.successResult();
    }

    private BaseInputConnection getConnection() {
        InputConnection inputConnection = InfoMethodUtil.getInputConnection();
        if (inputConnection instanceof BaseInputConnection) {
            return (BaseInputConnection) inputConnection;
        } else {
            System.out.println("InputConnection is not an instance of BaseInputConnection. (" + (inputConnection == null ? "null" : inputConnection.getClass().toString()) + ")");
            return null;
        }
    }

    @Override
    public String key() {
        return "set_selection";
    }
}
