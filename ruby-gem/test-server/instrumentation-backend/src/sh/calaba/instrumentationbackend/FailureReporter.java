package sh.calaba.instrumentationbackend;

import android.content.Context;
import android.content.Intent;

import java.io.PrintWriter;
import java.io.StringWriter;

public class FailureReporter {
    private Context context;
    private boolean hasReportedFailure;

    public FailureReporter(Context context) {
        this.context = context;
        this.hasReportedFailure= false;
    }

    public void reportFailure(String message)  {
        Intent intent = new Intent(context, FailureReporterActivity.class);
        intent.putExtra(FailureReporterActivity.EXTRA_METHOD, "report");
        intent.putExtra(FailureReporterActivity.EXTRA_MESSAGE, message);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        hasReportedFailure= true;
    }

    public void reportFailure(Throwable e) {
        StringWriter stringWriter = new StringWriter();
        stringWriter.write("Unknown error:\n");
        e.printStackTrace(new PrintWriter(stringWriter));

        reportFailure(stringWriter.toString());
    }

    public void clearFailure() {
        Intent intent = new Intent(context, FailureReporterActivity.class);
        intent.putExtra(FailureReporterActivity.EXTRA_METHOD, "clear");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public boolean hasReportedFailure() {
        return hasReportedFailure;
    }
}
