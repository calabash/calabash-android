package sh.calaba.instrumentationbackend.json;

import android.content.Intent;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import sh.calaba.org.codehaus.jackson.JsonParser;
import sh.calaba.org.codehaus.jackson.JsonProcessingException;
import sh.calaba.org.codehaus.jackson.map.DeserializationContext;
import sh.calaba.org.codehaus.jackson.map.JsonDeserializer;

public class IntentDeserializer extends JsonDeserializer<Intent> {
    @Override
    public Intent deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        throw new UnsupportedOperationException();
    }

}
