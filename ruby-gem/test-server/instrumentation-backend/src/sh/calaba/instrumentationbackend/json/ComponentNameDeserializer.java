package sh.calaba.instrumentationbackend.json;

import android.content.ComponentName;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import sh.calaba.org.codehaus.jackson.JsonParser;
import sh.calaba.org.codehaus.jackson.JsonProcessingException;
import sh.calaba.org.codehaus.jackson.map.DeserializationContext;
import sh.calaba.org.codehaus.jackson.map.JsonDeserializer;

public class ComponentNameDeserializer extends JsonDeserializer<ComponentName> {
    @Override
    public ComponentName deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        HashMap map = (HashMap) jp.readValueAs(Map.class);
        String className = (String) map.get("className");
        String packageName = (String) map.get("packageName");

        return new ComponentName(packageName, className);
    }
}
