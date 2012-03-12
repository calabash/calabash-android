package sh.calaba.org.codehaus.jackson.map.deser;

import sh.calaba.org.codehaus.jackson.map.*;
import sh.calaba.org.codehaus.jackson.map.type.ArrayType;

/**
 * @deprecated Since 1.9, use {@link sh.calaba.org.codehaus.jackson.map.deser.std.ObjectArrayDeserializer} instead.
 */
@Deprecated
public class ArrayDeserializer
    extends sh.calaba.org.codehaus.jackson.map.deser.std.ObjectArrayDeserializer
{
    /**
     * @deprecated
     */
    @Deprecated
    public ArrayDeserializer(ArrayType arrayType, JsonDeserializer<Object> elemDeser)
    {
        this(arrayType, elemDeser, null);
    }

    public ArrayDeserializer(ArrayType arrayType, JsonDeserializer<Object> elemDeser,
            TypeDeserializer elemTypeDeser)
    {
        super(arrayType, elemDeser, elemTypeDeser);
    }
}

