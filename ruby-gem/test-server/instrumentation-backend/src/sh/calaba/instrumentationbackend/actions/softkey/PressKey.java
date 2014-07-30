package sh.calaba.instrumentationbackend.actions.softkey;

import android.app.Instrumentation;
import android.view.KeyEvent;

import java.lang.reflect.Field;

import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;

public class PressKey implements Action {
    @Override
    public Result execute(String ... args) {
        if (args.length != 1) {
            return Result.failedResult("This action takes one argument ([String/int] key).");
        }

        String keyString = args[0];
        Integer keyCode = null;

        if (isNumber(keyString)) {
            keyCode = Integer.parseInt(keyString);
        } else {
            keyCode = getKey(keyString);
        }
        
        if (keyCode == null || keyCode < 0) {
            return Result.failedResult("Could not find key code from argument '" + keyString + "'");
        }

        Instrumentation instrumentation = InstrumentationBackend.instrumentation;
        Exception securityException = new SecurityException();

        for (int i = 0; i < 10; i++) { // Retry sending the key 10 times
            try {
                instrumentation.sendKeyDownUpSync(keyCode);

                return Result.successResult();
            } catch (SecurityException e) {
                securityException = e;

                // Sleep and try again
                try {
                    Thread.sleep(200);
                } catch (InterruptedException interruptedException) {

                }
            }
        }

        // The input event injection failed. This is most likely due to the application not having focus
        throw new SecurityException(securityException);
    }

    @Override
    public String key() {
        return "press_key";
    }

    public static Integer getKey(String keyName) {
        keyName = keyName.toUpperCase();

        Class<?> keyEventClass = KeyEvent.class;
        try {
            Field field = keyEventClass.getField(keyName);
            
            return field.getInt(null);
        } catch (NoSuchFieldException e) {
            return null;
        } catch (IllegalAccessException e) {
            return null;
        }
    }

    private static boolean isNumber(String string) {
        try {
            Integer.parseInt(string);

            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
