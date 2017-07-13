package com.ixbiopharma.glucose.utils;

import android.app.Activity;
import android.content.Context;
import android.util.TypedValue;
import android.view.inputmethod.InputMethodManager;

import com.ixbiopharma.glucose.GlucoseApplication;

/**
 * AndroidUtils
 *
 * Created by ivan on 14.04.17.
 */

public class AndroidUtils {

    public static void hideKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public static int dpToPx(Context context, int value){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, context.getResources().getDisplayMetrics());
    }

    public static int dpToPx(int value){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, GlucoseApplication.getInstance().getResources().getDisplayMetrics());
    }

    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
}
