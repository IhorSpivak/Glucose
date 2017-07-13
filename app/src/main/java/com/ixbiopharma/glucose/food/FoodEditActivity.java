package com.ixbiopharma.glucose.food;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ixbiopharma.glucose.BaseActivity;
import com.ixbiopharma.glucose.R;
import com.ixbiopharma.glucose.component.FeelingType;
import com.ixbiopharma.glucose.component.LinearAlertError;
import com.ixbiopharma.glucose.component.TimeView;
import com.ixbiopharma.glucose.component.ToolbarView;
import com.ixbiopharma.glucose.di.RepositoryComponent;
import com.ixbiopharma.glucose.food.food_layout.FoodLayout;
import com.ixbiopharma.glucose.food.food_layout.UploadCallback;
import com.ixbiopharma.glucose.image_picker.ImagePicker;
import com.ixbiopharma.glucose.model.Food;
import com.ixbiopharma.glucose.model.FoodType;
import com.ixbiopharma.glucose.model.Glucose;
import com.ixbiopharma.glucose.repository.storage.RealmHelper;
import com.ixbiopharma.glucose.utils.ConnectUtils;
import com.ixbiopharma.glucose.utils.DataTypeUtils;
import com.ixbiopharma.glucose.utils.TimeUtils;
import com.ixbiopharma.glucose.value_picker.TypeValuePickerActivity;

import org.parceler.Parcels;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * FoodActivity
 * <p>
 * Created by ivan on 17.04.17.
 */
public class FoodEditActivity extends BaseActivity implements FoodContract.View, UploadCallback {

    private FoodContract.Presenter presenter;

    private TextView pre_g, post_g, pre_t, post_t;

    private Glucose preGlucose, postGlucose;

    public static final int PICK_IMAGE = 2122;
    public static final int PRE_GLUCOSE = 11;
    public static final int POST_GLUCOSE = 33;

    @BindView(R.id.toolbar)
    ToolbarView toolbarView;

    @BindView(R.id.food_layout)
    FoodLayout foodLayout;

    @BindView(R.id.error)
    LinearAlertError linearAlertError;

    @BindView(R.id.delete)
    View delete;

    @BindView(R.id.timeView)
    TimeView timeView;

    @BindView(R.id.foodType)
    com.ixbiopharma.glucose.component.FoodType foodType;

    @BindView(R.id.feelingType)
    FeelingType feelingType;

    @BindView(R.id.container)
    NestedScrollView nestedScrollView;

    private ImagePicker imagePicker;
    private static String FOOD = "food";
    private static String IS_WATCH = "is_watch";
    private Food food;

    public static void start(Context activity, Food food, boolean isWatch) {
        Intent intent = new Intent(activity, FoodEditActivity.class);
        intent.putExtra(FOOD, food.getId());
        intent.putExtra(IS_WATCH, isWatch);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_edit);
        ButterKnife.bind(this);
        imagePicker = new ImagePicker();
        food = new RealmHelper().getFoodById(getIntent().getIntExtra(FOOD, 0));
        pre_g = (TextView) findViewById(R.id.pre_g);
        post_g = (TextView) findViewById(R.id.post_g);
        pre_t = (TextView) findViewById(R.id.pre_t);
        post_t = (TextView) findViewById(R.id.post_t);

        Glucose preg = new RealmHelper().getGlucoseById(food.getPreGlucose());
        if (preg != null) {
            pre_g.setText(DataTypeUtils.getValueWithType(preg));
            pre_t.setText(TimeUtils.dateToTimeFormat(preg.getDate()));
        }

        Glucose postG = new RealmHelper().getGlucoseById(food.getPostGlucose());

        if (postG != null) {
            post_g.setText(DataTypeUtils.getValueWithType(postG));
            post_t.setText(TimeUtils.dateToTimeFormat(postG.getDate()));
        }

        foodLayout.addFood(food);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(food.getDate());
        timeView.setTime(calendar);

        toolbarView.showHomeArrow(false);
        toolbarView.setHomeClickListener(v -> finish());
        toolbarView.setTitle(food.getName());

        pre_g = (TextView) findViewById(R.id.pre_g);
        post_g = (TextView) findViewById(R.id.post_g);
        pre_t = (TextView) findViewById(R.id.pre_t);
        post_t = (TextView) findViewById(R.id.post_t);

        foodType.setSelected(food.getType());
        feelingType.setSelected(food.getFeeling());

        presenter = new FoodPresenter(this,
                RepositoryComponent.provideFoodRepository(),
                RepositoryComponent.provideGlucoseRepository());

