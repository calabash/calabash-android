package sh.calaba.org.codehaus.jackson.map.deser;

/**
 * @deprecated Since 1.9, use {@link sh.calaba.org.codehaus.jackson.map.deser.std.ThrowableDeserializer} instead.
 */
@Deprecated
public class ThrowableDeserializer
    extends sh.calaba.org.codehaus.jackson.map.deser.std.ThrowableDeserializer
{
    public ThrowableDeserializer(BeanDeserializer baseDeserializer) {
        super(baseDeserializer);
    }
}
