package com.ixbiopharma.glucose.value_picker;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.view.View;
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
import com.ixbiopharma.glucose.model.Type;
import com.ixbiopharma.glucose.utils.DWUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TypeValuePickerActivity extends BaseActivity implements ValueContract.View {

    private static final float MIN_VALUE = 1;

    private int transparent;
    private ValueContract.Presenter presenter;

    private static String REQ_CODE = "req_code";
    public static String RESULT = "RESULT";
    private static String ITEM_TYPE = "ITEM_TYPE";
    private static String ID = "ID";
    private static String IS_EDIT = "isEdit";

    private static void startForResult(Activity activity, int requestCode, @Type int type, int id, boolean isEdit) {
        Intent intent = new Intent(activity, TypeValuePickerActivity.class);
        intent.putExtra(REQ_CODE, requestCode);
        intent.putExtra(IS_EDIT, isEdit);
        intent.putExtra(ITEM_TYPE, type);
        intent.putExtra(ID, id);

        activity.startActivityForResult(intent, requestCode);
    }

    public static void createGlucose(Activity activity, int requestCode) {
        startForResult(activity, requestCode, Type.GLUCOSE, -1, false);
    }

    public static void editWeight(Activity activity, int requestCode, int id) {
        startForResult(activity, requestCode, Type.WIGHT, id, true);
    }

    public static void editGlucose(Activity activity, int requestCode, int id) {
        startForResult(activity, requestCode, Type.GLUCOSE, id, true);
    }

    public static void createWeight(Activity activity, int requestCode) {
        startForResult(activity, requestCode, Type.WIGHT, -1, false);
    }

    @BindView(R.id.hint_title)
    TextView hintTitle;
    @BindView(R.id.value_point)
    TextView valuePointView;
    @BindView(R.id.timeView)
    TimeView timeView;
    @BindView(R.id.value)
    TextView valueT;
    @BindView(R.id.myScrollingValuePicker)
    ScrollingValuePicker myScrollingValuePicker;
    @BindView(R.id.left_grad)
    View left_grad;
    @BindView(R.id.right_grad)
    View right_grad;
    @BindView(R.id.container)
    RelativeLayout container;
    @BindView(R.id.placeholder)
    PlaceHolderView placeHolderView;
    @BindView(R.id.toolbar)
    ToolbarView toolbarView;
    @BindView(R.id.g_info)
    View gInfo;
    @BindView(R.id.w_info)
    View wInfo;
    @BindView(R.id.normal)
    TextView normal;
    @BindView(R.id.highrisk1)
    TextView highrisk1;
    @BindView(R.id.highrisk2)
    TextView highrisk2;
    @BindView(R.id.healthy)
    TextView healthy;

    private int max_value, koef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_value_picker);
        ButterKnife.bind(this);
        toolbarView.showHomeArrow(true);
        toolbarView.setActionEnabled(false);
        toolbarView.setHomeClickListener(v -> {
            setResult(RESULT_CANCELED);
            finish();
        });

        toolbarView.setActionOnClickListener(v -> {
            long time = timeView.getTime();
            presenter.save(valueT.getText().toString(), time);
            Toast.makeText(TypeValuePickerActivity.this, "Saved", Toast.LENGTH_SHORT).show();
        });

        transparent = ContextCompat.getColor(this, android.R.color.transparent);
        myScrollingValuePicker.setValueTypeMultiple(10);
        myScrollingValuePicker.setOnScrollChangedListener(pickerListener);

        ContainerScrollListener containerScrollListener = new ContainerScrollListener(myScrollingValuePicker);
        container.setOnTouchListener(containerScrollListener);

        presenter = new ValuePresenter(
                this,
                getIntent().getIntExtra(ITEM_TYPE, Type.GLUCOSE),
                getIntent().getIntExtra(ID, -1),
                RepositoryComponent.provideFWeightRepository(),
                RepositoryComponent.provideGlucoseRepository(),
                getIntent().getBooleanExtra(IS_EDIT, false));
    }

    @Override
    public void setItemTypeData(String title,
                                String hint,
                                String valuePoint,
                                int maxValue,
                                double lastValue,
                                int koef,
                                int valueKoef,
                                boolean showPlaceHolder) {

        toolbarView.setTitle(title);
        hintTitle.setText(hint);
        valuePointView.setText(valuePoint);
        max_value = maxValue;
        this.koef = koef;
        myScrollingValuePicker.setMaxValue(MIN_VALUE, max_value);
        myScrollingValuePicker.lineRulerView.kef = koef;

        myScrollingValuePicker.setInitValue((float) lastValue * koef);

        if (Objects.equals(valuePoint, "Kg")) {
            placeHolderView.setBackgroundColor(ContextCompat.getColor(this, R.color.color_green));
            wInfo.setVisibility(View.VISIBLE);
            gInfo.setVisibility(View.GONE);
        } else {
            gInfo.setVisibility(View.VISIBLE);
            wInfo.setVisibility(View.GONE);

            healthy.setText(String.format("%d to %d %s", 4 * valueKoef, 7 * valueKoef, valuePoint));
            highrisk1.setText(String.format("Below %d %s", 4 * valueKoef, valuePoint));
            normal.setText(String.format("%d to %d %s", 4 * valueKoef, 7 * valueKoef, valuePoint));
            highrisk2.setText(String.format("Above %d %s", 7 * valueKoef, valuePoint));
        }

        if (showPlaceHolder) {
            placeHolderView.setVisibility(View.VISIBLE);
        } else {
            placeHolderView.setVisibility(View.GONE);
            toolbarView.setActionEnabled(true);
        }

        placeHolderView.setOnClickListener(v -> {
            placeHolderView.setVisibility(View.GONE);
            toolbarView.setActionEnabled(true);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destroy();
    }

    private ObservableHorizontalScrollView.OnScrollChangedListener pickerListener = new ObservableHorizontalScrollView.OnScrollChangedListener() {
        @Override
        public void onScrollChanged(ObservableHorizontalScrollView view, int l, int t) {
            int value = DWUtils.getValueAndScrollItemToCenter(
                    myScrollingValuePicker.getScrollView(),
                    l, t, max_value, MIN_VALUE,
                    myScrollingValuePicker.getViewMultipleSize());

            float currentValue = (float) value / (float) koef;

            int newColor = ContextCompat.getColor(
                    TypeValuePickerActivity.this,
                    presenter.getValueColor(currentValue));

            int[] colors = {newColor, transparent};

            left_grad.setBackground(
                    new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors));

            right_grad.setBackground(
                    new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, colors));

            container.setBackgroundColor(newColor);
            valueT.setText(String.valueOf(currentValue));
        }

        @Override
        public void onScrollStopped(int l, int t) {

        }
    };

    @Override
    public <T extends Parcelable> void onRecordSaved(T obj) {
        Intent intent = new Intent();
        intent.putExtra(RESULT, obj);
        setResult(getIntent().getIntExtra(REQ_CODE, 0), intent);
        finish();
    }

    @Override
    public void showLoading() {
        progressDialog.show();
    }

    @Override
    public void hideLoading() {
        progressDialog.hide();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_CANCELED);
    }

    @Override
    public void onError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        hideLoading();
    }

    @Override
    public void showDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        timeView.setTime(calendar);
    }
}
