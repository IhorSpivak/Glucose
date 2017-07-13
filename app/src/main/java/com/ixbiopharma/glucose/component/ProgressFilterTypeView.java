package com.ixbiopharma.glucose.component;

import android.content.Context;
import android.support.annotation.NonNull;
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

public class ProgressFilterTypeView extends LinearLayout {

    public ProgressFilterTypeView(Context context) {
        this(context, null);
    }

    public ProgressFilterTypeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @Nullable
    private Listener listener;

    @BindView(R.id.action_ic)
    ImageView action_ic;
    @BindView(R.id.action_text)
    TextView action_text;

    @BindView(R.id.glucose_ic)
    ImageView glucose_ic;
    @BindView(R.id.glucose_text)
    TextView glucose_text;

    @BindView(R.id.weight_ic)
    ImageView weight_ic;
    @BindView(R.id.weight_text)
    TextView weight_text;


    public ProgressFilterTypeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(getContext(), R.layout.view_progress_type, this);
        int padding = AndroidUtils.dpToPx(context, 10);
        setPadding(padding, padding, padding, padding);
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER_HORIZONTAL);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.action)
    void action() {
        if (listener != null) {
            listener.onItemSelected(2);
        }

        action_ic.setColorFilter(ContextCompat.getColor(getContext(), R.color.color_orange));
        action_text.setTextColor(ContextCompat.getColor(getContext(), R.color.color_orange));

        weight_ic.setColorFilter(ContextCompat.getColor(getContext(), R.color.light_gray2));
        weight_text.setTextColor(ContextCompat.getColor(getContext(), R.color.light_gray2));

        glucose_ic.setColorFilter(ContextCompat.getColor(getContext(), R.color.light_gray2));
        glucose_text.setTextColor(ContextCompat.getColor(getContext(), R.color.light_gray2));
    }

    @OnClick(R.id.weight)
    void weight() {
        if (listener != null) {
            listener.onItemSelected(0);
        }

        weight_ic.setColorFilter(ContextCompat.getColor(getContext(), R.color.color_salad));
        weight_text.setTextColor(ContextCompat.getColor(getContext(), R.color.color_salad));

        action_ic.setColorFilter(ContextCompat.getColor(getContext(), R.color.light_gray2));
        action_text.setTextColor(ContextCompat.getColor(getContext(), R.color.light_gray2));

        glucose_ic.setColorFilter(ContextCompat.getColor(getContext(), R.color.light_gray2));
        glucose_text.setTextColor(ContextCompat.getColor(getContext(), R.color.light_gray2));

    }

    @OnClick(R.id.glucose)
    void glucose() {
        if (listener != null) {
            listener.onItemSelected(1);
        }

        glucose_ic.setColorFilter(ContextCompat.getColor(getContext(), R.color.color_blue));
        glucose_text.setTextColor(ContextCompat.getColor(getContext(), R.color.color_blue));

        action_ic.setColorFilter(ContextCompat.getColor(getContext(), R.color.light_gray2));
        action_text.setTextColor(ContextCompat.getColor(getContext(), R.color.light_gray2));

        weight_ic.setColorFilter(ContextCompat.getColor(getContext(), R.color.light_gray2));
        weight_text.setTextColor(ContextCompat.getColor(getContext(), R.color.light_gray2));

    }


    public void setListener(@NonNull Listener listener) {
        this.listener = listener;
        weight();
    }

    public interface Listener {
        void onItemSelected(int position);
    }

    @Override
    public boolean hasOverlappingRendering() {
        return false;
    }
}