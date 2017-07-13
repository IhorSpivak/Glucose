package com.ixbiopharma.glucose.utils;

import android.content.Context;

import com.ixbiopharma.glucose.component.ObservableHorizontalScrollView;


/**
 * DWUtils
 *
 * Created by ivan on 28.03.17.
 */

public class DWUtils {

    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static int getValueAndScrollItemToCenter(ObservableHorizontalScrollView view, int l, int t, float MAX_VALUE, float MIN_VALUE, float multipleSize) {
        return getValueAndScrollItemToCenter(view, l, t, MAX_VALUE, MIN_VALUE, multipleSize, 1);
    }

    private static int getValueAndScrollItemToCenter(ObservableHorizontalScrollView view, int l, int t, float MAX_VALUE, float MIN_VALUE, float multipleSize, int valueMultiple) {
        float oneValue = (float) view.getWidth() * multipleSize / (MAX_VALUE - MIN_VALUE);
        int value = (int) (l / oneValue) + (int) MIN_VALUE;
        int offset = (int) (l % oneValue);


        if (offset > oneValue / 2) {
            value += 1;
            view.smoothScrollBy((int) oneValue - offset, 0);

        } else {
            view.smoothScrollBy(-offset, 0);
        }

        if (value > MAX_VALUE) {
            value = (int) MAX_VALUE;
        }

        return value * valueMultiple;
    }

    public static void scrollToValue(ObservableHorizontalScrollView view, float value, float MAX_VALUE, float MIN_VALUE, float multipleSize) {
        float oneValue = (float) view.getWidth() * multipleSize / (MAX_VALUE - MIN_VALUE);
        float valueWidth = oneValue * (value - MIN_VALUE);

        view.scrollBy((int) valueWidth, 0);
    }

}
