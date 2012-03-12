package sh.calaba.org.codehaus.jackson.map.deser;

/**
 * @deprecated Since 1.9, use {@link sh.calaba.org.codehaus.jackson.map.deser.std.FromStringDeserializer} instead.
 */
@Deprecated
public abstract class FromStringDeserializer<T>
    extends sh.calaba.org.codehaus.jackson.map.deser.std.FromStringDeserializer<T>
{
    protected FromStringDeserializer(Class<?> vc) {
        super(vc);
    }
}
