package com.ixbiopharma.glucose.component;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ixbiopharma.glucose.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * * LinearAlert
 * <p>
 * Created by ivan on 23.03.17.
 */

public class LinearAlertError extends LinearLayout {
    public LinearAlertError(Context context) {
        this(context, null);
    }

    public LinearAlertError(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @BindView(R.id.title)
    TextView textViewTitle;

    @BindView(R.id.close)
    ImageView close;

    @BindView(R.id.image)
    ImageView imageView;

    @BindView(R.id.description)
    TextView description;

    public LinearAlertError(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(getContext(), R.layout.alert_error, this);
        ButterKnife.bind(this);
        close.setOnClickListener(v -> setVisibility(View.GONE));

    }

    public void setDescriptionText(String text) {
        description.setText(text);
    }

    @Override
    public boolean hasOverlappingRendering() {
        return false;
    }
}
