package sh.calaba.instrumentationbackend.json.requests;

import java.util.Map;

import sh.calaba.instrumentationbackend.intenthook.ActivityIntentFilter;

public class IntentHookRequest {
    private String type;
    private Map<String, ?> data;
    private ActivityIntentFilter intentFilterData;

    public IntentHookRequest(String type, Map<String, ?> data, ActivityIntentFilter intentFilterData) {
        this.type = type;
        this.data = data;
        this.intentFilterData = intentFilterData;
    }

    public String getType() {
        return type;
    }

    public Map<String, ?> getData() {
        return data;
    }

    public ActivityIntentFilter getIntentFilterData() {
        return intentFilterData;
    }
}

// This would probably be a better solution

/*
public class IntentHookRequest {
    private String type;
    private Data data;
    private ActivityIntentFilter intentFilterData;

    @JsonCreator
    public static ActivityIntentFilter createActivityIntentFilter(@JsonProperty("action") String action,
                                                                  @JsonProperty("component") ComponentName componentName) {
        return new ActivityIntentFilter(action, componentName);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public ActivityIntentFilter getIntentFilterData() {
        return intentFilterData;
    }

    public void setIntentFilterData(ActivityIntentFilter intentFilterData) {
        this.intentFilterData = intentFilterData;
    }

    private class Data {
        private ComponentName component;
        private Integer testServerPort;
        private String targetPackage;
        private String clazz;
        private String mainActivity;

        public Data() {

        }

        public ComponentName getComponent() {
            return component;
        }

        public void setComponent(ComponentName component) {
            this.component = component;
        }

        public int getTestServerPort() {
            return testServerPort;
        }

        public void setTestServerPort(int testServerPort) {
            this.testServerPort = testServerPort;
        }

        public String getTargetPackage() {
            return targetPackage;
        }

        public void setTargetPackage(String targetPackage) {
            this.targetPackage = targetPackage;
        }

        @JsonProperty("class")
        public String getClazz() {
            return clazz;
        }

        @JsonProperty("class")
        public void setClazz(String clazz) {
            this.clazz = clazz;
        }

        public String getMainActivity() {
            return mainActivity;
        }

        public void setMainActivity(String mainActivity) {
            this.mainActivity = mainActivity;
        }
    }
}
*/
