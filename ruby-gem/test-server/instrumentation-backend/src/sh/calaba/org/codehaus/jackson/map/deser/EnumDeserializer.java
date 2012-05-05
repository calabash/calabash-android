package sh.calaba.org.codehaus.jackson.map.deser;

import sh.calaba.org.codehaus.jackson.map.util.EnumResolver;

/**
 * @deprecated Since 1.9, use {@link sh.calaba.org.codehaus.jackson.map.deser.std.EnumDeserializer} instead.
 */
@Deprecated
public class EnumDeserializer
    extends sh.calaba.org.codehaus.jackson.map.deser.std.EnumDeserializer
{
    public EnumDeserializer(EnumResolver<?> res) {
        super(res);
    }
}
