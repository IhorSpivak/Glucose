package com.ixbiopharma.glucose.profile;

import android.net.Uri;

import com.ixbiopharma.glucose.model.UserProfile;

/**
 * ProfileContract
 * <p>
 * Created by ivan on 14.05.17.
 */

interface ProfileContract {

    interface View {
        void onError(String message);

        void onUserLoaded(UserProfile profile);

        void showLoading();

        void hideLoading();

        int getGlucose();

        int getMeasurementUnit();

        int getPhotos();

        int getNotifications();

        int getTips();

        int getNews();

        void showMenu();

        void showNewPassword();

        void onPasswordChanged();
    }

    interface Presenter {
        void updateUserPic(Uri uri);

        void updateProfile();

        void logout();

        void changePassword();

        void newPassword(String password);
    }
}
