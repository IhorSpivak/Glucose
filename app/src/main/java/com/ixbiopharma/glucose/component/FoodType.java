package com.ixbiopharma.glucose.component;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.ixbiopharma.glucose.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * FoodType
 * <p>
 * Created by Ivan on 13.06.17.
 */

public class FoodType extends FrameLayout {

    @BindView(R.id.f1)
    MealItem breakfast;
    @BindView(R.id.f2)
    MealItem lunch;
    @BindView(R.id.f3)
    MealItem dinner;
    @BindView(R.id.f4)
    MealItem snacks;

    private int selected = -1;
    private boolean isDisabled = false;

    public FoodType(Context context) {
        this(context, null);
    }

    public FoodType(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FoodType(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(getContext(), R.layout.food_type, this);
        ButterKnife.bind(this);

        breakfast.setOnClickListener(v -> {
            if (isDisabled) return;
            if (breakfast.isSelected){
                selected = -1;
                breakfast.setSelection(false);
                lunch.setSelection(false);
                dinner.setSelection(false);
                snacks.setSelection(false);
            } else {
                selected = 1;
                breakfast.setSelection(true);
                lunch.setSelection(false);
                dinner.setSelection(false);
                snacks.setSelection(false);
            }
        });

        lunch.setOnClickListener(v -> {
            if (isDisabled) return;

            if (lunch.isSelected){
                selected = -1;
                breakfast.setSelection(false);
                lunch.setSelection(false);
                dinner.setSelection(false);
                snacks.setSelection(false);
            } else {
                selected = 2;
                breakfast.setSelection(false);
                lunch.setSelection(true);
                dinner.setSelection(false);
                snacks.setSelection(false);
            }
        });

        dinner.setOnClickListener(v -> {
            if (isDisabled) return;

            if (dinner.isSelected){
                selected = -1;
                breakfast.setSelection(false);
                lunch.setSelection(false);
                dinner.setSelection(false);
                snacks.setSelection(false);
            } else {
                selected = 3;
                breakfast.setSelection(false);
                lunch.setSelection(false);
                dinner.setSelection(true);
                snacks.setSelection(false);
            }
        });

        snacks.setOnClickListener(v -> {
            if (isDisabled) return;

            if (snacks.isSelected){
                selected = -1;
                breakfast.setSelection(false);
                lunch.setSelection(false);
                dinner.setSelection(false);
                snacks.setSelection(false);
            } else {
                selected = 4;
                breakfast.setSelection(false);
                lunch.setSelection(false);
                dinner.setSelection(false);
                snacks.setSelection(true);
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
            breakfast.setSelection(true);
            lunch.setSelection(false);
            dinner.setSelection(false);
            snacks.setSelection(false);
        }

        if (id == 2){
            selected = 2;
            breakfast.setSelection(false);
            lunch.setSelection(true);
            dinner.setSelection(false);
            snacks.setSelection(false);
        }

        if (id == 3){
            selected = 3;
            breakfast.setSelection(false);
            lunch.setSelection(false);
            dinner.setSelection(true);
            snacks.setSelection(false);
        }

        if (id == 4){
            selected = 4;
            breakfast.setSelection(false);
            lunch.setSelection(false);
            dinner.setSelection(false);
            snacks.setSelection(true);
        }
    }

}
