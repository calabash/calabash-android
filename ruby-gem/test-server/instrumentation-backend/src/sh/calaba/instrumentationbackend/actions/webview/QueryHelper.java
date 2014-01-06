package sh.calaba.instrumentationbackend.actions.webview;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.actions.webview.CalabashChromeClient.WebFuture;
import sh.calaba.org.codehaus.jackson.map.ObjectMapper;
import android.util.Log;
import android.webkit.ValueCallback;
import android.webkit.WebView;

public class QueryHelper {

	
	@SuppressWarnings("unchecked")
	public static Map<String, Object> findFirstVisibleRectangle(List<HashMap<String,Object>> elements) {
		return (Map<String, Object>)findFirstVisibleElement(elements).get("rect");	
	}
	
	public static Map<String, Object> findFirstVisibleElement(List<HashMap<String,Object>> elements) {
		//TODO: Should do something more intelligent
		return (Map<String, Object>)elements.get(0);	
	}
	
	public static float translateCoordToScreen(int offset, float scale, Object point) {
		return offset + ((Number)point).floatValue() *scale;
	}
	
	public static Map<String, Object> translateRectToScreenCoordinates(WebView webView, Map<String, Object> rectangle) {
		try {
			
            float scale = webView.getScale();

			int[] webviewLocation = new int[2];
			webView.getLocationOnScreen(webviewLocation);
			//center_x, center_y
			//left, top, width, height
			float center_x = translateCoordToScreen(webviewLocation[0], scale,
					rectangle.get("center_x"));
			float center_y = translateCoordToScreen(webviewLocation[1], scale,
					rectangle.get("center_y"));
									
			float x = translateCoordToScreen(webviewLocation[0], scale, rectangle.get("left"));
			float y = translateCoordToScreen(webviewLocation[0], scale, rectangle.get("top"));
			Map<String,Object> result = new HashMap<String, Object>(rectangle);
			
			result.put("x",x);
			result.put("y",y);
			result.put("center_x",center_x);
			result.put("center_y",center_y);
			
			return result;
			
	
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static String toJsonString(Object o) {
		//http://www.mkyong.com/java/how-to-convert-java-map-to-from-json-jackson/
		try {
			return new ObjectMapper().writeValueAsString(o);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	

    private static String readJavascriptFromAsset(String scriptPath) {
    	StringBuffer script = new StringBuffer();
		try {
			InputStream is = InstrumentationBackend.instrumentation.getContext().getResources().getAssets().open(scriptPath);
			BufferedReader input =  new BufferedReader(new InputStreamReader(is));
			String line = null;
			while (( line = input.readLine()) != null){
				script.append(line);
				script.append("\n");
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return script.toString();
    }
	
	public static WebFuture executeAsyncJavascriptInWebviews(WebView webView,
		String scriptPath, String selector, String type) {

		String script = readJavascriptFromAsset(scriptPath);

		script = script.replaceFirst("%@", selector);
		script = script.replaceFirst("%@", type);

        CalabashChromeClient chromeClient = CalabashChromeClient.prepareWebView(webView);

        if (android.os.Build.VERSION.SDK_INT < 19) { // Android 4.4
            webView.loadUrl("javascript:calabash_result = " + script + ";prompt('calabash:' + calabash_result);");
        } else {
            chromeClient.evaluateCalabashScript(script);
        }

        return chromeClient.getResult();
	}
}
