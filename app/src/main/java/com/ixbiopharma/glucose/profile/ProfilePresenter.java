package com.ixbiopharma.glucose.profile;

import android.net.Uri;

import com.ixbiopharma.glucose.model.UserProfile;
import com.ixbiopharma.glucose.repository.UserRepository;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * ProfilePresenter
 * <p>
 * Created by ivan on 14.05.17.
 */

class ProfilePresenter implements ProfileContract.Presenter {

    private ProfileContract.View view;
    private UserRepository userRepository;
    private UserProfile userProfileCache;

    ProfilePresenter(ProfileContract.View view, UserRepository userRepository) {
        this.view = view;
        this.userRepository = userRepository;
        getProfile();
    }

    @Override
    public void updateUserPic(Uri uri) {
        view.showLoading();
        String path = uri.getPath();

        userRepository.setUserPic(path)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(() -> {
                            view.hideLoading();
                            getProfile();
                        },
                        throwable -> view.onError(throwable.getMessage()));
    }

    @Override
    public void updateProfile() {
        UserProfile userProfile = new UserProfile();
        userProfile.setGlucoseUnit(view.getGlucose());
        userProfile.setMeasurementUnit(view.getMeasurementUnit());
        userProfile.setEnableNews(view.getNews());
        userProfile.setEnableTips(view.getTips());
        userProfile.setEnableNotifications(view.getNotifications());
        userProfile.setSavePhotos(view.getPhotos());

        if (userProfileCache.settingsEqual(userProfile)) {
            view.showMenu();
            return;
        }

        view.showLoading();

        userRepository.setUserProfile(userProfile)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(() -> view.showMenu(),
                        throwable -> view.onError(throwable.getMessage()));

    }

    private void getProfile() {
        view.showLoading();

        userRepository.getUserProfile()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(userProfile -> {
                    userProfileCache = userProfile;
                    view.onUserLoaded(userProfile);
                    view.hideLoading();
                }, throwable -> {
                    throwable.printStackTrace();
                    view.onError(throwable.getMessage());
                });
    }

    @Override
    public void logout() {
        userRepository.logout();
    }

    @Override
    public void changePassword() {
        view.showNewPassword();
    }

    @Override
    public void newPassword(String password) {
        view.showLoading();

        userRepository.newPassword(password)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(() -> {
                    view.onPasswordChanged();
                    view.hideLoading();
                }, throwable -> view.onError(throwable.getMessage() + ""));
    }
}
