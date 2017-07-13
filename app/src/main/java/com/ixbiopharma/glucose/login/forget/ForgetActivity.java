package com.ixbiopharma.glucose.login.forget;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ixbiopharma.glucose.BaseActivity;
import com.ixbiopharma.glucose.R;
import com.ixbiopharma.glucose.component.LinearAlertError;
import com.ixbiopharma.glucose.component.ToolbarView;
import com.ixbiopharma.glucose.di.RepositoryComponent;
import com.ixbiopharma.glucose.utils.AndroidUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * LoginActivity
 * <p>
 * Created by ivan on 13.05.17.
 */

public class ForgetActivity extends BaseActivity implements ForgetContract.View {

    @BindView(R.id.toolbar)
    ToolbarView toolbarView;
    @BindView(R.id.alert_error)
    LinearAlertError errorAlert;
    @BindView(R.id.email)
    EditText email;

    private ForgetContract.Presenter presenter;

    public static void start(Activity activity) {
        activity.startActivity(new Intent(activity, ForgetActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);
        ButterKnife.bind(this);

        email.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

        toolbarView.showHomeArrow(false);
        toolbarView.setHomeClickListener(v -> finish());
        toolbarView.showAction(false);
        toolbarView.setTitle("Forget Password");

        presenter = new ForgetPresenter(this, RepositoryComponent.provideUserRepository());
    }

    @Override
    public String getEmailField() {
        return email.getText().toString();
    }

    @Override
    public void onForgetSuccess() {
        progressDialog.hide();
        new Handler().postDelayed(this::finish, 500);
    }

    @OnClick(R.id.reset)
    void onReset() {
        progressDialog.show();
        presenter.forget();
    }

    @Override
    public void onError(String message) {
        progressDialog.hide();
        errorAlert.setDescriptionText(message);
        errorAlert.setVisibility(View.VISIBLE);
    }

    private String code;

    @Override
    public void showCode() {
        progressDialog.show();
        progressDialog.hide();

        Toast.makeText(this, "Please look at reset password instructions in your email.", Toast.LENGTH_LONG).show();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final TextView title = new TextView(this);
        title.setText("Enter code from email");
        title.setTextSize(20);

        LinearLayout linearLayoutTitle = new LinearLayout(this);

        linearLayoutTitle.addView(title);

        LinearLayout.LayoutParams layoutParamsTitle =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);

        layoutParamsTitle.setMargins(AndroidUtils.dpToPx(this, 0), 80, AndroidUtils.dpToPx(this, 0), 0);




        title.setGravity(Gravity.CENTER_HORIZONTAL);
        title.setLayoutParams(layoutParamsTitle);
        title.setInputType(InputType.TYPE_CLASS_TEXT);

        builder.setCustomTitle(linearLayoutTitle);



        final EditText input = new EditText(this);

        LinearLayout linearLayout = new LinearLayout(this);

        linearLayout.addView(input);

        LinearLayout.LayoutParams layoutParams =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);

        layoutParams.setMargins(AndroidUtils.dpToPx(this, 45), 0, AndroidUtils.dpToPx(this, 45), 0);



        input.setLayoutParams(layoutParams);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(linearLayout);


        builder.setPositiveButton("OK", (dialog, which) -> {
            code = (input.getText().toString());
            showInputPassword();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void showInputPassword() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final TextView title = new TextView(this);
        title.setText("Enter new password");
        title.setTextSize(20);

        LinearLayout linearLayoutTitle = new LinearLayout(this);

        linearLayoutTitle.addView(title);

        LinearLayout.LayoutParams layoutParamsTitle =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);


        layoutParamsTitle.setMargins(AndroidUtils.dpToPx(this, 0), 80, AndroidUtils.dpToPx(this, 0), 0);




        title.setGravity(Gravity.CENTER_HORIZONTAL);
        title.setLayoutParams(layoutParamsTitle);
        title.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setCustomTitle(linearLayoutTitle);


        final EditText input = new EditText(this);
        LinearLayout linearLayout = new LinearLayout(this);

        linearLayout.addView(input);
        LinearLayout.LayoutParams layoutParams =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);

        layoutParams.setMargins(AndroidUtils.dpToPx(this, 45), 0, AndroidUtils.dpToPx(this, 45), 0);

        input.setLayoutParams(layoutParams);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(linearLayout);

        builder.setPositiveButton("OK", (dialog, which) -> {
            presenter.enterCode(code, (input.getText().toString()));
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }
}
