package com.ixbiopharma.glucose.profile;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.ixbiopharma.glucose.BaseActivity;
import com.ixbiopharma.glucose.ChromeTab;
import com.ixbiopharma.glucose.R;
import com.ixbiopharma.glucose.component.SettingsItem;
import com.ixbiopharma.glucose.component.SettingsSwitchItem;
import com.ixbiopharma.glucose.component.ToolbarView;
import com.ixbiopharma.glucose.di.RepositoryComponent;
import com.ixbiopharma.glucose.image_picker.ImagePicker;
import com.ixbiopharma.glucose.login.login.LoginActivity;
import com.ixbiopharma.glucose.model.UserProfile;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * ProfileActivity
 * <p>
 * Created by ivan on 13.05.17.
 */

public class ProfileActivity extends BaseActivity implements ProfileContract.View {

    public static void start(Activity activity) {
        activity.startActivity(new Intent(activity, ProfileActivity.class));
    }

    public static final int PICK_IMAGE = 2122;

    @BindView(R.id.profile_pic)
    SimpleDraweeView profile_pic;

    @BindView(R.id.toolbar)
    ToolbarView toolbarView;

    @BindView(R.id.change_photo)
    TextView change_photo;

    @BindView(R.id.name)
    TextView name;

    @BindView(R.id.email)
    SettingsItem email;

    @BindView(R.id.enable_news)
    SettingsSwitchItem enable_news;

    @BindView(R.id.enable_notifications)
    SettingsSwitchItem enable_notifications;

    @BindView(R.id.enable_tips)
    SettingsSwitchItem enable_tips;

    @BindView(R.id.save_to_camera)
    SettingsSwitchItem save_to_camera;

    @BindView(R.id.glucose_unit)
    SettingsSwitchItem glucoseUnit;

    @BindView(R.id.measurement_unit)
    SettingsSwitchItem measurementUnit;

    private ProfileContract.Presenter presenter;
    private ImagePicker imagePicker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        imagePicker = new ImagePicker();

        toolbarView.showHomeArrow(true);
        toolbarView.setHomeArrowIcon(R.drawable.ic_menu);
        toolbarView.showAction(false);
        toolbarView.setHomeClickListener(v -> presenter.updateProfile());

        toolbarView.setTitle("Settings");

        change_photo.setPaintFlags(change_photo.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        change_photo.setOnClickListener(v -> selectImage());

        RoundingParams roundingParams = RoundingParams.fromCornersRadius(5f);
        roundingParams.setRoundAsCircle(true);
        profile_pic.getHierarchy().setRoundingParams(roundingParams);

        presenter = new ProfilePresenter(this, RepositoryComponent.provideUserRepository());
    }

    private void selectImage() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {

            imagePicker.pickImage(ProfileActivity.this, "Select your image:");
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PICK_IMAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PICK_IMAGE) {
            selectImage();
        }
    }

    @OnClick(R.id.logout)
    void onLogout() {
        DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    presenter.logout();
                    LoginActivity.start(ProfileActivity.this, true);
                    finish();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    break;
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    @OnClick(R.id.faq)
    void onFAQ() {
        String websiteURL = "http://biopharma.massiveinfinity.com/faq/";
        ChromeTab.start(this, websiteURL);
    }

    @OnClick(R.id.feedback)
    void onFeedback() {
        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.setType("plain/text");
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"dawn@massiveinfinity.com"});
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Feedback");

        startActivity(Intent.createChooser(emailIntent, "Send mail..."));
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
    public void showMenu() {
        hideLoading();
        showMenu(13);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_CANCELED) return;

        if (resultCode == -1 && requestCode == 234) {

            Uri selectedImage = imagePicker.getResult(this, data);

            profile_pic.setImageURI(selectedImage);
            presenter.updateUserPic(selectedImage);
        }
    }

    @Override
    public void onError(String message) {
        hideLoading();
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onUserLoaded(UserProfile profile) {
        name.setText(String.format("%s %s", profile.getFirstName(), profile.getLastName()));
        profile_pic.setImageURI(profile.getPhoto());
        glucoseUnit.setCheckedItem(profile.getGlucoseUnit());
        measurementUnit.setCheckedItem(profile.getMeasurementUnit());
        email.setText(profile.getEmail());
        save_to_camera.setCheckedItem(profile.getSavePhotos());
        enable_news.setCheckedItem(profile.getEnableNews());
        enable_notifications.setCheckedItem(profile.getEnableNotifications());
        enable_tips.setCheckedItem(profile.getEnableTips());
    }

    @Override
    public int getGlucose() {
        return glucoseUnit.getCheckedItem();
    }

    @Override
    public int getMeasurementUnit() {
        return measurementUnit.getCheckedItem();
    }

    @Override
    public int getPhotos() {
        return save_to_camera.getCheckedItem();
    }

    @Override
    public int getNotifications() {
        return enable_notifications.getCheckedItem();
    }

    @Override
    public int getTips() {
        return enable_tips.getCheckedItem();
    }

    @Override
    public int getNews() {
        return enable_news.getCheckedItem();
    }

    @OnClick(R.id.change_password)
    void onChangePasswordClick() {
        presenter.changePassword();
    }

    @Override
    public void onPasswordChanged() {
        Toast.makeText(this, "Password changed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showNewPassword() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter new password");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);

        builder.setPositiveButton("OK", (dialog, which) -> presenter.newPassword(input.getText().toString()));
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();

    }
}
