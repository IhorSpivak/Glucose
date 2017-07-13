package com.ixbiopharma.glucose.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ixbiopharma.glucose.R;

/**
 * MealItem
 *
 * Created by ivan on 02.04.17.
 */

public class MealItem extends FrameLayout {
    public MealItem(@NonNull Context context) {
        this(context, null);
    }

    public MealItem(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public boolean isSelected = false;
    private TextView textView;
    private ImageView imageView;

    private int defColor;
    private int selectedColor;

    public MealItem(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(getContext(), R.layout.meal_item, this);

        setClickable(true);
        String text = "";

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MealItem, 0, 0);
        int icon;
        try {
            text = ta.getString(R.styleable.MealItem_text);
            icon = ta.getResourceId(R.styleable.MealItem_iconRes, R.mipmap.ic_excersice);
            defColor = ta.getColor(R.styleable.MealItem_defaultColorRes, getResources().getColor(R.color.light_gray3));
            selectedColor = ta.getColor(R.styleable.MealItem_selectedColorRes, getResources().getColor(R.color.color_purple));
        } finally {
            ta.recycle();
        }

        textView = (TextView) findViewById(R.id.meal_text);
        textView.setText(text);
        Typeface face = Typeface.createFromAsset(context.getAssets(),
                "fonts/OpenSans-Regular.ttf");
        textView.setTypeface(face);

        textView.setTextColor(defColor);

        imageView = (ImageView) findViewById(R.id.meal_image);
        imageView.setImageResource(icon);
        imageView.setColorFilter(defColor, PorterDuff.Mode.SRC_IN);
        if (getId() == -1) setId(generateViewId());
    }

    public void setSelection(boolean isSelected){
        this.isSelected = isSelected;

        if (isSelected){
            textView.setTextColor(selectedColor);
            imageView.setColorFilter(selectedColor, PorterDuff.Mode.SRC_IN);
        } else {
            textView.setTextColor(defColor);
            imageView.setColorFilter(defColor, PorterDuff.Mode.SRC_IN);
        }
    }

    public String getText(){
        return textView.getText().toString();
    }

    @Override
    public boolean hasOverlappingRendering() {
        return false;
    }
}
