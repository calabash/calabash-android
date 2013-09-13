package sh.calaba.instrumentationbackend.actions;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import sh.calaba.instrumentationbackend.InstrumentationBackend;
import android.app.Instrumentation;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.InstrumentationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.test.InstrumentationTestCase;

public class Actions {

    private Map<String, Action> actions = new HashMap<String, Action>();
    public static Instrumentation parentInstrumentation;
    public static InstrumentationTestCase parentTestCase;
    private Context context;

    public Actions(Instrumentation parentInstrumentation, InstrumentationTestCase parentTestCase) {
        Actions.parentInstrumentation = parentInstrumentation;
        Actions.parentTestCase = parentTestCase;
        this.context = parentInstrumentation.getContext();
        loadActions();
    }
    
    private void loadActions() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(context.getAssets().open("actions")));
            try {
                for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                    String name = line.trim();
                    if (name.length() > 0) addAction(name);
                }
            } finally {
                reader.close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("rawtypes")
    private void addAction(String className) throws Exception {
        Class action = Class.forName(className);
        if (isAction(action)) {
            put((Action) action.newInstance());
        }
    }

    @SuppressWarnings("rawtypes")
    private boolean isAction(Class actionCandidate) {
        boolean isImplementation = !actionCandidate.isInterface();
        return isImplementation && Action.class.isAssignableFrom(actionCandidate);
    }

    private void put(Action action) {
        if (getActions().containsKey(action.key())) {
            Action duplicate = getActions().get(action.key());
            throw new RuntimeException("Found duplicate action key:'" + action.key() + "'. [" + duplicate.getClass().getName() + "," + action.getClass().getName() + "]");
        }
        if (action.key() == null) {
            System.out.println("Skipping " + action.getClass() + ". Key is null.");
        } else {
            InstrumentationBackend.log("Added:'" + action.getClass().getSimpleName() + "', with key:'" + action.key() + "'");
            getActions().put(action.key(), action);
        }
    }

    public Action lookup(String key) {
        Action action = getActions().get(key);
        if (action == null) {
            action = new NullAction();
            ((NullAction) action).setMissingKey(key);
        }
        return action;
    }

    public Map<String, Action> getActions() {
        return actions;
    }
}
