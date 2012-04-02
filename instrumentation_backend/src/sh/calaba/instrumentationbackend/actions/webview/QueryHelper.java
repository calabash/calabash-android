package sh.calaba.instrumentationbackend.actions.webview;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.org.codehaus.jackson.map.ObjectMapper;
import android.webkit.WebView;

public class QueryHelper {

	public static String executeJavascriptInWebview(String scriptPath, String... args) {
		
		String script = readJavascriptFromAsset(scriptPath);
		
		for (String arg : args) {
			script = script.replaceFirst("%@", arg);
		}

		final String myScript = script;
    	List<CalabashChromeClient> webViews = CalabashChromeClient.findAndPrepareWebViews();

    	for (CalabashChromeClient ccc : webViews) {
    		final WebView webView = ccc.getWebView();
    		InstrumentationBackend.solo.getCurrentActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                	webView.loadUrl("javascript:calabash_result = " + myScript + ";prompt('calabash:' + calabash_result);");
                }
    		});
    		
			return ccc.getResult();
		}
    	throw new RuntimeException("No webviews found");
	}
	
	@SuppressWarnings("unchecked")
	public static Map<String, Object> findFirstVisibleRectangle(List<HashMap<String,Object>> elements) {
		return (Map<String, Object>)findFirstVisibleElement(elements).get("rect");	
	}
	
	public static Map<String, Object> findFirstVisibleElement(List<HashMap<String,Object>> elements) {
		//TODO: Should do something more intelligent
		return (Map<String, Object>)elements.get(0);	
	}
	
	public static float[] getScreenCoordinatesForCenter(Map<String, Object> rectangle) {
		try {
			
			CalabashChromeClient calabashChromeClient = CalabashChromeClient.findAndPrepareWebViews().get(0);
		
			WebView webView = calabashChromeClient.getWebView();
			float scale = calabashChromeClient.getScale();
			
			System.out.println("scale: " + scale);
			int[] webviewLocation = new int[2];
			webView.getLocationOnScreen(webviewLocation);
			
			//TODO: Exception if center_x or center_y are not integers
			float x = webviewLocation[0] + (Integer)rectangle.get("center_x") * scale;
			float y = webviewLocation[1] + (Integer)rectangle.get("center_y") * scale;
			return new float[]{x, y};
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
}
