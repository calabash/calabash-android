package sh.calaba.instrumentationbackend.actions.gestures;

import android.content.Context;
import android.content.Intent;

import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.ResumeTaskActivity;
import sh.calaba.instrumentationbackend.actions.Action;

public class ResumeApplication implements Action {
    @Override
    public Result execute(String... args) {
        if (args.length != 1) {
            return Result.failedResult("This action takes one argument ([String] package)");
        }

        String packageName = args[0];
        Context context = InstrumentationBackend.instrumentation.getContext();

        Intent intent = new Intent(context, ResumeTaskActivity.class);
        intent.putExtra(ResumeTaskActivity.EXTRA_PACKAGE_NAME, packageName);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

        return Result.successResult();
    }

    @Override
    public String key() {
        return "resume_application";
    }
}
