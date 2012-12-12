package sh.calaba.instrumentationbackend.query.ast;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UIQueryUtils {
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List subviews(Object o)
	{
		try {
			Method getChild = o.getClass().getMethod("getChildAt", int.class);
			getChild.setAccessible(true);
			Method getChildCount = o.getClass().getMethod("getChildCount");
			getChildCount.setAccessible(true);
			List result = new ArrayList(8);
			int childCount = (Integer) getChildCount.invoke(o);
			for (int i=0;i<childCount;i++)
			{
				result.add(getChild.invoke(o, i));
			}
			return result;
			
		} catch (NoSuchMethodException e) {			
			return Collections.EMPTY_LIST;
		} catch (IllegalArgumentException e) {			
			return Collections.EMPTY_LIST;
		} catch (IllegalAccessException e) {			
			return Collections.EMPTY_LIST;
		} catch (InvocationTargetException e) {
			return Collections.EMPTY_LIST;
		}
		
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List parents(Object o)
	{
		try {
			
			Method getParent = o.getClass().getMethod("getParent");
			getParent.setAccessible(true);
			
			List result = new ArrayList(8);
			try {
				while (true)
				{
					Object parent = getParent.invoke(o);
					if (parent == null)
					{
						return result;
					}
					else 
					{
						result.add(parent);
					}
					o = parent;	
				}											
			} catch (IllegalArgumentException e) {
				return result;
			} catch (IllegalAccessException e) {
				return result;
			} catch (InvocationTargetException e) {
				return result;
			} 
			
			
		} catch (NoSuchMethodException e) {
			return Collections.EMPTY_LIST;
		} 
		
	}

	@SuppressWarnings({ "rawtypes" })
	public static Method hasProperty(Object o, String propertyName) {
		
		Class c = o.getClass();
		Method[] methods = c.getMethods();
		
		for (Method m : methods)
		{
			String methodName = m.getName();
			if (methodName.equals(propertyName) || 
				methodName.equals("is"+captitalize(propertyName)) ||
				methodName.equals("get"+captitalize(propertyName)))
			{
				return m;
			}
		}
		
		return null;
		
	}


	private static String captitalize(String propertyName) {
		return propertyName.substring(0,1).toUpperCase() + propertyName.substring(1);
	}

	public static Object getProperty(Object receiver, Method m) {		
		try {
			return m.invoke(receiver);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) { 
			throw new RuntimeException(e);
		}
	}
}
