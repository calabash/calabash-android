package sh.calaba.instrumentationbackend.actions.activity;

import android.app.Activity;
import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CallActivityMethod implements Action {

    @Override
    public Result execute(String... args) {
        if (args.length == 0) {
            return Result.failedResult("expects at least one argument");
        }
        String[] callArgs = new String[args.length - 1];
        Class[] argClasses = new Class[args.length - 1];
        for (int i = 1; i < args.length; i++) {
            callArgs[i - 1] = args[i];
            argClasses[i - 1] = args[i].getClass();
        }

        Activity currentActivity = InstrumentationBackend.solo.getCurrentActivity();
        try {
            Method method = currentActivity.getClass().getMethod(args[0], argClasses);
            method.invoke(currentActivity, callArgs);
        } catch (NoSuchMethodException e) {
            return Result.failedResult(
                    String.format(
                        "Activity %s does not contain expected method %s",
                        currentActivity.getClass().toString(),
                        getFullMethodName(args[0], argClasses)
                    )
                );
        } catch (IllegalAccessException e) {
            return Result.failedResult(
                String.format(
                    "No permission to method %2$s in Activity %1$s",
                    currentActivity.getClass().toString(),
                    getFullMethodName(args[0], argClasses)
                )
            );
        } catch (InvocationTargetException e) {
            return Result.fromThrowable(e.getTargetException());
        }

        return Result.successResult();
    }

    @Override
    public String key() {
        return "call_activity_method";
    }

    private String getFullMethodName(String name, Class[] args) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(name);
        if (args.length > 0) {
            stringBuilder.append("(");
            for (int i = 0; i < args.length; i++) {
                if (i != 0) {
                    stringBuilder.append(", ");
                }
                stringBuilder.append(args[i].getCanonicalName());
            }
            stringBuilder.append(")");
        }
        return stringBuilder.toString();
    }
}
