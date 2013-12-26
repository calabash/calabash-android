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
            return Result.failedResult("Expects at least one argument");
        }
        String[] callArgs = new String[args.length - 1];
        Class[] argClasses = new Class[args.length - 1];
        fillCallArgs(callArgs, argClasses, args);

        Activity currentActivity = InstrumentationBackend.solo.getCurrentActivity();
        try {
            MethodWrapper methodWrapper = getMethod(currentActivity, args[0], argClasses);
            Object result = methodWrapper.method.invoke(currentActivity, methodWrapper.isStringArray ? new Object[]{callArgs} : callArgs);
            return processMethodReturn(methodWrapper.method, result);
        } catch (NoSuchMethodException e) {
            return getFailResult("Activity %s does not contain method %s", currentActivity, argClasses, args[0]);
        } catch (IllegalAccessException e) {
            return getFailResult("No permission for method %2$s in Activity %1$s", currentActivity, argClasses, args[0]);
        } catch (UnexpectedMethodReturnTypeException e) {
            return getFailResult("Unexpected return type %3$s in Activity %1$s method %2$s", currentActivity, argClasses, args[0], e.type.getName());
        } catch (InvocationTargetException e) {
            return Result.fromThrowable(e.getTargetException());
        }
    }

    @Override
    public String key() {
        return "call_activity_method";
    }

    private void fillCallArgs(String[] callArgs, Class[] argClasses, String[] args) {
        for (int i = 1; i < args.length; i++) {
            callArgs[i - 1] = args[i];
            argClasses[i - 1] = args[i].getClass();
        }
    }

    private MethodWrapper getMethod(Activity activity, String name, Class[] argClasses) throws NoSuchMethodException {
        Class<? extends Activity> activityClass = activity.getClass();
        try {
            return new MethodWrapper(activityClass.getMethod(name, argClasses), false);
        } catch (NoSuchMethodException e) { /* ok */ }
        try {
            return new MethodWrapper(activityClass.getMethod(name, new Class[]{String[].class}), true);
        } catch (NoSuchMethodException e) { /* ok */ }

        throw new NoSuchMethodException();
    }

    private Result processMethodReturn(Method method, Object invokeReturn) throws UnexpectedMethodReturnTypeException {
        Class<?> returnType = method.getReturnType();
        if (returnType.getName().equals("void")) {
            return Result.successResult();
        } else if (returnType.getName().equals("boolean") || returnType.equals(Boolean.class)) {
            if (invokeReturn instanceof Boolean) {
                Boolean isSuccess = (Boolean) invokeReturn;
                return new Result(isSuccess, isSuccess ? null : "True expected, got false");
            }
            return Result.failedResult("Boolean expected, got null");
        } else if (returnType.equals(String.class)) {
            if (invokeReturn instanceof String) {
                Result result = new Result(true);
                result.addBonusInformation((String) invokeReturn);
                return result;
            }
            return Result.failedResult("String expected, got null");
        } else {
            throw new UnexpectedMethodReturnTypeException(returnType);
        }
    }

    private Result getFailResult(String msg, Activity currentActivity, Class[] argClasses, String methodName, Object... others) {
        Object[] formatArgs = new Object[others.length + 2];
        formatArgs[0] = currentActivity.getClass().toString();
        formatArgs[1] = getFullMethodName(methodName, argClasses);
        System.arraycopy(others, 0, formatArgs, 2, others.length);
        return Result.failedResult(
            String.format(msg, formatArgs)
        );
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

    private class MethodWrapper {
        private final Method method;
        private final boolean isStringArray;

        private MethodWrapper(Method method, boolean isStringArray) {
            this.method = method;
            this.isStringArray = isStringArray;
        }
    }

    private class UnexpectedMethodReturnTypeException extends Exception {

        private final Class type;

        public UnexpectedMethodReturnTypeException(Class type) {
            this.type = type;
        }
    }
}
