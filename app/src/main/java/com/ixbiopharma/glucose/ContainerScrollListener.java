package com.ixbiopharma.glucose;

import android.view.MotionEvent;
import android.view.View;

import com.ixbiopharma.glucose.component.ScrollingValuePicker;

/**
 * ContainerScrollListener
 * <p>
 * Created by ivan on 22.04.17.
 */

public class ContainerScrollListener implements View.OnTouchListener {

    private ScrollingValuePicker myScrollingValuePicker;

    public ContainerScrollListener(ScrollingValuePicker myScrollingValuePicker) {
        this.myScrollingValuePicker = myScrollingValuePicker;
    }

    private int downX;
    private boolean moving = false;
    private int oldX = 0;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            downX = (int) event.getX();
            oldX = downX;
            moving = true;
            return true;
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            moving = false;
            return true;
        }

        if (moving) {
            int newx = (int) event.getX();
            myScrollingValuePicker.getScrollView().scrollBy((-newx + oldX), 0);
            oldX = newx;
        }
        return false;
    }
}
