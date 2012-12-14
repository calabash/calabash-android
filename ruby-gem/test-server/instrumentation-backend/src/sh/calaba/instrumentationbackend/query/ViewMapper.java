package sh.calaba.instrumentationbackend.query;

import java.util.HashMap;
import java.util.Map;

import sh.calaba.instrumentationbackend.InstrumentationBackend;
import android.content.res.Resources;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class ViewMapper {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Object extractDataFromView(Object obj) {
		if (!(obj instanceof View)) {return obj;}
		View v = (View) obj;
		Map data = new HashMap();
		data.put("class", v.getClass().getSimpleName());
		data.put("description", v.toString());
		data.put("contentDescription", v.getContentDescription());
		data.put("enabled", v.isEnabled());
		String id = null;
		try {
			id = InstrumentationBackend.solo.getCurrentActivity()
					.getResources().getResourceEntryName(v.getId());
		} catch (Resources.NotFoundException e) {
			System.out.println("Resource not found for " + v.getId()
					+ ". Moving on.");
		}
		data.put("id", id);

		Map frame = new HashMap();
		int[] location = new int[2];
		v.getLocationOnScreen(location);

		frame.put("x", location[0]);
		frame.put("y", location[1]);
		frame.put("width", v.getWidth());
		frame.put("height", v.getHeight());

		data.put("frame", frame);

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

}
