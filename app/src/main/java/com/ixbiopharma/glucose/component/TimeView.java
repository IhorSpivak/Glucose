package com.ixbiopharma.glucose.component;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ixbiopharma.glucose.R;
import com.ixbiopharma.glucose.utils.TimeUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Time view
 * <p>
 * Created by ivan on 23.03.17.
 */

public class TimeView extends FrameLayout {

    private int myYear;
    private int myMonth;
    private int myDay;

    private int myHour;
    private int myMinute;
    private final int DIALOG_DATE = 1;
    private final int TIME_DATE = 2;

    private boolean isDisabled = false;

    public TimeView(Context context) {
        this(context, null);
    }

    public TimeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @BindView(R.id.dateI)
    View dateI;
    @BindView(R.id.timeI)
    View timeI;
    @BindView(R.id.dateT)
    TextView dateT;
    @BindView(R.id.timeT)
    TextView timeT;


    public TimeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(getContext(), R.layout.time_view, this);
        ButterKnife.bind(this);

        setTime(Calendar.getInstance());

        dateI.setOnClickListener(view -> createDialog(DIALOG_DATE));
        dateT.setOnClickListener(view -> createDialog(DIALOG_DATE));

        timeI.setOnClickListener(view -> createDialog(TIME_DATE));
        timeT.setOnClickListener(view -> createDialog(TIME_DATE));
    }

    public void setTime(Calendar calendar) {
        myYear = calendar.get(Calendar.YEAR);
        myMonth = calendar.get(Calendar.MONTH);
        myDay = calendar.get(Calendar.DAY_OF_MONTH);
        myHour = calendar.get(Calendar.HOUR_OF_DAY);
        myMinute = calendar.get(Calendar.MINUTE);

        timeT.setText(TimeUtils.dateToTimeFormat(calendar.getTime()));
        dateT.setText(TimeUtils.dateToDateFormat(calendar.getTime()));
    }

    public long getTime() {
        Calendar c = Calendar.getInstance();
        c.set(myYear, myMonth, myDay, myHour, myMinute);
        return c.getTime().getTime();
    }

    private void createDialog(int id) {
        if (isDisabled) return;

        if (id == DIALOG_DATE) {
            new DatePickerDialog(
                    getContext(),
                    myCallBack1,
                    myYear,
                    myMonth,
                    myDay).show();
        } else {
            new TimePickerDialog(
                    getContext(),
                    myCallBack2,
                    myHour,
                    myMinute,
                    true).show();
        }
    }

    private void setCalTime() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf =
                new SimpleDateFormat("dd MM yyyy HH:mm", Locale.getDefault());

        try {
            cal.setTime(
                    sdf.parse(
                            myDay + " " + myMonth + " " +
                                    myYear + " " + myHour + ":" + myMinute));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        setTime(cal);
    }

    private TimePickerDialog.OnTimeSetListener myCallBack2 = (view, hourOfDay, minute) -> {
        myHour = hourOfDay;
        myMinute = minute;

        myMonth++;

        setCalTime();
    };

    private DatePickerDialog.OnDateSetListener myCallBack1
            = (view, year, monthOfYear, dayOfMonth) -> {

        myYear = year;
        myMonth = monthOfYear;
        myDay = dayOfMonth;

        myMonth++;

        setCalTime();
    };

    @Override
    public boolean hasOverlappingRendering() {
        return false;
    }

    public void disable() {
        isDisabled = true;
    }
}
