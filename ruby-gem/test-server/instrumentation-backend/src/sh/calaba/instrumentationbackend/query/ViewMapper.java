package sh.calaba.instrumentationbackend.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.Set;

import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.query.ast.UIQueryUtils;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class ViewMapper {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object extractDataFromView(View v) {

		Map data = new HashMap();
		data.put("class", getClassNameForView(v));
		data.put("description", v.toString());
		data.put("contentDescription", getContentDescriptionForView(v));
		data.put("enabled", v.isEnabled());
		
		data.put("id", getIdForView(v));
		data.put("path", computePath(v));

		Map rect = getRectForView(v);

		data.put("rect", rect);

		if (v instanceof Button) {
			Button b = (Button) v;
			data.put("text", b.getText().toString());
		}
		if (v instanceof CheckBox) {
			CheckBox c = (CheckBox) v;
			data.put("checked", c.isChecked());
		}
		if (v instanceof TextView) {
			TextView t = (TextView) v;
			data.put("text", t.getText().toString());
		}
		return data;

	}


	Map<View, String> mViewPaths = new HashMap<View, String>(1024);
	Set<ViewGroup> mViewScanned = new HashSet<ViewGroup>(512);

	private void computeChildPaths(ViewGroup parent) {
		if (mViewScanned.add(parent)) {
			String path = computePath(parent);
			for (int i=0,n=parent.getChildCount(); i<n; i++) {
				mViewPaths.put(parent.getChildAt(i), path + "/" + i);
			}
		}
	}

	private String computePath(View v) {
		String path = mViewPaths.get(v);
		if (path == null) {
			ViewParent parent = v.getParent();
			if (parent instanceof ViewGroup) {
				computeChildPaths((ViewGroup) parent);
				path = mViewPaths.get(v); // should be populated now
			} else {
				mViewPaths.put(v, path = "");
			}
		}
		return path;
	}

	public static Map<String, Object> getRectForView(View v) {
		Map<String, Object> rect = new HashMap<String, Object>();
		int[] location = new int[2];
		v.getLocationOnScreen(location);

		rect.put("x", location[0]);
		rect.put("y", location[1]);
		
		rect.put("center_x", location[0] + v.getWidth()/2.0);
		rect.put("center_y", location[1] + v.getHeight()/2.0);
		
		rect.put("width", v.getWidth());
		rect.put("height", v.getHeight());
		return rect;
	}

	public static String getContentDescriptionForView(View v) {
		CharSequence description = v.getContentDescription();
		return description != null ? description.toString() : null;
	}

	public static String getClassNameForView(View v) {
		return v.getClass().getName();
	}

	public static String getIdForView(View v) {
		String id = null;
		try {
			id = InstrumentationBackend.solo.getCurrentActivity()
					.getResources().getResourceEntryName(v.getId());
		} catch (Resources.NotFoundException e) {
			System.out.println("Resource not found for " + v.getId()
					+ ". Moving on.");
		}
		return id;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object mapView(Object o) {
		if (o instanceof View) {
			return extractDataFromView((View) o);
		} 
		else if (o instanceof Map) {			
			Map copy = new HashMap();			
			for (Object e : ((Map) o).entrySet()) {
				Map.Entry entry = (Entry) e;
				Object value = entry.getValue();
				if (value instanceof View) {
					copy.put(entry.getKey(), UIQueryUtils.getId((View) value));
				}				
				else {
					copy.put(entry.getKey(),entry.getValue());
				}			
			}
			
			return copy;
		} 
		else if (o instanceof CharSequence) {
			return o.toString();
		}
		return o;
	}

	public static List<Object> mapViews(final List<?> input) {
		return UIQueryUtils.evaluateSyncInMainThread(new Callable<List<Object>>() {
			@Override
			public List<Object> call() throws Exception {
				List<Object> output = new ArrayList<Object>(input.size());
				ViewMapper mapper = new ViewMapper();
				for (Object o : input) output.add(mapper.mapView(o));
				return output;
			}
		});
	}
}
