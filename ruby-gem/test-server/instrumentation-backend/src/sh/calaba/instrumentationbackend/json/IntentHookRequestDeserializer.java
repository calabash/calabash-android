package sh.calaba.instrumentationbackend.json;

import android.content.ComponentName;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import sh.calaba.instrumentationbackend.intenthook.ActivityIntentFilter;
import sh.calaba.instrumentationbackend.intenthook.InstrumentationHook;
import sh.calaba.instrumentationbackend.intenthook.IntentHook;
import sh.calaba.instrumentationbackend.json.requests.IntentHookRequest;
import sh.calaba.org.codehaus.jackson.JsonParser;
import sh.calaba.org.codehaus.jackson.JsonProcessingException;
import sh.calaba.org.codehaus.jackson.map.DeserializationContext;
import sh.calaba.org.codehaus.jackson.map.JsonDeserializer;
import sh.calaba.org.codehaus.jackson.map.ObjectMapper;

public class IntentHookRequestDeserializer extends JsonDeserializer<IntentHookRequest> {
    @Override
    public IntentHookRequest deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        Map json = jp.readValueAs(HashMap.class);
        String type = (String) json.get("type");
        int usageCount = (Integer) json.get("usageCount");
        Map data = (Map) json.get("data");

        Map intentFilterData = (Map) json.get("intentFilterData");
        Map componentMap = (Map) intentFilterData.get("component");
        ComponentName componentName = null;

        if (componentMap != null) {
            componentName = new ComponentName((String) componentMap.get("packageName"),
                    (String) componentMap.get("className"));
        }

        ActivityIntentFilter activityIntentFilter =
                new ActivityIntentFilter((String)intentFilterData.get("action"), componentName);

        return new IntentHookRequest(usageCount, type, data, activityIntentFilter);
    }
}
