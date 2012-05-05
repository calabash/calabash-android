package sh.calaba.org.codehaus.jackson.map.ser;

import sh.calaba.org.codehaus.jackson.map.annotate.JacksonStdImpl;

/**
 * @deprecated Since 1.9 use {@link sh.calaba.org.codehaus.jackson.map.ser.std.ToStringSerializer}
 */
@Deprecated
@JacksonStdImpl
public final class ToStringSerializer
    extends sh.calaba.org.codehaus.jackson.map.ser.std.ToStringSerializer
{
    public final static ToStringSerializer instance = new ToStringSerializer();
}
