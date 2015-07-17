package sh.calaba.instrumentationbackend;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Bundle;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

public class FailureReporterActivity extends Activity {
    public static final String EXTRA_MESSAGE = "message";
    public static final String EXTRA_METHOD = "method";

    private static final String FAILURE_FILE_PATH = "calabash_failure.out";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent() != null) {
            if (getIntent().getExtras() != null) {
                Bundle extras = getIntent().getExtras();

                String method = extras.getString(EXTRA_METHOD);

                if ("report".equals(method)) {
                    String message = extras.getString(EXTRA_MESSAGE);

                    try {
                        reportFailure(message);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else if ("clear".equals(method)) {
                    clearFailure();
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

        fileOutputStream.write(message.getBytes());

        fileOutputStream.close();
    }

    private void clearFailure() {
        ContextWrapper contextWrapper = new ContextWrapper(this);
        File file = new File(contextWrapper.getFilesDir(), FAILURE_FILE_PATH);
         file.delete();
    }
}
