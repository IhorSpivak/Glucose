package com.ixbiopharma.glucose.login.login;

/**
 * LoginContract
 *
 * Created by ivan on 13.05.17.
 */

interface LoginContract {

    interface View{
        void onLoginSuccess();
        String getLoginField();
        String getPasswordField();
        void onError(String message);
    }

    interface Presenter{
        void login();
    }
}
