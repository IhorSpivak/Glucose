package com.ixbiopharma.glucose.progress;

import android.app.Fragment;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.ixbiopharma.glucose.R;
import com.ixbiopharma.glucose.component.LineView;
import com.ixbiopharma.glucose.di.RepositoryComponent;
import com.ixbiopharma.glucose.repository.storage.AppPrefs;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * ProgressFragment
 * <p>
 * Created by ivan on 23.04.17.
 */

public class ProgressFragment extends Fragment
        implements ProgressContract.View, View.OnTouchListener {


    @BindView(R.id.line_view_float)
    LineView lineView;

    @BindView(R.id.middle_record)
    View middle_record;

    @BindView(R.id.high_type)
    TextView high_type;

    @BindView(R.id.average_type)
    TextView average_type;

    @BindView(R.id.lowest_type)
    TextView lowest_type;

    @BindView(R.id.high_value)
    TextView high_value;

    @BindView(R.id.average_value)
    TextView average_value;

    @BindView(R.id.lowest_value)
    TextView lowest_value;

    @BindView(R.id.next)
    View next;

    @BindView(R.id.prev)
    View prev;

    @BindView(R.id.text_next)
    TextView text_next;

    @BindView(R.id.text_prev)
    TextView text_prev;

    @BindView(R.id.text_cur)
    TextView text_cur;

    @BindView(R.id.info)
    View info;

    @BindView(R.id.info_day)
    View info_day;

    @BindView(R.id.move)
    View move;

    public static int YEAR = 0;
    public static int WEEK = 1;
    public static int DAY = 2;

    public static int GLUCOSE = 3;
    public static int WEIGHT = 4;
    public static int ACTIVITY = 5;

    private static String TIME = "TIME";
    private static String TYPE = "TYPE";

    private int time;
    private Calendar calendar;
    private ProgressContract.Presenter presenter;
    private float yCoOrdinate;
    private AppPrefs appPrefs;
    private int type;

    public static ProgressFragment newInstance(int time, int type) {

        Bundle args = new Bundle();

        args.putInt(TIME, time);
        args.putInt(TYPE, type);

        ProgressFragment fragment = new ProgressFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_progress, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        type = getArguments().getInt(TYPE, WEIGHT);
        appPrefs = AppPrefs.create(getActivity());
        presenter = new ProgressPresenter(this, type,
                RepositoryComponent.provideJournalRepository(),
                RepositoryComponent.provideExerciseRepository(),
                RepositoryComponent.provideFWeightRepository(),
                RepositoryComponent.provideGlucoseRepository());

        time = getArguments().getInt(TIME, DAY);


        if (type == WEIGHT) {
            String kg = "Kg";
            high_type.setText(kg);
            average_type.setText(kg);
            lowest_type.setText(kg);
        } else if (type == GLUCOSE) {
            //todo type
            String mmol = "mmol/L";
            high_type.setText(mmol);
            average_type.setText(mmol);
            lowest_type.setText(mmol);
        } else {
            String min = "Mins";
            high_type.setText(min);
            average_type.setText(min);
            lowest_type.setText(min);
        }

        if (time == YEAR) {
            middle_record.setVisibility(View.GONE);
        }

        if (time == DAY) {
            info.setVisibility(View.GONE);
            info_day.setVisibility(View.VISIBLE);
        } else {
            info.setVisibility(View.VISIBLE);
            info_day.setVisibility(View.GONE);
        }

        calendar = Calendar.getInstance();

        initLineView(time);
        randomSet(time, calendar);

        next.setOnClickListener(v -> change(1, calendar));

        prev.setOnClickListener(v -> change(-1, calendar));

        move.setOnTouchListener(this);

        lineView.invalidate();
    }

    private void setGoalValue(float value) {
        if (type == WEIGHT) {
            appPrefs.setRatingGoalWeight(value);
        } else if (type == ACTIVITY) {
            appPrefs.setRatingGoalActivity(value);
        } else {
            appPrefs.setRatingGoalGlucose(value);
        }
    }

    private float getGoalValue() {
        if (type == WEIGHT) {
            return appPrefs.getRatingGoalWeight();
        } else if (type == ACTIVITY) {
            return appPrefs.getRatingGoalActivity();
        } else {
            return appPrefs.getRatingGoalGlucose();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getGoalValue() != 0) {
            Display display = getActivity().getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);


            ViewTreeObserver vto = move.getViewTreeObserver();

            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    move.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    float value = getGoalValue();

                    if (value > lineView.getTop() && value < lineView.getBottom()) {
                        move.setY(value);
                    }
                }
            });
        }
    }

    private void change(int i, Calendar calendar) {

        int type;
        int k = 1;

        if (time == WEEK) {
            type = Calendar.DAY_OF_MONTH;
            k = 7;
        } else if (time == YEAR) {
            type = Calendar.YEAR;
        } else {
            type = Calendar.DAY_OF_MONTH;
        }

        calendar.add(type, k * i);
        randomSet(time, calendar);
    }

    private void randomSet(int time, Calendar cal) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(cal.getTime());

        if (time == YEAR) {
            int year = calendar.get(Calendar.YEAR);
            text_cur.setText(String.valueOf(year));
            text_prev.setText(String.valueOf(year - 1));
            text_next.setText(String.valueOf(year + 1));
            presenter.getYearData(year);

        } else if (time == WEEK) {

            String dd_MMM = "dd MMM";
            String firstCur = getDate(calendar, 0, dd_MMM);
            int first = getDateFirst(calendar);

            String lastCur = getDate(calendar, 6, dd_MMM);

            String dd_MM = "dd/MM";
            String firstPrev = getDate(calendar, -7, dd_MM);
            String lastPrev = getDate(calendar, -6, dd_MM);
            String firstNext = getDate(calendar, 14, dd_MM);
            String lastNext = getDate(calendar, 6, dd_MM);

            presenter.getWeekData(calendar);

            text_cur.setText(String.format("%s - %s", firstCur, lastCur));
            text_prev.setText(String.format("%s - %s", lastPrev, firstPrev));
            text_next.setText(String.format("%s - %s", firstNext, lastNext));

            ArrayList<String> test = new ArrayList<>();

            test.add(String.valueOf(first));
            for (int i = 0; i < 6; i++) {
                first++;

                if (first == 32) {
                    first = 1;
                }
                Log.e("first", String.valueOf(first));

                test.add(String.valueOf(first));
            }


            lineView.setBottomTextList(test);

        } else {

            String month = new SimpleDateFormat("MMM", Locale.getDefault()).format(calendar.getTime());
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            presenter.getDayData(calendar);

            text_cur.setText(String.format(Locale.getDefault(), "%d %s", day, month));
            text_prev.setText(String.format(Locale.getDefault(), "%d %s", day - 1, month));
            text_next.setText(String.format(Locale.getDefault(), "%d %s", day + 1, month));
        }
    }

    private String getDate(Calendar calendar, int days, String format) {
        calendar.add(Calendar.DAY_OF_MONTH, days);
        Date firstDateOfPreviousWeek = calendar.getTime();
        return new SimpleDateFormat(format, Locale.getDefault()).format(firstDateOfPreviousWeek);
    }

    private int getDateFirst(Calendar calendar) {
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    private void initLineView(int type) {
        ArrayList<String> test = new ArrayList<>();


        if (type == DAY) {
            test.add("12AM");
            test.add("6AM");
            test.add("12PM");
            test.add("6PM");

        } else if (type == WEEK) {
            test.add("25");
            test.add("26");
            test.add("27");
            test.add("28");
            test.add("29");
            test.add("30");
            test.add("1");

        } else {
            test.add("J");
            test.add("F");
            test.add("M");
            test.add("A");
            test.add("M");
            test.add("J");
            test.add("J");
            test.add("A");
            test.add("S");
            test.add("O");
            test.add("N");
            test.add("D");

        }

        lineView.setBottomTextList(test);

        if (time == DAY) {
            lineView.setColorArray(
                    new int[]{
                            Color.parseColor("#24cde7"),
                            Color.parseColor("#24cde7")
                    });
        } else {
            lineView.setColorArray(
                    new int[]{
                            Color.parseColor("#F44336"),
                            Color.parseColor("#FA7C16")
                    });
        }


        lineView.setDrawDotLine(true);
    }

    @Override
    public void onDataSetsLoaded(ArrayList<ArrayList<Float>> dataListFs) {

        lineView.setFloatDataList(dataListFs);
        lineView.invalidate();
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                yCoOrdinate = view.getY() - event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                float value = event.getRawY() + yCoOrdinate;
                if (value > lineView.getTop() && value < lineView.getBottom()) {
                    move.setY(value);
                    setGoalValue(move.getY());
                }
                break;
            default:
                return false;
        }
        return true;
    }

    @Override
    public void setGrapthData(String a1, String a2, String a3, int i1, int i2) {
        high_value.setText(a1);
        lowest_value.setText(a2);
        average_value.setText(a3);

        lineView.minTitle = i1;
        lineView.maxTitle = i2;
    }
}