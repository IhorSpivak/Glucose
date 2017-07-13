package com.ixbiopharma.glucose.login.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;

import com.ixbiopharma.glucose.BaseActivity;
import com.ixbiopharma.glucose.R;
import com.ixbiopharma.glucose.component.LinearAlert;
import com.ixbiopharma.glucose.component.LinearAlertError;
import com.ixbiopharma.glucose.component.ToolbarView;
import com.ixbiopharma.glucose.di.RepositoryComponent;
import com.ixbiopharma.glucose.journal.JournalActivity;
import com.ixbiopharma.glucose.login.forget.ForgetActivity;
import com.ixbiopharma.glucose.login.register.RegisterActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * LoginActivity
 * <p>
 * Created by ivan on 13.05.17.
 */

public class LoginActivity extends BaseActivity implements LoginContract.View {

    @BindView(R.id.toolbar)
    ToolbarView toolbarView;

    @BindView(R.id.login_edit)
    EditText login;

    @BindView(R.id.password_edit)
    EditText password;

    @BindView(R.id.alert)
    LinearAlert linearAlert;
    @BindView(R.id.alert_error)
    LinearAlertError errorAlert;

    private LoginContract.Presenter presenter;

    public static void start(Activity activity, boolean isLogout) {
        Intent intent = new Intent(activity, LoginActivity.class);
        intent.putExtra("logout", isLogout);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);


        if (getIntent().getBooleanExtra("logout", false)) {
            linearAlert.setVisibility(View.VISIBLE);
            linearAlert.setDescriptionText("You have successfully logged out.");
            linearAlert.setTitle("Logged Out");
            linearAlert.setIcon(R.drawable.ic_success_alert);
        }

        toolbarView.hideHome();
        toolbarView.showAction(false);
        toolbarView.setTitle("Login");

        presenter = new LoginPresenter(this, RepositoryComponent.provideUserRepository());
    }

    @OnClick(R.id.login)
    void onLogin() {
        progressDialog.show();
        presenter.login();
    }

    @Override
    public void onLoginSuccess() {
        progressDialog.hide();
        JournalActivity.start(this);
        finish();
    }

    @Override
    public String getLoginField() {
        return login.getText().toString();
    }

    @Override
    public String getPasswordField() {
        return password.getText().toString();
    }

    @OnClick(R.id.register)
    void onRegister() {
        RegisterActivity.start(this);
    }

    @Override
    public void onError(String message) {
        progressDialog.hide();
        linearAlert.setVisibility(View.GONE);
        errorAlert.setDescriptionText(message);
        errorAlert.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.forget)
    void onForget() {
        ForgetActivity.start(this);
    }
}
