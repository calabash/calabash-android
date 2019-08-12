package com.xamarin.xtcandroidsample;

import android.os.Bundle;
import android.app.Activity;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class DirectionalSwipeActivity extends Activity {
    private GestureDetector gestureDetector;
    View.OnTouchListener gestureListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.directional_swipe_sample);

        this.gestureDetector = new GestureDetector(this, new GestureListener());

        this.gestureListener = new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        };

        this.findViewById(R.id.relativeLayout).setOnTouchListener(this.gestureListener);
    }

    public void onSwipe(String message) {
        TextView textView = (TextView)this.findViewById(R.id.textViewHeader);
        textView.setText(message);
    }

    private class GestureListener extends SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent motionEventA, MotionEvent motionEventB, float vX, float vY) {
            boolean directionRight = (motionEventA.getX() < motionEventB.getX());
            boolean directionDown = (motionEventA.getY() < motionEventB.getY());

            float deltaX = Math.abs(motionEventB.getX() - motionEventA.getX());
            float deltaY = Math.abs(motionEventB.getY() - motionEventA.getY());

            float speedX = Math.abs(vX);
            float speedY = Math.abs(vY);

            boolean horizontal = (deltaX > deltaY);

            if ((horizontal && (speedX < 200 || deltaX < 100)) || (!horizontal && (speedY < 200 || deltaY < 100))) {
                return false;
            }

            String output = "";

            if (horizontal) {
                if (directionRight) {
                    output = "Right Swipe";
                } else {
                    output = "Left Swipe";
                }
            } else {
                if (directionDown) {
                    output = "Down Swipe";
                } else {
                    output = "Up Swipe";
                }
            }

            DirectionalSwipeActivity.this.onSwipe(output);

            return true;
        }
    }
}
