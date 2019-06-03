package com.xamarin.xtcandroidsample;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.xamarin.xtcandroidsample.orientations.OrientationsActivity;

import java.util.ArrayList;

public class MainActivity extends Activity {
    public static final String SCREENS = "#Screens";
    public static final String FRAMES = "#Frames";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);

        this.findViewById(R.id.imageButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Dialog Text")
                        .setMessage("Question?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Toast.makeText(MainActivity.this, "Yes", Toast.LENGTH_LONG).show();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.getWindow().getAttributes().gravity = Gravity.BOTTOM | Gravity.RIGHT;

                alertDialog.show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void onButtonClick(View view) {
        assert view.getClass().isInstance(Button.class) || view.getClass().isInstance(ImageButton.class);

        switch (view.getId()) {
            case R.id.buttonGotoListView:
                startActivity(new Intent(this, ListViewActivity.class));
                break;

            case R.id.buttonGotoWebView: {
                Intent intent = new Intent(this, WebViewActivity.class);
                intent.putExtra(FRAMES, false);
                startActivity(intent);
                break;
            }

            case R.id.buttonGotoFramesWebView: {
                Intent intent = new Intent(this, WebViewActivity.class);
                intent.putExtra(FRAMES, true);
                startActivity(intent);
                break;
            }

            case R.id.buttonGotoDatePicker:
                startActivity(new Intent(this, DatePickerActivity.class));
                break;

            case R.id.buttonGotoScrollScreen:
                startActivity(new Intent(this, ScrollActivity.class));
                break;

            case R.id.buttonGotoViewData:
                startActivity(new Intent(this, ViewDataActivity.class));
                break;

            case R.id.buttonGotoSpellCheck:
                startActivity(new Intent(this, SpellCheckActivity.class));
                break;

            case R.id.buttonGotoDirectionalSwipe:
                startActivity(new Intent(this, DirectionalSwipeActivity.class));
                break;

            case R.id.buttonGotoViewsSample:
                startActivity(new Intent(this, ViewsActivity.class));
                break;

            case R.id.buttonGotoOrientations:
                startActivity(new Intent(this, OrientationsActivity.class));
                break;

            case R.id.buttonScrollWebView:
                startActivity(new Intent(this, ScrollWebViewActivity.class));
                break;

            case R.id.buttonScrollplicated:
                startActivity(new Intent(this, ScollplicatedActivity.class));
                break;

            case R.id.buttonMultiScoll:
                startActivity(new Intent(this, MultiScroll.class));
                break;

            case R.id.buttonShortScroll:
                startActivity(new Intent(this, ScrollShortActivity.class));
                break;

            case R.id.buttonHoriScroll:
                startActivity(new Intent(this, HorizontalScrollActivity.class));
                break;

            case R.id.buttonListView:
                startActivity(new Intent(this, ListViewActivity.class));
                break;

            case R.id.buttonListViewLong: {
                Intent intent = new Intent(this, ListViewActivity.class);
                intent.putExtra(SCREENS, 2);
                startActivity(intent);
                break;
            }

            case R.id.recycleView:
                startActivity(new Intent(this, RecycleViewActivity.class));
                break;

            case R.id.recycleViewLong: {
                Intent intent = new Intent(this, RecycleViewActivity.class);
                intent.putExtra(SCREENS, 2);
                startActivity(intent);
                break;
            }

            case R.id.buttonGotoTextfieldsView:
                startActivity(new Intent(this, TextfieldsActivity.class));
                break;

            case R.id.buttonDragAndDropView:
                startActivity(new Intent(this, DragAndDropActivity.class));
                break;

            case R.id.buttonGotoLocation:
                startActivity(new Intent(this, LocationActivity.class));
                break;

            case R.id.buttonCrashOnPurpose:
                throw new RuntimeException("Crashed on purpose!");

            case R.id.buttonNativeCrashOnPurpose:
                NativeCrash.crash();
                break;

            case R.id.buttonWebViewSwipe:
                startActivity(new Intent(this, SwipeWebViewActivity.class));
                break;
        }
    }

    public String backdoorString() {
        return "string";
    }

    public String backdoorWithString(String stringArg) {
        return stringArg;
    }

    public String backdoorWithString(String stringArg, ArrayList<String> stringArrayArg) {
        return stringArg + ":" + stringArrayArg.size();
    }
}
