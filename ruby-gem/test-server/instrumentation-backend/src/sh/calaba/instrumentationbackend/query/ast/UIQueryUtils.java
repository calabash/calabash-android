package sh.calaba.instrumentationbackend.query.ast;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import sh.calaba.instrumentationbackend.InstrumentationBackend;
import android.content.res.Resources.NotFoundException;
import android.view.View;

import com.jayway.android.robotium.solo.PublicViewFetcher;

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
		Method method = methodOrNull(c,propertyName);
		if (method != null) { return method;}
		method = methodOrNull(c,"get"+captitalize(propertyName));
		if (method != null) { return method;}
		method = methodOrNull(c,"is"+captitalize(propertyName));
		return method;
				
/*		
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
*/
		
		
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static Method methodOrNull(Class c, String methodName) {
		try {
			return c.getMethod(methodName);
		} catch (NoSuchMethodException e) {
			return null;
		}
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

	public static boolean isVisible(PublicViewFetcher viewFetcher, Object v) {
		if (!(v instanceof View)) { return true; }		
		View view = (View) v;
		return view.isShown() && viewFetcher.isViewSufficientlyShown(view);
	}

	public static String getId(View view) {
		try {
			return InstrumentationBackend.solo.getCurrentActivity()
					.getResources().getResourceEntryName(view.getId());			
	
		}
		catch (NotFoundException e) {}
		return null;
	}
	
}
