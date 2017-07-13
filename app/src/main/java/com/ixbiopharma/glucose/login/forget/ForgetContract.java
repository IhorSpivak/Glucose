package com.ixbiopharma.glucose.login.forget;

/**
 * ForgetContract
 * <p>
 * Created by ivan on 14.05.17.
 */

interface ForgetContract {
    interface View {
        String getEmailField();

        void onError(String message);

        void onForgetSuccess();

        void showCode();
    }

    interface Presenter {
        void forget();

        void enterCode(String code, String password);
    }
}
