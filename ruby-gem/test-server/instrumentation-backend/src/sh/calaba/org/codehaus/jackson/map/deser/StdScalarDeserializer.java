package sh.calaba.org.codehaus.jackson.map.deser;

/**
 * @deprecated Since 1.9, use {@link sh.calaba.org.codehaus.jackson.map.deser.std.StdScalarDeserializer} instead.
 */
@Deprecated
public abstract class StdScalarDeserializer<T>
    extends sh.calaba.org.codehaus.jackson.map.deser.std.StdDeserializer<T>
{
    protected StdScalarDeserializer(Class<?> vc) {
        super(vc);
    } 
}
