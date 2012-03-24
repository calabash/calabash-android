package sh.calaba.instrumentationbackend.actions;

import java.util.Enumeration;
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
import dalvik.system.DexFile;

public class Actions {

    private Map<String, Action> actions = new HashMap<String, Action>();
    public static Instrumentation parentInstrumentation;
    public static InstrumentationTestCase parentTestCase;
    private Context context;
	private Context targetContext;

    public Actions(Instrumentation parentInstrumentation, InstrumentationTestCase parentTestCase) {
        Actions.parentInstrumentation = parentInstrumentation;
        Actions.parentTestCase = parentTestCase;
        this.context = parentInstrumentation.getContext();
        this.targetContext = parentInstrumentation.getTargetContext();
        loadActions();
    }
    
    private void loadActions() {
        try {
            DexFile dexFile = new DexFile(getApkLocation());
            Enumeration<String> entries = dexFile.entries();
            while (entries.hasMoreElements()) {
                String element = entries.nextElement();
                // Only look at classes from the actions package (and sub
                // packages)
                if (element.startsWith("sh.calaba.instrumentationbackend.actions.")) {
                    addAction(element);
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
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
    private boolean isAction(Class action) {
        for (Class i : action.getInterfaces()) {
            if (i.equals(Action.class)) {
                return true;
            }
        }
        return false;
    }

    private void put(Action action) {
        if (getActions().containsKey(action.key())) {
            Action duplicate = getActions().get(action.key());
            throw new RuntimeException("Found duplicate action key:'" + action.key() + "'. [" + duplicate.getClass().getName() + "," + action.getClass().getName() + "]");
        }
        InstrumentationBackend.log("Added:'" + action.getClass().getSimpleName() + "', with key:'" + action.key() + "'");
        getActions().put(action.key(), action);
    }

    public Action lookup(String key) {
        Action action = getActions().get(key);
        if (action == null) {
            action = new NullAction();
            ((NullAction) action).setMissingKey(key);
        }
        return action;
    }

    private String getApkLocation() {
        PackageManager pm = context.getPackageManager();
        InstrumentationInfo info = null;
        try {
            info = pm.getInstrumentationInfo(new ComponentName(context, parentInstrumentation.getClass()), 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return info != null ? info.sourceDir : null;
    }
    
    private String getTargetApkLocation() {
        PackageManager pm = targetContext.getPackageManager();
        InstrumentationInfo info = null;
        try {
            info = pm.getInstrumentationInfo(new ComponentName(targetContext, parentInstrumentation.getClass()), 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return info != null ? info.sourceDir : null;
    }


    public Map<String, Action> getActions() {
        return actions;
    }
}
