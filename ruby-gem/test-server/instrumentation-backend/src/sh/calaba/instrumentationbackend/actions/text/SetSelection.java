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

    private static final String SELECTION_START = "SELECTION_START";
    private static final String SELECTION_END   = "SELECTION_END";
    private static final String SELECTION_ALL   = "SELECTION_ALL";

    private static final String USAGE           = "This action takes 1 or 2 arguments:\n"
                                                    + "(\"SELECTION_START\") |  (\"SELECTION_END\") | (\"SELECTION_ALL\") |\n" 
                                                    + " ([int] position) | ([int] position, [int] length)";

    @Override
    public Result execute(final String... args) {
        if (args.length == 0 || args.length > 2) {
            return Result.failedResult(USAGE);
        } 

        final BaseInputConnection connection = getConnection();
        if(connection == null) {
            return Result.failedResult("Unable to set selection, no element has focus");
        }

        final Editable editable = connection.getEditable();
        if(editable == null) {
            return Result.failedResult("Unable to set selection, not editable");
        }

        final int textLength = editable.length();

        final CountDownLatch latch = new CountDownLatch(1);
        InstrumentationBackend.solo.runOnMainSync(new Runnable() {
            @Override
            public void run() {
                if (args.length == 1) {
                    String arg1 = args[0];
                    if (arg1.equals(SELECTION_START)) { selectStart(connection); }
                    else if (arg1.equals(SELECTION_END)) { selectEnd(connection, textLength); }
                    else if (arg1.equals(SELECTION_ALL)) { selectAll(connection, textLength); }
                    else { setSelection(connection, Integer.parseInt(arg1)); }
                } else {
                    setSelection(connection, Integer.parseInt(args[0]), Integer.parseInt(args[1]));
                }
                latch.countDown();
            }
        });

        try {
            latch.await(10, TimeUnit.SECONDS);
            return Result.successResult();
        } catch (InterruptedException e) {
            Result result = Result.fromThrowable(e);
            result.setMessage("Timeout while trying to set selection");
            return result;
        }
    }

    private void setSelection(BaseInputConnection connection, int position) {
        connection.setSelection(position, position);
    }

    private void setSelection(BaseInputConnection connection, int position, int length) {
        connection.setSelection(position, length);
    }

    private void selectAll(BaseInputConnection connection, int textLength) {
        connection.setSelection(0, textLength);
    }

    private void selectStart(BaseInputConnection connection) {
        setSelection(connection, 0);
    }

    private void selectEnd(BaseInputConnection connection, int textLength) {
        setSelection(connection, textLength);
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
