package com.ixbiopharma.glucose.component;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

/**
 * MealGroup
 * <p>
 * Created by ivan on 02.04.17.
 */

public class MealGroup extends LinearLayout {

    private int id = -1;

    public MealGroup(Context context) {
        this(context, null);
    }

    public MealGroup(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MealGroup(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        id = -1;
    }

    public int getSelected() {
        Log.e("id", id + "");

        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child.getId() != id) {
                return i + 1;
            }
        }

        throw new IllegalArgumentException("SELECTED NOT FOUND");

    }

    public void setSelected(int position) {
        Log.e("setSelected", position + "");

        id = getChildAt(position - 1).getId();
        updateItems(id);
    }


    public void updateItems(int id) {
        this.id = id;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child.getId() != id) {
                ((MealItem) child).setSelection(false);
            } else {
                ((MealItem) child).setSelection(true);
            }
        }
    }

    @Override
    public boolean hasOverlappingRendering() {
        return false;
    }
}
