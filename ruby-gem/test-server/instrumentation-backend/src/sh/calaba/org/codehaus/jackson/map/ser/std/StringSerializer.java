package sh.calaba.org.codehaus.jackson.map.ser.std;

import java.io.IOException;
import java.lang.reflect.Type;

import sh.calaba.org.codehaus.jackson.JsonGenerationException;
import sh.calaba.org.codehaus.jackson.JsonGenerator;
import sh.calaba.org.codehaus.jackson.JsonNode;
import sh.calaba.org.codehaus.jackson.map.SerializerProvider;
import sh.calaba.org.codehaus.jackson.map.annotate.JacksonStdImpl;

/**
 * This is the special serializer for regular {@link java.lang.String}s.
 *<p>
 * Since this is one of "native" types, no type information is ever
 * included on serialization (unlike for most scalar types as of 1.5)
 */
@JacksonStdImpl
public final class StringSerializer
    extends NonTypedScalarSerializerBase<String>
{
    public StringSerializer() { super(String.class); }

    @Override
    public void serialize(String value, JsonGenerator jgen, SerializerProvider provider)
        throws IOException, JsonGenerationException
    {
        jgen.writeString(value);
    }

    @Override
    public JsonNode getSchema(SerializerProvider provider, Type typeHint)
    {
        return createSchemaNode("string", true);
    }
}
