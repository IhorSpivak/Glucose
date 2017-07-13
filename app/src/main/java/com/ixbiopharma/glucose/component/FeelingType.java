package com.ixbiopharma.glucose.component;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.ixbiopharma.glucose.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * FeelingType
 *
 * Created by Ivan on 13.06.17.
 */

public class FeelingType extends FrameLayout {

    @BindView(R.id.f1)
    MealItem happy;
    @BindView(R.id.f2)
    MealItem excited;
    @BindView(R.id.f3)
    MealItem relaxed;
    @BindView(R.id.f4)
    MealItem tired;

    private int selected = -1;
    private boolean isDisabled = false;

    public FeelingType(Context context) {
        this(context, null);
    }

    public FeelingType(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FeelingType(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(getContext(), R.layout.feeling_type, this);
        ButterKnife.bind(this);

        happy.setOnClickListener(v -> {
            if (isDisabled) return;
            if (happy.isSelected){
                selected = -1;
                happy.setSelection(false);
                excited.setSelection(false);
                relaxed.setSelection(false);
                tired.setSelection(false);
            } else {
                selected = 1;
                happy.setSelection(true);
                excited.setSelection(false);
                relaxed.setSelection(false);
                tired.setSelection(false);
            }
        });

        excited.setOnClickListener(v -> {
            if (isDisabled) return;

            if (happy.isSelected){
                selected = -1;
                happy.setSelection(false);
                excited.setSelection(false);
                relaxed.setSelection(false);
                tired.setSelection(false);
            } else {
                selected = 2;
                happy.setSelection(false);
                excited.setSelection(true);
                relaxed.setSelection(false);
                tired.setSelection(false);
            }
        });

        relaxed.setOnClickListener(v -> {
            if (isDisabled) return;

            if (happy.isSelected){
                selected = -1;
                happy.setSelection(false);
                excited.setSelection(false);
                relaxed.setSelection(false);
                tired.setSelection(false);
            } else {
                selected = 3;
                happy.setSelection(false);
                excited.setSelection(false);
                relaxed.setSelection(true);
                tired.setSelection(false);
            }
        });

        tired.setOnClickListener(v -> {
            if (isDisabled) return;

            if (happy.isSelected){
                selected = -1;
                happy.setSelection(false);
                excited.setSelection(false);
                relaxed.setSelection(false);
                tired.setSelection(false);
            } else {
                selected = 4;
                happy.setSelection(false);
                excited.setSelection(false);
                relaxed.setSelection(false);
                tired.setSelection(true);
            }
        });
    }

    public int getSelected(){
        return selected;
    }

    public void setDisabled(boolean disabled){
        isDisabled = disabled;
    }

    public void setSelected(int id){
        if (id == 1){
            selected = 1;
            happy.setSelection(true);
            excited.setSelection(false);
            relaxed.setSelection(false);
            tired.setSelection(false);
        }

        if (id == 2){
            selected = 2;
            happy.setSelection(false);
            excited.setSelection(true);
            relaxed.setSelection(false);
            tired.setSelection(false);
        }

        if (id == 3){
            selected = 3;
            happy.setSelection(false);
            excited.setSelection(false);
            relaxed.setSelection(true);
            tired.setSelection(false);
        }

        if (id == 4){
            selected = 4;
            happy.setSelection(false);
            excited.setSelection(false);
            relaxed.setSelection(false);
            tired.setSelection(true);
        }
    }

}
