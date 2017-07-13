package com.ixbiopharma.glucose.login.register;

import com.ixbiopharma.glucose.repository.UserRepository;
import com.ixbiopharma.glucose.utils.StringUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * RegisterPresenter
 * <p>
 * Created by ivan on 14.05.17.
 */

class RegisterPresenter implements RegisterContract.Presenter {

    private RegisterContract.View view;
    private UserRepository userRepository;

    RegisterPresenter(RegisterContract.View view, UserRepository userRepository) {
        this.view = view;
        this.userRepository = userRepository;
    }

    private boolean isDataValid(String firstName,
                                String lastName,
                                String email,
                                String password,
                                String confirmPassword,
                                double height,
                                double weight,
                                int reason,
                                int age) {

        if (firstName.isEmpty()) {
            view.onError("First name cant be empty!");
            return false;
        }

        if (lastName.isEmpty()) {
            view.onError("Last name cant be empty!");
            return false;
        }

        if (email.isEmpty()) {
            view.onError("Email cant be empty!");
            return false;
        }

        if (!StringUtils.isValidEmail(email)) {
            view.onError("Email is not valid!");
            return false;
        }

        if (password.isEmpty()) {
            view.onError("Invalid password!");
            return false;
        }

        if (!confirmPassword.equals(password)) {
            view.onError("Passwords does not match!");
            return false;
        }

        if (reason == -1) {
            view.onError("Choose reason why did you join this app!");
            return false;
        }

        if (weight == 0) {
            view.onError("Weight is invalid!");
            return false;
        }

        if (age == 0 || age > 130) {
            view.onError("Weight is invalid!");
            return false;
        }

        if (height == 0) {
            view.onError("Height is invalid!");
            return false;
        }

        return true;
    }

    @Override
    public void signUp() {
        String firstName = view.getFirstNameField().replace(" ", "");
        String lastName = view.getLastNameField().replace(" ", "");
        String email = view.getEmailField().replace(" ", "");
        String password = view.getPasswordField().replace(" ", "");
        String confirmPassword = view.getConfirmPassword().replace(" ", "");

        double height = view.getHeightField();
        double weight = view.getWeightField();
        int reason = view.getJoinReasonField();
        int age = view.getAge();

        if (!isDataValid(firstName, lastName, email,
                password, confirmPassword,
                height, weight, reason, age)) return;

        userRepository.signup(
                firstName,
                lastName,
                email,
                password,
                height,
                weight,
                reason,
                age)

                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(registerResponseResponse -> {
                    if (registerResponseResponse.getStatus() != 1) {
                        view.onError(registerResponseResponse.getMessage());
                    } else {
                        view.onRegisterSuccess(registerResponseResponse.getMessage());
                    }
                }, throwable -> {
                    throwable.printStackTrace();
                    view.onError(throwable.getMessage());
                });
    }

}
