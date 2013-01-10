package sh.calaba.instrumentationbackend.query.ast;

import static sh.calaba.instrumentationbackend.InstrumentationBackend.viewFetcher;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.actions.webview.QueryHelper;
import sh.calaba.instrumentationbackend.query.CompletedFuture;
import sh.calaba.org.codehaus.jackson.map.ObjectMapper;
import sh.calaba.org.codehaus.jackson.type.TypeReference;
import android.content.res.Resources.NotFoundException;
import android.view.View;
import android.webkit.WebView;

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

	public static boolean isVisible(Object v) {
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

	@SuppressWarnings("rawtypes")
	public static Future evaluateAsyncInMainThread(final Callable callable) throws Exception {
		final AtomicReference<Future> result = new AtomicReference<Future>();
		final AtomicReference<Exception> errorResult = new AtomicReference<Exception>();
		
		InstrumentationBackend.instrumentation.runOnMainSync(new Runnable() {			
			@SuppressWarnings("unchecked")
			public void run() {
				try {
					Object res = callable.call();
					if (res instanceof Future) {
						result.set((Future) res);
					}
					else {
						result.set(new CompletedFuture(res));
					}
				} catch (Exception e) {
					errorResult.set(e);
				}			
			}			
		});
		if (result.get() == null) {
			throw errorResult.get();
		}
		return result.get();
	}

	@SuppressWarnings("rawtypes")
	public static Object evaluateSyncInMainThread(Callable callable) {
		try {
			return evaluateAsyncInMainThread(callable).get(10, TimeUnit.SECONDS);
		} catch (RuntimeException e) {
			throw e;
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} 
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<Map<String, Object>> mapWebViewJsonResponse(final String jsonResponse, final WebView webView) {
		return (List<Map<String, Object>>) evaluateSyncInMainThread(new Callable() {

			@Override
			public Object call() throws Exception {
				List<Map<String, Object>> parsedResult;
				try {
					parsedResult = new ObjectMapper()
					.readValue(
							jsonResponse,
							new TypeReference<List<HashMap<String, Object>>>() {
							});
					for (Map<String,Object> data : parsedResult) {
						Map<String,Object> rect = (Map<String, Object>) data.get("rect");
						Map <String,Object> updatedRect = QueryHelper.translateRectToScreenCoordinates(webView, rect);
						data.put("rect", updatedRect);
						data.put("webView", webView);
					}
					return parsedResult;
				} catch (Exception e) {
					e.printStackTrace();
					throw new RuntimeException(e);		
				}		
			}
		});
		
	}

}
