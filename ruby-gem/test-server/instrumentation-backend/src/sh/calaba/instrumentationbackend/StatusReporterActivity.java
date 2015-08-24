package sh.calaba.instrumentationbackend;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Bundle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

public class StatusReporterActivity extends Activity {
    public static final String EXTRA_MESSAGE = "message";
    public static final String EXTRA_METHOD = "method";
    public static final String EXTRA_STATE = "state";

    private static final String FAILURE_FILE_PATH = "calabash_failure.out";
    private static final String FINISHED_FILE_PATH = "calabash_finished.out";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        {
            ContextWrapper contextWrapper = new ContextWrapper(this);
            File failureFile = new File(contextWrapper.getFilesDir(), FAILURE_FILE_PATH);
            System.out.println("Failure file: "+ failureFile);
            File finishedFile = new File(contextWrapper.getFilesDir(), FINISHED_FILE_PATH);
            System.out.println("Finished file: "+ finishedFile);
        }

        if (getIntent() != null) {
            if (getIntent().getExtras() != null) {
                Bundle extras = getIntent().getExtras();

                String method = extras.getString(EXTRA_METHOD);

                System.out.println("method: " + method);

                if ("report-failure".equals(method)) {
                    String message = extras.getString(EXTRA_MESSAGE);

                    try {
                        reportFailure(message);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else if ("clear".equals(method)) {
                    clear();
                } else if ("report-finished".equals(method)) {
                    try {
                        reportFinished((StatusReporter.FinishedState) extras.get(EXTRA_STATE));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    throw new RuntimeException("No such method '" + method + "'");
                }
            } else {
                throw new RuntimeException("No extras given");
            }
        } else {
            throw new RuntimeException("No intent given");
        }

        finish();
    }

    private void reportFailure(String message) throws IOException {
        clearFailure();

        OutputStream fileOutputStream = openFileOutput(FAILURE_FILE_PATH, Context.MODE_WORLD_READABLE);

        try {
            fileOutputStream.write(message.getBytes());
        } finally {
            fileOutputStream.close();
        }
    }

    private void clear() {
        clearFailure();
        clearFinishedStatus();
    }

    private void clearFailure() {
        ContextWrapper contextWrapper = new ContextWrapper(this);
        new File(contextWrapper.getFilesDir(), FAILURE_FILE_PATH).delete();
    }

    private void clearFinishedStatus() {
        ContextWrapper contextWrapper = new ContextWrapper(this);
        new File(contextWrapper.getFilesDir(), FINISHED_FILE_PATH).delete();
    }

    private void reportFinished(StatusReporter.FinishedState finishedState) throws IOException {
        System.out.println("Finished state: " + finishedState.toString());

        OutputStream fileOutputStream = openFileOutput(FINISHED_FILE_PATH, Context.MODE_WORLD_READABLE);

        try {
            fileOutputStream.write(finishedState.toString().getBytes());
        } finally {
            fileOutputStream.close();
        }
    }
}
