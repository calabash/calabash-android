package sh.calaba.org.codehaus.jackson.map.deser;

/**
 * @deprecated Since 1.9, use {@link sh.calaba.org.codehaus.jackson.map.deser.std.StdKeyDeserializer} instead.
 */
@Deprecated
public abstract class StdKeyDeserializer
    extends sh.calaba.org.codehaus.jackson.map.deser.std.StdKeyDeserializer
{
    protected StdKeyDeserializer(Class<?> cls) { super(cls); }
}

