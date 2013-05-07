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

import org.antlr.runtime.tree.CommonTree;

import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.actions.webview.QueryHelper;
import sh.calaba.instrumentationbackend.query.CompletedFuture;
import sh.calaba.instrumentationbackend.query.Query;
import sh.calaba.instrumentationbackend.query.ViewMapper;
import sh.calaba.instrumentationbackend.query.antlr.UIQueryParser;
import sh.calaba.org.codehaus.jackson.map.ObjectMapper;
import sh.calaba.org.codehaus.jackson.type.TypeReference;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

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

        if (view.getWidth() == 0 || view.getWidth() == 0) {
            return false;
        }

		return view.isShown() && viewFetcher.isViewSufficientlyShown(view);
	}

	public static String getId(View view) {
		return ViewMapper.getIdForView(view);
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
					parsedResult = new ObjectMapper().readValue(
									jsonResponse,
									new TypeReference<List<HashMap<String, Object>>>() {});
					for (Map<String,Object> data : parsedResult) {
						Map<String,Object> rect = (Map<String, Object>) data.get("rect");
						Map <String,Object> updatedRect = QueryHelper.translateRectToScreenCoordinates(webView, rect);
						data.put("rect", updatedRect);
						data.put("webView", webView);
					}
					return parsedResult;
				} catch (Exception igored) {
					try {
						Map resultAsMap = new ObjectMapper().readValue(
											jsonResponse,
											new TypeReference<HashMap>() {});
						//This usually happens in case of error
						//check this case
						System.out.println(resultAsMap);
						String errorMsg = (String) resultAsMap.get("error");
						System.out.println(errorMsg);
						return Collections.singletonList(resultAsMap);
					} catch (Exception e) {
						e.printStackTrace();
						throw new RuntimeException(e);		
					}
					
					
				}		
			}
		});
		
	}

	public static Object parseValue(CommonTree val) {
		switch (val.getType()) {
		case UIQueryParser.STRING: {
			String textWithPings = val.getText();
			String text = textWithPings
					.substring(1, textWithPings.length() - 1);
            text = text.replaceAll("\\\\'", "'");
			return text;
		}
		case UIQueryParser.INT:
			return Integer.parseInt(val.getText(), 10);
		case UIQueryParser.BOOL: {
			String text = val.getText();
			return Boolean.parseBoolean(text);
		}
		case UIQueryParser.NIL:
			return null;

		default:
			throw new IllegalArgumentException("Unable to parse value type:"
					+ val.getType() + " text " + val.getText());

		}

	}

/*
 * {"rect"=>{"x"=>0, "y"=>0, "width"=>768, "height"=>1024},
 "hit-point"=>{"x"=>384, "y"=>512},
 "id"=>"",
 "action"=>false,
 "enabled"=>1,
 "visible"=>1,
 "value"=>nil,
 "type"=>"[object UIAWindow]",
 "name"=>nil,
 "label"=>nil,
 "children"=> [same-structure*]
 }
 */
	public static Map<?,?> dump() 
	{
		Query mainQuery = new Query("not_used");		
		return dumpRecursively(emptyRootView(), mainQuery.rootViews());
		
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected static Map<?,?> dumpRecursively(Map parentView,List<View> children)
	{
		ArrayList childrenArray = new ArrayList(32);
		for (View view : children) {			
			childrenArray.add(dumpRecursively(serializeViewToDump(view),  UIQueryUtils.subviews(view)));
		}
			
		parentView.put("children", childrenArray);
		
		return parentView;
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Map<?,?> serializeViewToDump(View view) {
		if (view == null) {return null;}
		
		Map m = new HashMap();
		
		m.put("id",getId(view));
		
		Map rect = ViewMapper.getRectForView(view);
		Map hitPoint = extractHitPointFromRect(rect);
		
		m.put("rect",rect);
		m.put("hit-point",hitPoint);
		m.put("action",isAction(view));
		m.put("enabled",view.isEnabled());
		m.put("visible",isVisible(view));
		m.put("value",extractValueFromView(view));
		m.put("type",ViewMapper.getClassNameForView(view));
		m.put("name",getId(view));//TODO: does name make sense on Android?
		m.put("label",ViewMapper.getContentDescriptionForView(view));									
		return m;
	}

	public static Object extractValueFromView(View view) {
		if (view instanceof Button) {
			Button b = (Button) view;
			return b.getText().toString();
		}
		else if (view instanceof CheckBox) {
			CheckBox c = (CheckBox) view;
			return c.isChecked();
		}
		else if (view instanceof TextView) {
			TextView t = (TextView) view;
			return t.getText().toString();
		}
		return view.toString();
	}

	public static boolean isAction(View view) 
	{
		
		return (view instanceof android.widget.Button);
		//TODO: obviously many more!
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Map extractHitPointFromRect(Map rect) {
		Map hitPoint = new HashMap();
		hitPoint.put("x", rect.get("center_x"));
		hitPoint.put("y", rect.get("center_y"));
		return hitPoint;
	}

	@SuppressWarnings({"unchecked", "rawtypes", "serial"})
	private static Map<?,?> emptyRootView() {
		return new HashMap() {{				
			put("id",null);
			put("rect",null);
			put("hit-point",null);
			put("action",false);
			put("enabled",false);
			put("visible",true);
			put("value",null);
			put("type","[object CalabashRootView]");
			put("name",null);
			put("label",null);									
		}};
	}

}
