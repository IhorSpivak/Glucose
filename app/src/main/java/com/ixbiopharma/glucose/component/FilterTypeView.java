package com.ixbiopharma.glucose.component;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ixbiopharma.glucose.R;
import com.ixbiopharma.glucose.utils.AndroidUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Filter type view
 * <p>
 * Created by ivan on 17.04.17.
 */

public class FilterTypeView extends LinearLayout {

    public FilterTypeView(Context context) {
        this(context, null);
    }

    public FilterTypeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    private Listener listener;

    @BindView(R.id.all_type)
    TextView all_type;
    @BindView(R.id.food_type)
    ImageView food_type;
    @BindView(R.id.glucose_type)
    ImageView glucose_type;
    @BindView(R.id.weight_type)
    ImageView weight_type;
    @BindView(R.id.exercise_type)
    ImageView exercise_type;

    public FilterTypeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(getContext(), R.layout.view_filter_type, this);
        ButterKnife.bind(this);

        int padding = AndroidUtils.dpToPx(context, 10);
        setPadding(padding, padding, padding, padding);
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER);
    }

    @OnClick(R.id.all_type)
    void all_type() {
        listener.onItemSelected(0);
        all_type.setTextColor(ContextCompat.getColor(getContext(), R.color.gray));
        food_type.setColorFilter(ContextCompat.getColor(getContext(), R.color.color_purple));
        glucose_type.setColorFilter(ContextCompat.getColor(getContext(), R.color.color_blue));
        weight_type.setColorFilter(ContextCompat.getColor(getContext(), R.color.color_green));
        exercise_type.setColorFilter(ContextCompat.getColor(getContext(), R.color.color_orange));

    }

    @OnClick(R.id.food_type)
    void food_type() {
        listener.onItemSelected(1);
        all_type.setTextColor(ContextCompat.getColor(getContext(), R.color.light_gray2));

        food_type.setColorFilter(ContextCompat.getColor(getContext(), R.color.color_purple));
        glucose_type.setColorFilter(ContextCompat.getColor(getContext(), R.color.light_gray2));
        weight_type.setColorFilter(ContextCompat.getColor(getContext(), R.color.light_gray2));
        exercise_type.setColorFilter(ContextCompat.getColor(getContext(), R.color.light_gray2));
    }

    @OnClick(R.id.exercise_type)
    void exercise_type() {
        listener.onItemSelected(2);
        all_type.setTextColor(ContextCompat.getColor(getContext(), R.color.light_gray2));

        food_type.setColorFilter(ContextCompat.getColor(getContext(), R.color.light_gray2));
        glucose_type.setColorFilter(ContextCompat.getColor(getContext(), R.color.light_gray2));
        weight_type.setColorFilter(ContextCompat.getColor(getContext(), R.color.light_gray2));
        exercise_type.setColorFilter(ContextCompat.getColor(getContext(), R.color.color_orange));
    }

    @OnClick(R.id.weight_type)
    void weight_type() {
        listener.onItemSelected(3);
        all_type.setTextColor(ContextCompat.getColor(getContext(), R.color.light_gray2));

        food_type.setColorFilter(ContextCompat.getColor(getContext(), R.color.light_gray2));
        glucose_type.setColorFilter(ContextCompat.getColor(getContext(), R.color.light_gray2));
        weight_type.setColorFilter(ContextCompat.getColor(getContext(), R.color.color_green));
        exercise_type.setColorFilter(ContextCompat.getColor(getContext(), R.color.light_gray2));
    }

    @OnClick(R.id.glucose_type)
    void glucose_type() {
        all_type.setTextColor(ContextCompat.getColor(getContext(), R.color.light_gray2));

        listener.onItemSelected(4);
        food_type.setColorFilter(ContextCompat.getColor(getContext(), R.color.light_gray2));
        glucose_type.setColorFilter(ContextCompat.getColor(getContext(), R.color.color_blue));
        weight_type.setColorFilter(ContextCompat.getColor(getContext(), R.color.light_gray2));
        exercise_type.setColorFilter(ContextCompat.getColor(getContext(), R.color.light_gray2));
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public interface Listener {
        void onItemSelected(int position);
    }

    @Override
    public boolean hasOverlappingRendering() {
        return false;
    }
}