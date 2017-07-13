package com.ixbiopharma.glucose.component;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ixbiopharma.glucose.R;
import com.ixbiopharma.glucose.model.api.DayAdviceResponse;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * * LinearAlert
 * <p>
 * Created by ivan on 23.03.17.
 */

public class LinearAlert extends LinearLayout {
    public LinearAlert(Context context) {
        this(context, null);
    }

    public LinearAlert(Context context, @Nullable AttributeSet attrs) {
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

    public LinearAlert(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(getContext(), R.layout.alert, this);
        ButterKnife.bind(this);
        close.setOnClickListener(v -> setVisibility(View.GONE));
    }

    public void setOnCloseClickListener(OnClickListener listener){
        close.setOnClickListener(listener);
    }

    public void setDescriptionText(String text) {
        description.setText(text);
    }

    public void setDescriptionText(String name, DayAdviceResponse dayAdviceResponse) {
        Calendar c = Calendar.getInstance();

        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if (timeOfDay >= 0 && timeOfDay < 12) {
            textViewTitle.setText(String.format("Good Morning %s!", name));
            imageView.setImageResource(R.drawable.ic_sun);
            description.setText(dayAdviceResponse.getMorning());
        } else if (timeOfDay >= 12 && timeOfDay < 18) {
            textViewTitle.setText(String.format("Good Afternoon %s!", name));
            imageView.setImageResource(R.drawable.afternoon);
            description.setText(dayAdviceResponse.getDay());
        } else {
            textViewTitle.setText(String.format("Good Evening %s!", name));
            imageView.setImageResource(R.drawable.evening);
            description.setText(dayAdviceResponse.getEvening());

        }
    }

    public void setTitle(String text){
        textViewTitle.setText(text);
    }

    public void setIcon(int icon){
        imageView.setImageResource(icon);
    }

    @Override
    public boolean hasOverlappingRendering() {
        return false;
    }
}
