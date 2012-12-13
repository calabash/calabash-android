package sh.calaba.instrumentationbackend.query;

import android.content.res.Resources;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import sh.calaba.instrumentationbackend.InstrumentationBackend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static sh.calaba.instrumentationbackend.InstrumentationBackend.*;

public class Query {

    private String queryString;

    public Query(String queryString) {
        System.out.println("CREATED QUERY: " + queryString);
        this.queryString = queryString;
    }


    public QueryResult execute() {
        List<Map<?,?>> result = new ArrayList<Map<?, ?>>();
        for (View v : allVisibleViews()) {

            if (!v.isShown() ) {
                continue;
            }

            if (queryString != null) {
                if (!queryString.trim().equals("")) {
                    if (!queryString.equalsIgnoreCase(v.getClass().getSimpleName())) {
                        continue;
                    }
                }
            }

            System.out.println(v.getClass().getSimpleName().toString());
            Map view = new HashMap();
            view.put("class", v.getClass().getSimpleName());
            view.put("description", v.toString());
            view.put("contentDescription", v.getContentDescription());
            view.put("enabled", v.isEnabled());
            String id = null;
            try {
                id = InstrumentationBackend.solo.getCurrentActivity().getResources().getResourceEntryName(v.getId());
            } catch (Resources.NotFoundException e) {
                System.out.println("Resource not found for " + v.getId() + ". Moving on.");
            }
            view.put("id", id);

            Map frame = new HashMap();
            int[] location = new int[2];
            v.getLocationOnScreen(location);

            frame.put("x", location[0]);
            frame.put("y", location[1]);
            frame.put("width", v.getWidth());
            frame.put("height", v.getHeight());

            view.put("frame", frame);

            if (v instanceof Button) {
                Button b = (Button)v;
                view.put("text", b.getText().toString());
            }
            if (v instanceof CheckBox) {
                CheckBox c = (CheckBox)v;
                view.put("checked", c.isChecked());
            }
            if (v instanceof TextView) {
                TextView t = (TextView)v;
                view.put("text", t.getText().toString());
            }

            result.add(view);
        }
        return new QueryResult(result);
    }


    public List<View> allVisibleViews() {
        return viewFetcher.getAllViews(false);
    }
}
