package com.ixbiopharma.glucose.component;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ixbiopharma.glucose.R;
import com.kyleduo.switchbutton.SwitchButton;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * SettingsSwitchItem
 * <p>
 * Created by ivan on 14.05.17.
 */

public class SettingsSwitchItem extends LinearLayout {

    @BindView(R.id.divider)
    View divider;
    @BindView(R.id.text)
    TextView text;
    @BindView(R.id.switch_view)
    SwitchButton aSwitch;
    @BindView(R.id.content)
    View content;

    public SettingsSwitchItem(Context context) {
        this(context, null);
    }

    public SettingsSwitchItem(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SettingsSwitchItem(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(getContext(), R.layout.view_settings_switch_item, this);
        ButterKnife.bind(this);

        TypedArray ta = context.obtainStyledAttributes(
                attrs, R.styleable.SettingsItem,
                0, 0);

        try {
            String switch1 = ta.getString(R.styleable.SettingsItem_settings_switch1);
            String switch2 = ta.getString(R.styleable.SettingsItem_settings_switch2);

            int def = ta.getInt(R.styleable.SettingsItem_settings_switch_default, 0);
            aSwitch.setChecked(def == 1);

            if (switch1 == null || switch2 == null) {
                ColorStateList switchColorState = new ColorStateList(
                        new int[][]{
                                new int[]{android.R.attr.state_checked},
                                new int[]{-android.R.attr.state_enabled},
                                new int[]{}
                        },
                        new int[]{
                                ContextCompat.getColor(context, R.color.switch_green),
                                Color.LTGRAY,
                                Color.LTGRAY
                        }
                );

                aSwitch.setBackColor(switchColorState);

            } else {
                aSwitch.setTextColor(ContextCompat.getColor(context, android.R.color.white));
                aSwitch.setBackColorRes(R.color.color_blue);
                aSwitch.setText(switch1, switch2);
            }

            text.setText(ta.getString(R.styleable.SettingsItem_settings_text));
            boolean isDivider = ta.getBoolean(R.styleable.SettingsItem_settings_divider, true);

            if (isDivider) {
                divider.setVisibility(VISIBLE);
            } else {
                divider.setVisibility(GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ta.recycle();
        }

        ColorStateList switchColorState = new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_checked},
                        new int[]{-android.R.attr.state_enabled},
                        new int[]{}
                },
                new int[]{
                        Color.WHITE,
                        Color.WHITE,
                        Color.WHITE
                }
        );

        aSwitch.setThumbColor(switchColorState);
        content.setOnClickListener(v -> aSwitch.setChecked(!aSwitch.isChecked()));
    }

    public void setOnCheckListener(CompoundButton.OnCheckedChangeListener listener) {
        aSwitch.setOnCheckedChangeListener(listener);
    }

    public int getCheckedItem() {
        return aSwitch.isChecked() ? 1 : 0;
    }

    public void setCheckedItem(int item) {
        aSwitch.setChecked(item == 1);
    }
}
