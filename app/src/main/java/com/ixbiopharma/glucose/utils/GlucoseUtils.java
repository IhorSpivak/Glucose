package com.ixbiopharma.glucose.utils;

import com.ixbiopharma.glucose.R;

/**
 * GlucoseUtils
 *
 * Created by ivan on 07.05.17.
 */

public class GlucoseUtils {

    public static int getValueColor(float value){
        int color;

        if (value <= 7 && value >= 4) {
            color = R.color.color_blue;
        } else {
            color = R.color.color_red;
        }

        return color;
    }

    static int getGlucoseColor(){
        return R.color.color_blue;
    }

    static int getGlucoseIcon(){
        return R.mipmap.ic_glucose;
    }
}
