package com.xamarin.xtcandroidsample;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Display;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListViewActivity extends ListActivity {

    private static List<? extends Map<String, String>> createData(int normalItems) {
        ArrayList<Map<String, String>> data = new ArrayList<Map<String, String>>();

        for(int i = 0; i < normalItems; i++) {
            String value = "Item " + i;
            Map<String, String> element = new HashMap<String, String>();
            element.put("key", value);
            data.add(element);
        }
        Map<String, String> lastElement = new HashMap<String, String>();
        lastElement.put("key", "TheEnd");
        data.add(lastElement);
        return data;
    }

    private static List<? extends Map<String, String>> getScreenFullPlusListData(Activity activity, int screens) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        float height = display.getHeight();

        float listItemHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 64, activity.getResources().getDisplayMetrics());
        return createData(screens * (int) ((height/listItemHeight) + 4));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        int screens = intent.getIntExtra(MainActivity.SCREENS, 1);

        ListAdapter adapter = new SimpleAdapter(this, getScreenFullPlusListData(this, screens), android.R.layout.simple_list_item_1, new String[] {"key"}, new int[] {android.R.id.text1});
        setListAdapter(adapter);
    }
}
