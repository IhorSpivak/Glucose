package com.ixbiopharma.glucose.login.login;

import com.ixbiopharma.glucose.model.UserInfo;
import com.ixbiopharma.glucose.repository.UserRepository;
import com.ixbiopharma.glucose.utils.StringUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * LoginPresenter
 * <p>
 * Created by ivan on 14.05.17.
 */

class LoginPresenter implements LoginContract.Presenter {

    private LoginContract.View view;
    private UserRepository userRepository;

    LoginPresenter(LoginContract.View view, UserRepository userRepository) {
        this.view = view;
        this.userRepository = userRepository;
    }

    @Override
    public void login() {
        String login = view.getLoginField();
        String password = view.getPasswordField();


        if(login.isEmpty()){
            view.onError("Login field is required.");
            return;
        }

        if(password.isEmpty()){
            view.onError("Password field is required.");
            return;
        }

        if(password.isEmpty() && login.isEmpty() ){
            view.onError("Please enter a valid email and password.");
            return;
        }

        if (!StringUtils.isValidEmail(login)) {
            view.onError("Please enter a valid email and password.");
            return;
        }

        userRepository.login(login, password)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(response -> {
                    if (response.getStatus() != 1) {
                        view.onError(response.getMessage());
                    } else {
                        userRepository.saveAuthToken(response.getData().getAuthorization_key());
                        userRepository.saveUserInfo(new UserInfo(
                                response.getData().getFirst_name(),
                                response.getData().getEmail(),
                                response.getData().getUser_id()));

                        view.onLoginSuccess();
                    }
                }, throwable -> {
                    view.onError(throwable.getMessage());
                    throwable.printStackTrace();
                });
    }
}
