package com.xamarin.xtcandroidsample;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;

public class RecycleViewActivity extends ActionBarActivity {

    private static String[] createData(int normalItems) {
        String[] data = new String[normalItems + 1];

        for(int i = 0; i < normalItems; i++) {
            String value = "Item " + i;
            data[i] = value;
        }
        data[normalItems] = "TheEnd";
        return data;
    }

    private static String[] getScreenFullPlusListData(Activity activity, int screens) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        float height = display.getHeight();

        float listItemHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 64, activity.getResources().getDisplayMetrics());
        return createData(screens * (int) ((height/listItemHeight) + 4));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle_view_activity);


        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        Intent intent = getIntent();
        int screens = intent.getIntExtra(MainActivity.SCREENS, 1);


        // specify an adapter (see also next example)
        RecycleViewAdapter mAdapter = new RecycleViewAdapter(getScreenFullPlusListData(this, screens));
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_recycle_view_example, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
