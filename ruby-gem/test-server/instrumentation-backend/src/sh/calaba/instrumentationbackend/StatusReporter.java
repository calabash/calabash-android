package sh.calaba.instrumentationbackend;

import android.content.Context;
import android.content.Intent;

import java.io.PrintWriter;
import java.io.StringWriter;

public class StatusReporter {
    private Context context;
    private boolean hasReportedFailure;

    public StatusReporter(Context context) {
        this.context = context;
        this.hasReportedFailure = false;
    }

    public void reportFailure(String message)  {
        Intent intent = new Intent(context, StatusReporterActivity.class);
        intent.putExtra(StatusReporterActivity.EXTRA_METHOD, "report-failure");
        intent.putExtra(StatusReporterActivity.EXTRA_MESSAGE, message);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        hasReportedFailure = true;
    }

    public void reportFailure(Throwable e) {
        StringWriter stringWriter = new StringWriter();
        stringWriter.write("Unknown error:\n");
        e.printStackTrace(new PrintWriter(stringWriter));

        reportFailure(stringWriter.toString());
    }

    public void clear() {
        Intent intent = new Intent(context, StatusReporterActivity.class);
        intent.putExtra(StatusReporterActivity.EXTRA_METHOD, "clear");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public enum FinishedState {SUCCESSFUL, NOT_SUCCESSFUL};

    public void reportFinished(FinishedState state)  {
        Intent intent = new Intent(context, StatusReporterActivity.class);
        intent.putExtra(StatusReporterActivity.EXTRA_METHOD, "report-finished");
        intent.putExtra(StatusReporterActivity.EXTRA_STATE, state);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public boolean hasReportedFailure() {
        return hasReportedFailure;
    }
}
