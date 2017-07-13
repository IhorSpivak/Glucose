package com.ixbiopharma.glucose.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.ixbiopharma.glucose.R;

/**
 * Wave view
 * <p>
 * Created by ivan on 17.04.17.
 */

public class SinWaveView extends View {

    private Paint firstWaveColor;
    private int amplitude = 80;
    private Path firstWavePath = new Path();

    public SinWaveView(Context context) {
        this(context, null);
    }

    public SinWaveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray attributes = getContext().obtainStyledAttributes(attrs, R.styleable.wave, 0, 0);
            int height = attributes.getInt(R.styleable.wave_wave_height, 8);
            if (height > 10) {
                amplitude = 50;
            } else {
                amplitude = height * 20;
            }

            attributes.recycle();
        }

        firstWaveColor = new Paint();
        firstWaveColor.setAntiAlias(true);
        firstWaveColor.setStrokeWidth(5);
        firstWaveColor.setColor(ContextCompat.getColor(getContext(), R.color.light_gray));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int quadrant = getHeight() / 2;

        int width = canvas.getWidth();

        firstWavePath.moveTo(0, getHeight());

        for (int i = 0; i < width + 5; i = i + 5) {
            float x = (float) i;

            float shift = 0;
            int frequency = 90;
            float y1 = quadrant + amplitude * (float) Math.sin(((i + 5) * Math.PI / frequency) + shift);
            canvas.drawLine(x, y1, x + 3, y1 + 5, firstWaveColor);
        }

    }

    @Override
    public boolean hasOverlappingRendering() {
        return false;
    }
}