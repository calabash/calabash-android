package sh.calaba.org.codehaus.jackson.map.ser;

import sh.calaba.org.codehaus.jackson.map.annotate.JacksonStdImpl;
import sh.calaba.org.codehaus.jackson.map.util.EnumValues;

/**
 * @deprecated Since 1.9 use {@link sh.calaba.org.codehaus.jackson.map.ser.std.EnumSerializer}
 */
@Deprecated
@JacksonStdImpl
public class EnumSerializer
    extends sh.calaba.org.codehaus.jackson.map.ser.std.EnumSerializer
{
    public EnumSerializer(EnumValues v) {
        super(v);
    }
}
