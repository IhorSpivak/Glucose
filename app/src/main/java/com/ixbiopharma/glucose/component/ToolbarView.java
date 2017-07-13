package com.ixbiopharma.glucose.component;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ixbiopharma.glucose.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * ToolbarView
 * <p>
 * Created by ivan on 22.04.17.
 */

public class ToolbarView extends FrameLayout {

    @BindView(R.id.imageViewHome)
    ImageView imageViewHome;

    @BindView(R.id.textViewTitle)
    TextView textViewTitle;

    @BindView(R.id.textViewSave)
    TextView textViewSave;

    @BindView(R.id.textViewCancel)
    TextView textViewCancel;

    public ToolbarView(Context context) {
        this(context, null);
    }

    public ToolbarView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ToolbarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(getContext(), R.layout.view_toolbar, this);
        ButterKnife.bind(this);
        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    public void showHomeArrow(boolean isVisible) {
        if (isVisible) {
            imageViewHome.setVisibility(VISIBLE);
            textViewCancel.setVisibility(GONE);
        } else {
            imageViewHome.setVisibility(GONE);
            textViewCancel.setVisibility(VISIBLE);
        }
    }

    public void hideHome(){
        imageViewHome.setVisibility(GONE);
        textViewCancel.setVisibility(GONE);

    }

    public void setActionTitle(String title){
        textViewSave.setText(title);
    }
    public void showAction(boolean isVisible){
        if (isVisible){
            textViewSave.setVisibility(VISIBLE);
        } else {
            textViewSave.setVisibility(GONE);

        }
    }

    public void setActionEnabled(boolean isEnabled){
        textViewSave.setEnabled(isEnabled);
    }

    public void setHomeArrowIcon(int icon){
        imageViewHome.setImageResource(icon);
    }

    public void setTitle(String title) {
        textViewTitle.setText(title);
    }

    public void setActionOnClickListener(View.OnClickListener listener) {
        textViewSave.setOnClickListener(listener);
    }

    public void setHomeClickListener(View.OnClickListener listener) {
        textViewCancel.setOnClickListener(listener);
        imageViewHome.setOnClickListener(listener);
    }

    @Override
    public boolean hasOverlappingRendering() {
        return false;
    }
}
