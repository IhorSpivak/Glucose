package com.ixbiopharma.glucose.login.register;

/**
 * RegisterContract
 * <p>
 * Created by ivan on 14.05.17.
 */

interface RegisterContract {
    interface View {
        String getFirstNameField();

        String getLastNameField();

        String getEmailField();

        String getPasswordField();

        double getHeightField();

        double getWeightField();

        int getAge();

        int getJoinReasonField();

        void onError(String message);

        void onRegisterSuccess(String message);

        String getConfirmPassword();
    }

    interface Presenter {
        void signUp();
    }
}
