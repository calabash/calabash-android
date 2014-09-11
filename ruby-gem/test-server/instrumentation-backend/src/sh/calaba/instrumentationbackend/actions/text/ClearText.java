package sh.calaba.instrumentationbackend.actions.text;

import android.text.Editable;
import android.view.inputmethod.BaseInputConnection;
import android.view.inputmethod.InputConnection;
import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ClearText implements Action {
    @Override
    public Result execute(String... args) {
        final BaseInputConnection connection = getConnection();
        if(connection == null) {
            return Result.failedResult("Unable to clear text, no element has focus");
        }

        final Editable editable = connection.getEditable();
        if(editable == null) {
            return Result.failedResult("Unable to clear text, not editable");
        }

        final CountDownLatch latch = new CountDownLatch(1);
        InstrumentationBackend.solo.runOnMainSync(new Runnable() {
            @Override
            public void run() {
                connection.setSelection(0, editable.length());
                connection.commitText("", 0);
                latch.countDown();
            }
        });

        try {
            latch.await(10, TimeUnit.SECONDS);
            return Result.successResult();
        } catch (InterruptedException e) {
            Result result = Result.fromThrowable(e);
            result.setMessage("Timeout while trying to clear text");
            return result;
        }

    }

    private BaseInputConnection getConnection() {
        InputConnection inputConnection = InfoMethodUtil.getInputConnection();
        if (inputConnection instanceof BaseInputConnection) {
            return (BaseInputConnection) inputConnection;
        }
        return null;
    }

    @Override
    public String key() {
        return "clear_text";
    }
}
