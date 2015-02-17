package sh.calaba.instrumentationbackend.json;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import java.io.IOException;
import java.util.Set;

import sh.calaba.org.codehaus.jackson.JsonGenerator;
import sh.calaba.org.codehaus.jackson.JsonProcessingException;
import sh.calaba.org.codehaus.jackson.map.JsonSerializer;
import sh.calaba.org.codehaus.jackson.map.SerializerProvider;

public class IntentSerializer extends JsonSerializer<Intent> {

    @Override
    public void serialize(Intent intent, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
        jgen.writeStartObject();
        jgen.writeStringField("action", intent.getAction());
        jgen.writeStringField("data", intent.getDataString());
        jgen.writeNumberField("flags", intent.getFlags());
        jgen.writeStringField("package", intent.getPackage());
        jgen.writeStringField("type", intent.getType());
        jgen.writeObjectField("component", intent.getComponent());

        Bundle extras = intent.getExtras();

        jgen.writeFieldName("extras");

        if (extras == null) {
            jgen.writeNull();
        } else {
            jgen.writeStartObject();

            Set<String> keySet = extras.keySet();

            for (String key : keySet) {
                jgen.writeObjectField(key, extras.get(key));
            }

            jgen.writeEndObject();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            jgen.writeObjectField("clipData", intent.getClipData());
        }

        jgen.writeEndObject();
    }
}
