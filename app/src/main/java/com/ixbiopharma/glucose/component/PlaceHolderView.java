package com.ixbiopharma.glucose.component;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;

import com.ixbiopharma.glucose.R;

/**
 * PlaceHolderView
 *
 * Created by ivan on 17.04.17.
 */

public class PlaceHolderView extends LinearLayout {
    public PlaceHolderView(Context context) {
        this(context, null);
    }

    public PlaceHolderView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PlaceHolderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER);
        setBackgroundColor(ContextCompat.getColor(context, R.color.color_blue));
        inflate(getContext(), R.layout.view_place_holder, this);

        setOnClickListener(v -> PlaceHolderView.this.setVisibility(GONE));
    }

    @Override
    public boolean hasOverlappingRendering() {
        return false;
    }
}
