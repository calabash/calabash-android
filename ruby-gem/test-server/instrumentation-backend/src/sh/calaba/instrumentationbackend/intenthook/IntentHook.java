package sh.calaba.instrumentationbackend.intenthook;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class IntentHook implements IIntentHook {
    protected Object activityManagerNative;
    protected Class<?> activityManagerNativeClass;

    public IntentHook() {
        try {
            activityManagerNativeClass = Class.forName("android.app.ActivityManagerNative");
            // ActivityManagerNative.getDefault();
            Method methodGetDefault = activityManagerNativeClass.getMethod("getDefault");
            activityManagerNative = methodGetDefault.invoke(activityManagerNativeClass);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
