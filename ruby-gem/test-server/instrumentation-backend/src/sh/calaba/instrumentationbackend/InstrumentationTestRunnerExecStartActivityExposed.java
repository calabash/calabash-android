package sh.calaba.instrumentationbackend;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.UserHandle;
import android.test.InstrumentationTestRunner;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * As {@link android.app.Instrumentation} does not expose the execStartActivity
 * methods, we have to clone our instance as a new InstrumentationTestRunner to
 * be able to execute the default implementation.
 */
public class InstrumentationTestRunnerExecStartActivityExposed extends InstrumentationTestRunner {
        public void execStartActivities(Context who, IBinder contextThread,
                                    IBinder token, Activity target, Intent[] intents, Bundle options) {
        InstrumentationTestRunner instrumentationTestRunner = cloneAsInstrumentationTestRunner();

        try {
            Method methodExecStartActivity = instrumentationTestRunner.getClass().getMethod("execStartActivity",
                    Context.class, IBinder.class, IBinder.class, Activity.class, Intent[].class,
                    Bundle.class);

            methodExecStartActivity.invoke(instrumentationTestRunner, who,
                    contextThread, token, target, intents, options);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public void execStartActivitiesAsUser(Context who, IBinder contextThread,
                                          IBinder token, Activity target, Intent[] intents, Bundle options,
                                          int userId) {
        InstrumentationTestRunner instrumentationTestRunner = cloneAsInstrumentationTestRunner();

        try {
            Method methodExecStartActivity = instrumentationTestRunner.getClass().getMethod("execStartActivity",
                    Context.class, IBinder.class, IBinder.class, Activity.class, Intent[].class,
                    Bundle.class, int.class);

            methodExecStartActivity.invoke(instrumentationTestRunner, who,
                    contextThread, token, target, intents, options, userId);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public ActivityResult execStartActivity(
            Context who, IBinder contextThread, IBinder token, Fragment target,
            Intent intent, int requestCode, Bundle options) {
        Logger.info("3");
        InstrumentationTestRunner instrumentationTestRunner = cloneAsInstrumentationTestRunner();

        try {
            Method methodExecStartActivity = instrumentationTestRunner.getClass().getMethod("execStartActivity",
                    Context.class, IBinder.class, IBinder.class, Fragment.class, Intent.class,
                    int.class, Bundle.class);

            return (ActivityResult) methodExecStartActivity.invoke(instrumentationTestRunner, who,
                    contextThread, token, target, intent, requestCode, options);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /*
    TODO
      public void execStartActivityFromAppTask(
            Context who, IBinder contextThread, IAppTask appTask,
            Intent intent, Bundle options) {
     */

    public ActivityResult execStartActivity(
            Context who, IBinder contextThread, IBinder token, Activity target,
            Intent intent, int requestCode, Bundle options, UserHandle user) {
        InstrumentationTestRunner instrumentationTestRunner = cloneAsInstrumentationTestRunner();

        try {
            Method methodExecStartActivity = instrumentationTestRunner.getClass().getMethod("execStartActivity",
                    Context.class, IBinder.class, IBinder.class, Activity.class, Intent.class,
                    int.class, Bundle.class, UserHandle.class);

            return (ActivityResult) methodExecStartActivity.invoke(instrumentationTestRunner, who,
                    contextThread, token, target, intent, requestCode, options, user);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }




    public ActivityResult execStartActivity(
            Context who, IBinder contextThread, IBinder token, Activity target,
            Intent intent, int requestCode, Bundle options) {
        InstrumentationTestRunner instrumentationTestRunner = cloneAsInstrumentationTestRunner();

        try {
            Method methodExecStartActivity = instrumentationTestRunner.getClass().getMethod("execStartActivity",
                    Context.class, IBinder.class, IBinder.class, Activity.class, Intent.class, int.class, Bundle.class);

            return (ActivityResult) methodExecStartActivity.invoke(instrumentationTestRunner, who, contextThread, token, target,
                    intent, requestCode, options);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private InstrumentationTestRunner cloneAsInstrumentationTestRunner() {
        try {
            InstrumentationTestRunner instrumentationTestRunner = InstrumentationTestRunner.class.newInstance();
            setFields(instrumentationTestRunner, InstrumentationTestRunner.class);

            return instrumentationTestRunner;
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private void setFields(Object instance, Class<?> instanceClass) {
        Field[] fields = instanceClass.getDeclaredFields();

        for (Field field : fields) {
            if ((field.getModifiers() & Modifier.FINAL) == Modifier.FINAL) {
                continue;
            }

            boolean accessible = field.isAccessible();
            field.setAccessible(true);

            try {
                Field myField = getField(this.getClass(), field.getName());
                myField.setAccessible(true);
                field.set(instance, myField.get(this));
                field.setAccessible(accessible);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e);
            }
        }

        // Lollipop (ART) introduces private transient int java.lang.Object.shadow$_monitor_
        // and private transient java.lang.Class java.lang.Object.shadow$_klass_.
        // Avoid cloning these.
        if (instanceClass.getSuperclass() != Object.class) {
            setFields(instance, instanceClass.getSuperclass());
        }
    }

    /**
     * Helper method to retrieve a declared field on a given class or any of
     * its superclasses.

     * @param instanceClass The class owning the field
     * @param fieldName The name of the declared field
     *
     * @return Field with fieldName
     *
     * @throws java.lang.NoSuchFieldException if the field is not found
     */
    private Field getField(Class<?> instanceClass, final String fieldName)
            throws NoSuchFieldException {
        try {
            Field field = instanceClass.getDeclaredField(fieldName);

            return field;
        } catch (NoSuchFieldException e) {
            if (instanceClass != Object.class) {
                return getField(instanceClass.getSuperclass(), fieldName);
            } else {
                throw e;
            }
        }
    }
}