        if (getIntent().getBooleanExtra(IS_WATCH, false)) {
            toolbarView.setActionTitle("Edit");
            delete.setVisibility(View.GONE);

            toolbarView.setActionOnClickListener(v -> {
                finish();
                FoodEditActivity.start(this, food, false);
            });

            timeView.disable();
            foodLayout.disable();
            foodType.setDisabled(true);
            feelingType.setDisabled(true);
        } else {
            toolbarView.setActionTitle("Save");
            delete.setVisibility(View.VISIBLE);
            toolbarView.setActionOnClickListener(v -> presenter.save(
                    foodType.getSelected(),
                    feelingType.getSelected(),
                    timeView.getTime(),
                    foodLayout.getFoods(),
                    preGlucose,
                    postGlucose
            ));

            feelingType.setDisabled(false);
            foodType.setDisabled(false);

            pre_g.setOnClickListener(v -> {
                if (preGlucose == null) {
                    TypeValuePickerActivity
                            .createGlucose(FoodEditActivity.this, PRE_GLUCOSE);
                } else {
                    TypeValuePickerActivity
                            .editGlucose(FoodEditActivity.this, PRE_GLUCOSE, preGlucose.getId());
                }
            });

            pre_t.setOnClickListener(v -> {
                if (preGlucose == null) {
                    TypeValuePickerActivity
                            .createGlucose(FoodEditActivity.this, PRE_GLUCOSE);
                } else {
                    TypeValuePickerActivity
                            .editGlucose(FoodEditActivity.this, PRE_GLUCOSE, preGlucose.getId());
                }
            });

            post_g.setOnClickListener(v -> {
                if (postGlucose == null) {
                    TypeValuePickerActivity
                            .createGlucose(FoodEditActivity.this, POST_GLUCOSE);
                } else {
                    TypeValuePickerActivity
                            .editGlucose(FoodEditActivity.this, POST_GLUCOSE, postGlucose.getId());
                }
            });

            post_t.setOnClickListener(v -> {
                if (postGlucose == null) {
                    TypeValuePickerActivity
                            .createGlucose(FoodEditActivity.this, POST_GLUCOSE);
                } else {
                    TypeValuePickerActivity
                            .editGlucose(FoodEditActivity.this, POST_GLUCOSE, postGlucose.getId());
                }
            });
        }


        delete.setOnClickListener(view -> {
            DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        presenter.delete(food);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(FoodEditActivity.this);
            builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();
        });
    }

    @Override
    public void onDeleteComplete() {
        finish();
    }

    @Override
    public void setFoodHintData(List<FoodType> array) {
        foodLayout.post(() -> foodLayout.setFoodHintData(array));
    }

    private void selectImage() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {

            imagePicker.pickImage(FoodEditActivity.this, "Select your image:");
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PICK_IMAGE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PICK_IMAGE) {
            selectImage();
            return;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_CANCELED) return;

        if (requestCode == PRE_GLUCOSE) {
            Glucose glucose = Parcels.unwrap(data.getParcelableExtra(TypeValuePickerActivity.RESULT));
            glucose.meal = "Pre-Meal";
            preGlucose = glucose;

            pre_g.setText(DataTypeUtils.getValueWithType(glucose));
            pre_t.setText(TimeUtils.dateToTimeFormat(glucose.getDate()));
            return;
        }

        if (requestCode == POST_GLUCOSE) {
            Glucose glucose = Parcels.unwrap(data.getParcelableExtra(TypeValuePickerActivity.RESULT));
            glucose.meal = "Post-Meal";
            postGlucose = glucose;

            post_g.setText(DataTypeUtils.getValueWithType(glucose));
            post_t.setText(TimeUtils.dateToTimeFormat(glucose.getDate()));
            return;
        }
        if (resultCode == -1 && requestCode == 234) {
            foodLayout.onImageAdded(imagePicker.getResult(this, data).toString());
        }
    }

    @Override
    public void showLoading() {
        runOnUiThread(() -> progressDialog.show());
    }

    @Override
    public void hideLoading() {
        runOnUiThread(() -> progressDialog.hide());
    }

    @Override
    public void showError(String message) {
        linearAlertError.post(() -> {
            linearAlertError.setDescriptionText(message);
            linearAlertError.setVisibility(View.VISIBLE);
            progressDialog.hide();
            nestedScrollView.scrollTo(0, 0);

        });
    }

    @Override
    public void onFoodSaved() {
        runOnUiThread(() ->
                Toast.makeText(
                        FoodEditActivity.this,
                        "Saved",
                        Toast.LENGTH_SHORT).show());
        finish();
    }

    @Override
    public void startUpload() {
        selectImage();
    }

    @Override
    public boolean hasInternet() {
        return ConnectUtils.isHasInternet(this);
    }
}
