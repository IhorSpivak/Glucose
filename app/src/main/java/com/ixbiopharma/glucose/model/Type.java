package com.ixbiopharma.glucose.model;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.ixbiopharma.glucose.model.Type.ADVICE;
import static com.ixbiopharma.glucose.model.Type.EXERCISE;
import static com.ixbiopharma.glucose.model.Type.FOOD;
import static com.ixbiopharma.glucose.model.Type.GLUCOSE;
import static com.ixbiopharma.glucose.model.Type.NEWS;
import static com.ixbiopharma.glucose.model.Type.WIGHT;

/**
 * Type
 *
 * Created by ivan on 11.04.17.
 */

@Retention(RetentionPolicy.SOURCE)
@IntDef({GLUCOSE, WIGHT, EXERCISE, FOOD, NEWS, ADVICE})
public @interface Type {

    int GLUCOSE = 0;
    int WIGHT = 1;
    int EXERCISE = 2;
    int FOOD = 3;
    int ADVICE = 4;
    int NEWS = 5;
}
