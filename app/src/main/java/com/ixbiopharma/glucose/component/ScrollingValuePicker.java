package com.ixbiopharma.glucose.component;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.ixbiopharma.glucose.utils.DWUtils;


public class ScrollingValuePicker extends FrameLayout {

    private View mLeftSpacer;
    private View mRightSpacer;
    public LineRulerView lineRulerView;
    private ObservableHorizontalScrollView mScrollView;
    private float viewMultipleSize = 8.5f;

    private float maxValue = 0;
    private float minValue = 0;

    private float initValue = 0f;

    private int valueMultiple = 1;

    private int valueTypeMultiple = 5;

    public ScrollingValuePicker(Context context) {
        super(context);
        init(context);
    }

    public ScrollingValuePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ScrollingValuePicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ScrollingValuePicker(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public void setOnScrollChangedListener(final ObservableHorizontalScrollView.OnScrollChangedListener onScrollChangedListener) {
        mScrollView.setOnScrollChangedListener(onScrollChangedListener);
    }

    public void setMaxValue(float minValue, float maxValue) {
        setMaxValue(minValue, maxValue, 1);
    }

    public void setMaxValue(float minValue, float maxValue, int valueMultiple) {
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.valueMultiple = valueMultiple;
        lineRulerView.setMaxValue(this.maxValue);
        lineRulerView.setMinValue(this.minValue);
        lineRulerView.setValueMultiple(this.valueMultiple);
    }

    public void setValueTypeMultiple(int valueTypeMultiple) {
        this.valueMultiple = valueTypeMultiple;
        lineRulerView.setMultipleTypeValue(valueTypeMultiple);
    }

    public void setViewMultipleSize(float size) {
        this.viewMultipleSize = size;
    }

    public void setInitValue(float initValue) {
        this.initValue = initValue;
    }

    public float getViewMultipleSize() {
        return this.viewMultipleSize;
    }


    private void init(Context context) {
        mScrollView = new ObservableHorizontalScrollView(context);
        mScrollView.setHorizontalScrollBarEnabled(false);
        addView(mScrollView);

        final LinearLayout container = new LinearLayout(context);
        mScrollView.addView(container);

        mLeftSpacer = new View(context);
        mRightSpacer = new View(context);

        lineRulerView = new LineRulerView(context);
        container.addView(lineRulerView);
        container.addView(mLeftSpacer, 0);
        container.addView(mRightSpacer);


        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (getWidth() != 0) {
                    DWUtils.scrollToValue(getScrollView(), initValue, maxValue, minValue, viewMultipleSize);
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });

    }

    public ObservableHorizontalScrollView getScrollView() {
        return mScrollView;
    }

    private Paint paint = new Paint() {{
        setColor(Color.parseColor("#ffffff"));
        setStrokeWidth(5f);
        setStyle(Paint.Style.FILL_AND_STROKE);
    }};

    private Path path = new Path() {{
        moveTo(getWidth() / 2 - 30, 0);
        lineTo(getWidth() / 2, 40);
        lineTo(getWidth() / 2 + 30, 0);
    }};

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPath(path, paint);
        super.onDraw(canvas);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        if (changed) {

            final int width = getWidth();

            final ViewGroup.LayoutParams leftParams = mLeftSpacer.getLayoutParams();
            leftParams.width = width / 2;
            mLeftSpacer.setLayoutParams(leftParams);

            final ViewGroup.LayoutParams rulerViewParams = lineRulerView.getLayoutParams();
            rulerViewParams.width = (int) (width * viewMultipleSize);  // set RulerView Width
            lineRulerView.setLayoutParams(rulerViewParams);
            lineRulerView.invalidate();


            final ViewGroup.LayoutParams rightParams = mRightSpacer.getLayoutParams();
            rightParams.width = width / 2;
            mRightSpacer.setLayoutParams(rightParams);

            invalidate();

        }
    }

    @Override
    public boolean hasOverlappingRendering() {
        return false;
    }

}
