package com.ixbiopharma.glucose.component;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ixbiopharma.glucose.utils.AndroidUtils;

/**
 * RoundedLinearLayout
 * <p>
 * Created by ivan on 01.05.17.
 */

public class RoundedRelativeLayout extends RelativeLayout {
    Path mPath;
    float mCornerRadius = AndroidUtils.dpToPx(getContext(), 3);

    public RoundedRelativeLayout(Context context) {
        super(context);
    }

    public RoundedRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RoundedRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.save();
        canvas.clipPath(mPath);
        super.draw(canvas);
        canvas.restore();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        RectF r = new RectF(0, 0, w, h);
        mPath = new Path();
        mPath.addRoundRect(r, mCornerRadius, mCornerRadius, Path.Direction.CW);
        mPath.close();
    }

    public void setCornerRadius(int radius) {
        mCornerRadius = radius;
        invalidate();
    }
}