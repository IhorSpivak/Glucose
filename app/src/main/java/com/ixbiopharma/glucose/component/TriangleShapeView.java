package com.ixbiopharma.glucose.component;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.ixbiopharma.glucose.R;

/**
 * TriangleShapeView
 * <p>
 * Created by ivan on 01.05.17.
 */

public class TriangleShapeView extends View {

    public TriangleShapeView(Context context) {
        super(context);
    }

    public TriangleShapeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public TriangleShapeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }




    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Path path = new Path();
        Paint p = new Paint();

        int w = getWidth() / 2;


        path.moveTo(w, 0);
        path.lineTo(2 * w, 0);
        path.lineTo(2 * w, w);
        path.lineTo(w, 0);
        path.close();

        p.setColor(ContextCompat.getColor(getContext(), R.color.color_red));

        canvas.drawPath(path, p);
    }
}
