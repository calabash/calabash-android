package sh.calaba.instrumentationbackend.json;

import android.net.Uri;

import java.io.IOException;

import sh.calaba.instrumentationbackend.Logger;
import sh.calaba.org.codehaus.jackson.JsonGenerator;
import sh.calaba.org.codehaus.jackson.JsonProcessingException;
import sh.calaba.org.codehaus.jackson.map.JsonSerializer;
import sh.calaba.org.codehaus.jackson.map.SerializerProvider;

public class UriSerializer extends JsonSerializer<Uri> {
    @Override
    public void serialize(Uri value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
        jgen.writeString(value.toString());
    }
}
