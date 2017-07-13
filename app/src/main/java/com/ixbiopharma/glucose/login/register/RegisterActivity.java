package com.ixbiopharma.glucose.login.register;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ixbiopharma.glucose.BaseActivity;
import com.ixbiopharma.glucose.R;
import com.ixbiopharma.glucose.component.LinearAlertError;
import com.ixbiopharma.glucose.component.ToolbarView;
import com.ixbiopharma.glucose.di.RepositoryComponent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * LoginActivity
 * <p>
 * Created by ivan on 13.05.17.
 */

public class RegisterActivity extends BaseActivity implements RegisterContract.View {

    @BindView(R.id.toolbar)
    ToolbarView toolbarView;
    @BindView(R.id.alert_error)
    LinearAlertError errorAlert;

    @BindView(R.id.first)
    EditText first;

    @BindView(R.id.last)
    EditText last;

    @BindView(R.id.email)
    EditText email;

    @BindView(R.id.password)
    EditText password;

    @BindView(R.id.confirmPassword)
    EditText confirmPassword;

    @BindView(R.id.weight)
    EditText weight;

    @BindView(R.id.age)
    EditText age;

    @BindView(R.id.height)
    EditText height;

    @BindView(R.id.choose)
    TextView choose;

    @BindView(R.id.nested)
    NestedScrollView nestedScrollView;

    private int reason = -1;

    public static void start(Activity activity) {
        activity.startActivity(new Intent(activity, RegisterActivity.class));
    }

    private RegisterContract.Presenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        toolbarView.showHomeArrow(false);
        toolbarView.setHomeClickListener(v -> finish());
        toolbarView.showAction(false);
        toolbarView.setTitle("Register");

        presenter = new RegisterPresenter(this, RepositoryComponent.provideUserRepository());


        height.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }
            @Override
            public void afterTextChanged(Editable text) {
                if (text.length() == 1) {
                    text.append('.');
                }
            }
        });

        weight.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }
            @Override
            public void afterTextChanged(Editable text) {
                String value = text.toString();
                if (value.length() == 2) {
                    value = value + ".";
                    weight.setText(value);
                    weight.setSelection(weight.getText().length());
                }
            }
        });

//        weight.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(3, 1)});
//        height.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(3, 2)});

    }



    @Override
    public String getFirstNameField() {
        return first.getText().toString();
    }

    @Override
    public String getLastNameField() {
        return last.getText().toString();
    }

    @Override
    public String getEmailField() {
        return email.getText().toString().trim();
    }

    @Override
    public String getPasswordField() {
        return password.getText().toString().trim();
    }

    @Override
    public double getHeightField() {
        String value = height.getText().toString().trim();
        if (value.isEmpty()) return 0;
        return Double.parseDouble(value);
    }

    @Override
    public double getWeightField() {
        String value = weight.getText().toString().trim();
        if (value.isEmpty()) return 0;
        return Double.parseDouble(value);
    }

    @Override
    public int getJoinReasonField() {
        return reason;
    }

    @Override
    public void onRegisterSuccess(String message) {
        progressDialog.hide();
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        new Handler().postDelayed(this::finish, 500);
    }

    @Override
    public void onError(String message) {
        nestedScrollView.post(() -> {
            progressDialog.hide();
            errorAlert.setDescriptionText(message);
            errorAlert.setVisibility(View.VISIBLE);
            nestedScrollView.smoothScrollTo(0, 0);
        });
    }

    @Override
    public String getConfirmPassword() {
        return confirmPassword.getText().toString();
    }

    @OnClick(R.id.register)
    void onRegister() {
        progressDialog.show();
        presenter.signUp();
    }

    @Override
    public int getAge() {
        if (age.getText().toString().trim().isEmpty()) return 0;
        return Integer.parseInt(age.getText().toString().trim());
    }

    @OnClick(R.id.choose)
    void onChoose() {
        final CharSequence[] items = {"Diabetes Management", "Weight Management", "General Health"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Why did you join this app?");
        builder.setItems(items, (dialog, item) -> {
            choose.setText(items[item]);
            reason = item;
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private class DecimalDigitsInputFilter implements InputFilter {

        Pattern mPattern;

        DecimalDigitsInputFilter(int digitsBeforeZero, int digitsAfterZero) {
            mPattern = Pattern.compile("[0-9]{0," + (digitsBeforeZero - 1) + "}+((\\.[0-9]{0," + (digitsAfterZero - 1) + "})?)||(\\.)?");
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            Matcher matcher = mPattern.matcher(dest);
            if (!matcher.matches())
                return "";
            return null;
        }

    }
}
