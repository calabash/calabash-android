package sh.calaba.org.codehaus.jackson.map.deser;

/**
 * @deprecated Since 1.9, use {@link sh.calaba.org.codehaus.jackson.map.deser.std.ContainerDeserializerBase} instead.
 */
@Deprecated
public abstract class ContainerDeserializer<T>
    extends sh.calaba.org.codehaus.jackson.map.deser.std.ContainerDeserializerBase<T>
{
    protected ContainerDeserializer(Class<?> selfType)
    {
        super(selfType);
    }
}
