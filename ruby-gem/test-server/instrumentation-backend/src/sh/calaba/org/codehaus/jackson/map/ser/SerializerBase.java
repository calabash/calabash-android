package sh.calaba.org.codehaus.jackson.map.ser;

import sh.calaba.org.codehaus.jackson.type.JavaType;

/**
 * @deprecated Since 1.9 use {@link sh.calaba.org.codehaus.jackson.map.ser.std.SerializerBase}
 */
@Deprecated
public abstract class SerializerBase<T>
    extends sh.calaba.org.codehaus.jackson.map.ser.std.SerializerBase<T>
{
    protected SerializerBase(Class<T> t) {
        super(t);
    }

    protected SerializerBase(JavaType type) {
        super(type);
    }
    
    protected SerializerBase(Class<?> t, boolean dummy) {
        super(t, dummy);
    }
}
