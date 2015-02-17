package sh.calaba.instrumentationbackend.json;

import android.content.ComponentName;

import java.io.IOException;

import sh.calaba.org.codehaus.jackson.JsonGenerator;
import sh.calaba.org.codehaus.jackson.JsonProcessingException;
import sh.calaba.org.codehaus.jackson.map.JsonSerializer;
import sh.calaba.org.codehaus.jackson.map.SerializerProvider;

public class ComponentNameSerializer extends JsonSerializer<ComponentName> {
    @Override
    public void serialize(ComponentName componentName, JsonGenerator jgen, SerializerProvider provider)
            throws IOException, JsonProcessingException {
        jgen.writeStartObject();
        jgen.writeStringField("className", componentName.getClassName());
        jgen.writeStringField("packageName", componentName.getPackageName());
        jgen.writeEndObject();
    }
}
