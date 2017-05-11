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
            return findMethod(currentActivity, args[0], argClasses).invoke(currentActivity, callArgs);
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

    private MethodWrapper findMethod(Activity activity, String name, Class[] argClasses) throws NoSuchMethodException, UnexpectedMethodReturnTypeException {
        Class<? extends Activity> activityClass = activity.getClass();
        Method method;
        boolean isArrayArgs;
        try {
            method = activityClass.getMethod(name, argClasses);
            isArrayArgs = false;
        } catch (NoSuchMethodException e) {
            method = activityClass.getMethod(name, new Class[]{String[].class});
            isArrayArgs = true;
        }

        ResultConverter resultConverter = new ResultConverterFactory().get(method.getReturnType());

        return new MethodWrapper(method, isArrayArgs, resultConverter);
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

    private interface ResultConverter {
        boolean checkReturnType(Class<?> returnType);
        Result getResult(Object invokeReturn);
    }

    private class VoidResultConverter implements ResultConverter {
        @Override
        public boolean checkReturnType(Class<?> returnType) {
            return returnType.getName().equals("void");
        }

        @Override
        public Result getResult(Object invokeReturn) {
            return Result.successResult();
        }
    }

    private class BooleanResultConverter implements ResultConverter {
        @Override
        public boolean checkReturnType(Class<?> returnType) {
            return returnType.getName().equals("boolean") || returnType.equals(Boolean.class);
        }

        @Override
        public Result getResult(Object invokeReturn) {
            if (invokeReturn instanceof Boolean) {
                Boolean isSuccess = (Boolean) invokeReturn;
                return new Result(isSuccess, isSuccess ? null : "True expected, got false");
            }
            return Result.failedResult("Boolean expected, got null");
        }
    }

    private class StringResultConverter implements ResultConverter {
        @Override
        public boolean checkReturnType(Class<?> returnType) {
            return returnType.equals(String.class);
        }

        @Override
        public Result getResult(Object invokeReturn) {
            if (invokeReturn instanceof String) {
                Result result = new Result(true);
                result.addBonusInformation((String) invokeReturn);
                return result;
            }
            return Result.failedResult("String expected, got null");
        }
    }

    private class ResultConverterFactory {
        private ResultConverter get(Class<?> returnType) throws UnexpectedMethodReturnTypeException {
            ResultConverter[] list = {
                new VoidResultConverter(),
                new BooleanResultConverter(),
                new StringResultConverter()
            };
            for (ResultConverter converter : list) {
                if (converter.checkReturnType(returnType)) {
                    return converter;
                }
            }
            throw new UnexpectedMethodReturnTypeException(returnType);
        }
    }

    private class MethodWrapper {
        private final Method method;
        private final boolean isStringArray;
        private final ResultConverter resultConverter;

        private MethodWrapper(Method method, boolean isStringArray, ResultConverter resultConverter) {
            this.method = method;
            this.isStringArray = isStringArray;
            this.resultConverter = resultConverter;
        }

        private Result invoke(Activity activity, String[] callArgs) throws InvocationTargetException, IllegalAccessException {
            return resultConverter.getResult(
                method.invoke(activity, isStringArray ? new Object[]{callArgs} : callArgs)
            );
        }
    }

    private class UnexpectedMethodReturnTypeException extends Exception {

        private final Class type;

        public UnexpectedMethodReturnTypeException(Class type) {
            this.type = type;
        }
    }
}
