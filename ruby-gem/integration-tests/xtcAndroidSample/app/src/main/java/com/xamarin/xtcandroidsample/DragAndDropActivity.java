package com.xamarin.xtcandroidsample;

import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.app.Activity;
import android.widget.*;
import android.view.*;

public class DragAndDropActivity extends Activity implements View.OnTouchListener {
    RelativeLayout dragAndDropActivity;
    ImageView redImageView;
    TextView targetTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag_and_drop);

        dragAndDropActivity = (RelativeLayout)findViewById(R.id.dragAndDropActivity);
        dragAndDropActivity.setOnTouchListener(this);

        redImageView = (ImageView) findViewById(R.id.redImageView);
        redImageView.setOnTouchListener(this);

        targetTextView = (TextView)findViewById(R.id.targetTextView);
    }

    private boolean dragging = false;
    private Rect hitRect = new Rect();

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        boolean eventConsumed = true;
        int x = (int)event.getX();
        int y = (int)event.getY();

        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            if (v == redImageView) {
                dragging = true;
                eventConsumed = false;
            }
        } else if (action == MotionEvent.ACTION_UP) {
            if (dragging) {
                targetTextView.getHitRect(hitRect);
                if (hitRect.contains(x, y)) {
                    targetTextView.setBackgroundColor(Color.parseColor("#FFAB3933"));
                    targetTextView.setText("red");
                }
                setAbsoluteLocation(redImageView, 0, 0);
            }
            dragging = false;
            eventConsumed = false;
        } else if (action == MotionEvent.ACTION_MOVE) {
            if (v != redImageView) {
                if (dragging) {
                    setAbsoluteLocationCentered(redImageView, x, y);
                }
            }
        }

        return eventConsumed;
    }

    private void setAbsoluteLocationCentered(View v, int x, int y) {
        setAbsoluteLocation(v, x - v.getWidth() / 2, y - v.getHeight() / 2);
    }

    private void setAbsoluteLocation(View v, int x, int y) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)v.getLayoutParams();
        layoutParams.leftMargin = x;
        layoutParams.topMargin = y;
        v.setLayoutParams(layoutParams);
    }
}
