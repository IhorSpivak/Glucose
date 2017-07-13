package com.ixbiopharma.glucose.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ixbiopharma.glucose.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * SettingsItem
 * <p>
 * Created by ivan on 14.05.17.
 */

public class SettingsItem extends LinearLayout {

    @BindView(R.id.divider)
    View divider;
    @BindView(R.id.text)
    TextView text;
    @BindView(R.id.arrow)
    View arrow;

    public SettingsItem(Context context) {
        this(context, null);
    }

    public SettingsItem(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SettingsItem(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(getContext(), R.layout.view_settings_item, this);
        ButterKnife.bind(this);

        TypedArray ta = context.obtainStyledAttributes(
                attrs, R.styleable.SettingsItem,
                0, 0);

        try {
            text.setText(ta.getString(R.styleable.SettingsItem_settings_text));
            boolean isDivider = ta.getBoolean(R.styleable.SettingsItem_settings_divider, true);

            if (isDivider) {
                divider.setVisibility(VISIBLE);
            } else {
                divider.setVisibility(GONE);
            }

            boolean hideArrow = ta.getBoolean(R.styleable.SettingsItem_settings_hide_arrow, false);

            if (hideArrow){
                arrow.setVisibility(INVISIBLE);
            }

        } finally {
            ta.recycle();
        }
    }

    public void setText(String s){
        text.setText(s);
    }
}
