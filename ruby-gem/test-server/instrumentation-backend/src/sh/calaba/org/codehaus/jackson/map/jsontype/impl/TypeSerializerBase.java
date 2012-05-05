package sh.calaba.org.codehaus.jackson.map.jsontype.impl;

import sh.calaba.org.codehaus.jackson.annotate.JsonTypeInfo;
import sh.calaba.org.codehaus.jackson.map.BeanProperty;
import sh.calaba.org.codehaus.jackson.map.TypeSerializer;
import sh.calaba.org.codehaus.jackson.map.jsontype.TypeIdResolver;

/**
 * @since 1.5
 */
public abstract class TypeSerializerBase extends TypeSerializer
{
    protected final TypeIdResolver _idResolver;

    protected final BeanProperty _property;
    
    protected TypeSerializerBase(TypeIdResolver idRes, BeanProperty property)
    {
        _idResolver = idRes;
        _property = property;
    }

    @Override
    public abstract JsonTypeInfo.As getTypeInclusion();

    @Override
    public String getPropertyName() { return null; }
    
    @Override
    public TypeIdResolver getTypeIdResolver() { return _idResolver; }
}
