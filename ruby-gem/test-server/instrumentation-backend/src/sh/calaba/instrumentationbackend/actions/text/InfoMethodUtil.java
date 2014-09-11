package sh.calaba.instrumentationbackend.actions.text;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import sh.calaba.instrumentationbackend.InstrumentationBackend;

import java.lang.reflect.Field;

public class InfoMethodUtil {
    static View getServedView() {
        Context context = InstrumentationBackend.instrumentation.getTargetContext();

        try {
            InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            Field servedViewField = InputMethodManager.class.getDeclaredField("mServedView");
            servedViewField.setAccessible(true);

            return (View)servedViewField.get(inputMethodManager);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static InputConnection getInputConnection() {
        Context context = InstrumentationBackend.instrumentation.getTargetContext();

        try {
            InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            Field servedInputConnectionField = InputMethodManager.class.getDeclaredField("mServedInputConnection");
            servedInputConnectionField.setAccessible(true);

            return (InputConnection)servedInputConnectionField.get(inputMethodManager);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



}
