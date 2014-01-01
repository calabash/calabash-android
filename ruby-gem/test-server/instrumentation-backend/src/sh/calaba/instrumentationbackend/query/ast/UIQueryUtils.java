package sh.calaba.instrumentationbackend.query.ast;

import static sh.calaba.instrumentationbackend.InstrumentationBackend.viewFetcher;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import org.antlr.runtime.tree.CommonTree;

import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.actions.webview.CalabashChromeClient.WebFuture;
import sh.calaba.instrumentationbackend.actions.webview.QueryHelper;
import sh.calaba.instrumentationbackend.query.CompletedFuture;
import sh.calaba.instrumentationbackend.query.Query;
import sh.calaba.instrumentationbackend.query.ViewMapper;
import sh.calaba.instrumentationbackend.query.antlr.UIQueryParser;
import sh.calaba.org.codehaus.jackson.map.ObjectMapper;
import sh.calaba.org.codehaus.jackson.type.TypeReference;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class UIQueryUtils {

	private static final Set<String> DOM_TEXT_TYPES;
	static {
		DOM_TEXT_TYPES = new HashSet<String>();
		DOM_TEXT_TYPES.add("email");
		DOM_TEXT_TYPES.add("text");
		DOM_TEXT_TYPES.add("");
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List subviews(Object o) {
		
		try {
			Method getChild = o.getClass().getMethod("getChildAt", int.class);
			getChild.setAccessible(true);
			Method getChildCount = o.getClass().getMethod("getChildCount");
			getChildCount.setAccessible(true);
			List result = new ArrayList(8);
			int childCount = (Integer) getChildCount.invoke(o);
			for (int i = 0; i < childCount; i++) {
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

	@SuppressWarnings({ "rawtypes" })
	public static Future webViewSubViews(WebView o) {
		
		Log.i("Calabash", "About to webViewSubViews");
		

		WebFuture controls = QueryHelper.executeAsyncJavascriptInWebviews(o,
				"calabash.js", "input,button","css");
		
		return controls;
		
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List parents(Object o) {
		try {

			Method getParent = o.getClass().getMethod("getParent");
			getParent.setAccessible(true);

			List result = new ArrayList(8);
			try {
				while (true) {
					Object parent = getParent.invoke(o);
					if (parent == null) {
						return result;
					} else {
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
		Method method = methodOrNull(c, propertyName);
		if (method != null) {
			return method;
		}
		method = methodOrNull(c, "get" + captitalize(propertyName));
		if (method != null) {
			return method;
		}
		method = methodOrNull(c, "is" + captitalize(propertyName));
		return method;

		/*
		 * for (Method m : methods) { String methodName = m.getName(); if
		 * (methodName.equals(propertyName) ||
		 * methodName.equals("is"+captitalize(propertyName)) ||
		 * methodName.equals("get"+captitalize(propertyName))) { return m; } }
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
		return propertyName.substring(0, 1).toUpperCase()
				+ propertyName.substring(1);
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
		if (!(v instanceof View)) {
			return true;
		}
		View view = (View) v;

		if (view.getHeight() == 0 || view.getWidth() == 0) {
			return false;
		}

		return view.isShown() && viewFetcher.isViewSufficientlyShown(view);
	}

	public static boolean isClickable(Object v) {
		if (!(v instanceof View)) {
			return true;
		}
		View view = (View) v;

		return view.isClickable();
	}

	public static String getId(View view) {
		return ViewMapper.getIdForView(view);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
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
					} else {
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
			return evaluateAsyncInMainThread(callable)
					.get(10, TimeUnit.SECONDS);
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<Map<String, Object>> mapWebViewJsonResponse(
			final String jsonResponse, final WebView webView) {
		return (List<Map<String, Object>>) evaluateSyncInMainThread(new Callable() {

			@Override
			public Object call() throws Exception {
				List<Map<String, Object>> parsedResult;
				try {
					parsedResult = new ObjectMapper().readValue(jsonResponse,
							new TypeReference<List<HashMap<String, Object>>>() {
							});
					for (Map<String, Object> data : parsedResult) {
						Map<String, Object> rect = (Map<String, Object>) data.get("rect");
						Map<String, Object> updatedRect = QueryHelper.translateRectToScreenCoordinates(webView, rect);
						data.put("rect", updatedRect);
						data.put("webView", webView);
					}
					return parsedResult;
				} catch (Exception igored) {
					try {
						Map resultAsMap = new ObjectMapper().readValue(
								jsonResponse, new TypeReference<HashMap>() {
								});
						// This usually happens in case of error
						// check this case
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
	 * 
	 * {"rect"=>{"x"=>0, "y"=>0, "width"=>768, "height"=>1024},
	 * "hit-point"=>{"x"=>384, "y"=>512}, "id"=>"", "action"=>false,
	 * "enabled"=>1, "visible"=>1, "value"=>nil, "type"=>"[object UIAWindow]",
	 * "name"=>nil, "label"=>nil, "children"=> [(samestructure)*]
	 */
	public static Map<?, ?> dump() {
		Query dummyQuery = new Query("not_used");

		return dumpRecursively(emptyRootView(), dummyQuery.rootViews());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map<?, ?> mapWithElAsNull(Map<?, ?> dump) {
		if (dump == null)
			return null;
		HashMap result = new HashMap(dump);
		result.put("el", null);
		return result;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected static Map<?, ?> dumpRecursively(Map parentView,
			List children) {
		ArrayList childrenArray = new ArrayList(32);
		for (int i = 0; i < children.size(); i++) {
			Object view = children.get(i);
			Map serializedChild = serializeViewToDump(view);
			List<Integer> childPath = new ArrayList<Integer>(
					(List) parentView.get("path"));
			childPath.add(i);
			serializedChild.put("path", childPath);
			List childrenList = null;
			if (view instanceof WebView) {
				Future webViewSubViews = webViewSubViews((WebView) view);
				childrenArray.add(webViewSubViews);
			}
			else {
				childrenList = UIQueryUtils.subviews(view);
				childrenArray.add(dumpRecursively(serializedChild,
						childrenList));
			}
			
		}

		parentView.put("children", childrenArray);

		return parentView;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Map<?, ?> dumpByPath(List<Integer> path) {
		Query dummyQuery = new Query("not_used");

		Map currentView = emptyRootView();
		List<View> currentChildren = dummyQuery.rootViews();

		for (Integer i : path) {
			if (i < currentChildren.size()) {
				View child = currentChildren.get(i);
				currentView = serializeViewToDump(child);
				currentChildren = UIQueryUtils.subviews(child);
			} else {
				return null;
			}

		}

		return currentView;
	}

	/*
 * 
                                            "enabled" => true,
                                            "visible" => true,
                                           "children" => [],
                                              "label" => nil,
                                               "rect" => {
                                            "center_y" => 158.5,
                                            "center_x" => 300.0,
                                              "height" => 25,
                                                   "y" => 146,
                                               "width" => 600,
                                                   "x" => 0
                                        },
                                               "type" => "android.widget.TextView",
                                                 "id" => "FacebookTextView",
                                                 "el" => nil,
                                               "name" => "",
                                             "action" => nil,
                                              "value" => "",
                                               "path" => [
                                            [0] 0,
                                            [1] 0,
                                            [2] 2,
                                            [3] 0,
                                            [4] 2
                                        ],
                                          "hit-point" => {
                                            "y" => 158.5,
                                            "x" => 300.0
                                        },
                                        "entry_types" => [
                                            [0] "0"
                                        ]
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Map<?, ?> serializeViewToDump(Object viewOrMap) {
		if (viewOrMap == null) {
			return null;
		}
		
		if (viewOrMap instanceof Map) 
		{
			Map map = (Map) viewOrMap;			
			map.put("el", map);

			Map rect = (Map) map.get("rect");
			Map hitPoint = extractHitPointFromRect(rect);
			
			map.put("hit-point", hitPoint);			
			map.put("enabled", true);
			map.put("visible", true);
			map.put("value", null);
			map.put("type", "dom");
			map.put("name", null);
			map.put("label", null);
			map.put("children", Collections.EMPTY_LIST);
			String html = (String)map.get("html");
			String nodeName = (String) map.get("nodeName");			
			if (nodeName != null && nodeName.toLowerCase().equals("input")) {				
				String domType = extractDomType(html);
				if (isDomPasswordType(domType)) {
					map.put("entry_types", Collections.singletonList("password"));
				}
				else if (isDomTextType(domType)) {
					map.put("entry_types", Collections.singletonList("text"));
				} 
				else {
					map.put("entry_types", Collections.emptyList());	
				}					
				map.put("value", extractAttribute(html, "value"));
				map.put("type", "dom");
				map.put("name", extractAttribute(html, "name"));
				map.put("label", extractAttribute(html, "title"));
			}					
						
			return map;	
			
		}
		else 
		{
			Map m = new HashMap();
			
			View view = (View) viewOrMap;
			m.put("id", getId(view));
			m.put("el", view);

			Map rect = ViewMapper.getRectForView(view);
			Map hitPoint = extractHitPointFromRect(rect);

			m.put("rect", rect);
			m.put("hit-point", hitPoint);
			m.put("action", actionForView(view));
			m.put("enabled", view.isEnabled());
			m.put("visible", isVisible(view));
			m.put("entry_types", elementEntryTypes(view));
			m.put("value", extractValueFromView(view));
			m.put("type", ViewMapper.getClassNameForView(view));
			m.put("name", getNameForView(view));
			m.put("label", ViewMapper.getContentDescriptionForView(view));
			return m;	
		}

		

		
	}

	private static boolean isDomTextType(String domType) {
		if (domType == null) {
			return true;
		}
		return DOM_TEXT_TYPES.contains(domType);
	}

	private static boolean isDomPasswordType(String domType) {		
		return "password".equalsIgnoreCase(domType);
	}

	// naive implementation only works for (valid) input tags
	public static String extractDomType(String input) {
		return extractAttribute(input, "type");		
	}
	
	public static String extractAttribute(String input, String attribute) {
		String[] split = input.split(attribute+"=");
		if (split.length == 1) {
			split = input.split(attribute+" =");			
		}
		if (split.length > 1) {
			String lastPart = split[1];
			if (lastPart == null) {
				return null;	
			}				
			if (lastPart.charAt(0) == '"' || lastPart.charAt(0) == '\'') {
				int endIndex = -1;
				for (int i=1;i<lastPart.length();i++) {
					if (lastPart.charAt(i) == '\'' || lastPart.charAt(i) == '"') {
						endIndex = i;
						break;
					}
				}
				
				if (endIndex > 0) {
					return lastPart.substring(1,endIndex);
				}
				
			}			
		}
		return null;
		
	}



	public static List<String> elementEntryTypes(View view) {
		if (view instanceof TextView) {
			TextView textView = (TextView) view;
			return mapTextViewInputTypes(textView.getInputType());
		}
		return null;

	}

	public static List<String> mapTextViewInputTypes(int inputType) {
		List<String> inputTypes = new ArrayList<String>();
		if (inputTypeHasTrait(inputType, InputType.TYPE_TEXT_VARIATION_PASSWORD)
				|| inputTypeHasTrait(inputType,
						InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD)) {
			inputTypes.add("password");
		}
		if (inputTypeHasTrait(inputType, InputType.TYPE_CLASS_NUMBER)) {
			inputTypes.add("numeric");
		}
		inputTypes.add(String.valueOf(inputType));

		return inputTypes;
	}

	private static boolean inputTypeHasTrait(int inputType, int inputTypeTrait) {
		return (inputType & inputTypeTrait) != 0;
	}

	private static Object getNameForView(View view) {
		Object result = null;
		Method hintMethod = hasProperty(view, "hint");
		if (hintMethod != null) {
			result = getProperty(view, hintMethod);
		}
		if (result != null) {
			return result.toString();
		}
		Method textMethod = hasProperty(view, "text");
		if (textMethod != null) {
			result = getProperty(view, textMethod);
		}
		if (result != null) {
			return result.toString();
		}

		return null;
	}

	public static Object extractValueFromView(View view) {
		if (view instanceof Button) {
			Button b = (Button) view;
			return b.getText().toString();
		} else if (view instanceof CheckBox) {
			CheckBox c = (CheckBox) view;
			return c.isChecked();
		} else if (view instanceof TextView) {
			TextView t = (TextView) view;
			return t.getText().toString();
		}
		return null;
	}

	/*
	 * function action(el) { var normalized = normalize(el); if (!normalized) {
	 * return false; } if (normalized instanceof UIAButton) { return {
	 * "type":'touch', "gesture":'tap' }; } //TODO MORE return false; }
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Map<?, ?> actionForView(View view) {
		Map result = null;
		if (view instanceof android.widget.Button
				|| view instanceof android.widget.ImageButton) {
			result = new HashMap();
			result.put("type", "touch");
			result.put("gesture", "tap");
		}

		// TODO: obviously many more!
		return result;

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Map extractHitPointFromRect(Map rect) {
		Map hitPoint = new HashMap();
		hitPoint.put("x", rect.get("center_x"));
		hitPoint.put("y", rect.get("center_y"));
		return hitPoint;
	}

	@SuppressWarnings({ "unchecked", "rawtypes", "serial" })
	private static Map<?, ?> emptyRootView() {
		return new HashMap() {
			{
				put("id", null);
				put("el", null);
				put("rect", null);
				put("hit-point", null);
				put("action", false);
				put("enabled", false);
				put("visible", true);
				put("value", null);
				put("path", new ArrayList<Integer>());
				put("type", "[object CalabashRootView]");
				put("name", null);
				put("label", null);
			}
		};
	}

}
