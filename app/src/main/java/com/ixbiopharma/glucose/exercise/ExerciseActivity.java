package com.ixbiopharma.glucose.exercise;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ixbiopharma.glucose.BaseActivity;
import com.ixbiopharma.glucose.ContainerScrollListener;
import com.ixbiopharma.glucose.R;
import com.ixbiopharma.glucose.component.ObservableHorizontalScrollView;
import com.ixbiopharma.glucose.component.PlaceHolderView;
import com.ixbiopharma.glucose.component.ScrollingValuePicker;
import com.ixbiopharma.glucose.component.TimeView;
import com.ixbiopharma.glucose.component.ToolbarView;
import com.ixbiopharma.glucose.di.RepositoryComponent;
import com.ixbiopharma.glucose.model.Exercise;
import com.ixbiopharma.glucose.model.ExerciseType;
import com.ixbiopharma.glucose.utils.AndroidUtils;
import com.ixbiopharma.glucose.utils.ConnectUtils;
import com.ixbiopharma.glucose.utils.DWUtils;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * ExerciseActivity
 * <p>
 * Created by ivan on 22.04.17.
 */
public class ExerciseActivity extends BaseActivity
        implements ExerciseContract.View, FitHelper.Listener {

    private static final float MIN_VALUE = 1;
    private static float MAX_VALUE = 240;

    private int transparent;
    private ExerciseContract.Presenter presenter;

    @BindView(R.id.hint_title)
    TextView hintTitle;

    @BindView(R.id.placeHolder)
    PlaceHolderView placeHolderView;

    @BindView(R.id.autocomplete)
    AutoCompleteTextView mAutoCompleteTextView;

    @BindView(R.id.myScrollingValuePicker)
    ScrollingValuePicker myScrollingValuePicker;

    @BindView(R.id.container)
    RelativeLayout container;

    @BindView(R.id.value)
    TextView valueT;

    @BindView(R.id.timeView)
    TimeView timeView;

    @BindView(R.id.left_grad)
    View left_grad;

    @BindView(R.id.right_grad)
    View right_grad;

    @BindView(R.id.toolbar)
    ToolbarView toolbarView;

    private static String ID = "id";
    private static String IS_EDIT = "isedit";

    public static void editExercise(Activity activity, int id) {
        Intent intent = new Intent(activity, ExerciseActivity.class);
        intent.putExtra(ID, id);
        intent.putExtra(IS_EDIT, true);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        ButterKnife.bind(this);

        int newColor = ContextCompat.getColor(
                ExerciseActivity.this,
                R.color.color_orange);


        int[] colors = {newColor, transparent};

        left_grad.setBackground(
                new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors));

        right_grad.setBackground(
                new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, colors));

        container.setBackgroundColor(newColor);

        toolbarView.showHomeArrow(true);
        toolbarView.setTitle("New Exercise Log");

        if (getIntent().getBooleanExtra(IS_EDIT, false)){
            placeHolderView.setVisibility(View.GONE);
        } else {
            placeHolderView.setVisibility(View.VISIBLE);
        }

        placeHolderView.setBackgroundColor(ContextCompat.getColor(this, R.color.color_orange));

        FitHelper fitHelper = new FitHelper<>(ExerciseActivity.this);

        placeHolderView.setOnClickListener(v -> placeHolderView.setVisibility(View.GONE));

        hintTitle.setText("Exercise");

        transparent = ContextCompat.getColor(this, android.R.color.transparent);

        mAutoCompleteTextView.setOnItemClickListener(
                (parent, view, position, id) ->
                        AndroidUtils.hideKeyboard(ExerciseActivity.this));

        mAutoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                toolbarView.setActionEnabled(s.length() != 0);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mAutoCompleteTextView.getText().clear();
        toolbarView.setActionEnabled(false);

        toolbarView.setHomeClickListener(v -> finish());
        toolbarView.setActionOnClickListener(v -> {
            long time = timeView.getTime();

            presenter.save(
                    mAutoCompleteTextView.getText().toString(),
                    Integer.parseInt(valueT.getText().toString()),
                    time);
        });

        myScrollingValuePicker.setOnScrollChangedListener(
                new ObservableHorizontalScrollView.OnScrollChangedListener() {

                    @Override
                    public void onScrollChanged(ObservableHorizontalScrollView view, int l, int t) {

                        int value = DWUtils.getValueAndScrollItemToCenter(
                                myScrollingValuePicker.getScrollView(),
                                l, t, MAX_VALUE, MIN_VALUE,
                                myScrollingValuePicker.getViewMultipleSize());

                        valueT.setText(String.valueOf(value));
                    }

                    @Override
                    public void onScrollStopped(int l, int t) {

                    }
                });

        ContainerScrollListener containerScrollListener =
                new ContainerScrollListener(myScrollingValuePicker);
        container.setOnTouchListener(containerScrollListener);

        presenter = new ExercisePresenter(this,
                getIntent().getIntExtra(ID, -1),
                RepositoryComponent.provideExerciseRepository());

        setInitValue(50);
    }

    boolean seted = false;

    @Override
    public void showLoading() {
        progressDialog.show();
    }

    @Override
    public void hideLoading() {
        progressDialog.hide();
    }

    @Override
    public void onSaved() {
        Toast.makeText(ExerciseActivity.this, "Saved", Toast.LENGTH_SHORT).show();

        finish();
    }

    @Override
    public void setInitValue(float value) {
        if (!seted) {

            if (MAX_VALUE < value) {
                MAX_VALUE = value * 1.5f;
            }

            myScrollingValuePicker.setValueTypeMultiple(10);

            myScrollingValuePicker.setMaxValue(MIN_VALUE, MAX_VALUE);
            myScrollingValuePicker.lineRulerView.kef = 1;

            myScrollingValuePicker.setInitValue(value);

            seted = true;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destroy();
    }

    @Override
    public void showExercise(Exercise exercise) {
        setInitValue((float) exercise.getValue());
        mAutoCompleteTextView.setText(exercise.exercise);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(exercise.getDate());
        timeView.setTime(calendar);
    }

    @Override
    public void setExerciseList(List<ExerciseType> array) {
        mAutoCompleteTextView.setAdapter(new AutocompleteAdapter(array));
    }

    @Override
    public void onError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        hideLoading();
    }

    @Override
    public boolean hasInternet() {
        return ConnectUtils.isHasInternet(this);
    }
}
