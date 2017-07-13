package com.ixbiopharma.glucose.repository;

import com.google.gson.Gson;
import com.ixbiopharma.glucose.api.ApiImpl;
import com.ixbiopharma.glucose.model.UserInfo;
import com.ixbiopharma.glucose.model.UserProfile;
import com.ixbiopharma.glucose.model.api.ForgetPasswordResponse;
import com.ixbiopharma.glucose.model.api.LoginResponse;
import com.ixbiopharma.glucose.model.api.RegisterResponse;
import com.ixbiopharma.glucose.model.api.Response;
import com.ixbiopharma.glucose.repository.storage.AppPrefs;
import com.ixbiopharma.glucose.repository.storage.RealmHelper;
import com.ixbiopharma.glucose.utils.DataTypeUtils;

import java.io.File;

import io.reactivex.Completable;
import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * UserRepositoryImpl
 * <p>
 * Created by ivan on 21.05.17.
 */

public class UserRepositoryImpl implements UserRepository {

    private ApiImpl api;
    private AppPrefs prefs;
    private RealmHelper realmHelper;

    public UserRepositoryImpl(ApiImpl api, AppPrefs prefs, RealmHelper realmHelper) {
        this.api = api;
        this.prefs = prefs;
        this.realmHelper = realmHelper;
    }

    @Override
    public Observable<Response<LoginResponse>> login(String email, String password) {
        return api.login(email, password);
    }

    @Override
    public void saveAuthToken(String token) {
        prefs.setAuthToken(token);
    }

    @Override
    public void saveUserInfo(UserInfo userInfo) {
        Gson gson = new Gson();
        String jsonInString = gson.toJson(userInfo);
        prefs.setUserInfoJson(jsonInString);
    }

    @Override
    public Observable<UserProfile> getUserProfile() {
        return api.getUserProfile(prefs.getAuthToken())
                .map(userProfile -> {
                    saveUserProfile(userProfile);
                    return userProfile;
                });
    }

    @Override
    public Completable setUserProfile(UserProfile userProfile) {
        return Completable.fromObservable(api.setUserProfile(prefs.getAuthToken(), userProfile));
    }

    @Override
    public UserProfile getUserProfileFromPrefs() {
        Gson gson = new Gson();
        return gson.fromJson(prefs.getUserProfleJson(), UserProfile.class);
    }

    @Override
    public Completable newPassword(String password) {
        return api.enterNewPassword(prefs.getAuthToken(), password);
    }

    @Override
    public Completable emailCode(String code, String password) {
        return api.enterEmailCode(code, password);
    }

    @Override
    public boolean isFirstRun() {
        return prefs.isIsFirstRun();
    }

    @Override
    public void setRunned() {
        prefs.setIsFirstRun(false);
    }

    @Override
    public Observable<UserProfile> getUserProfileCache() {
        return Observable.fromCallable(this::getUserProfileFromPrefs)
                .flatMap(userProfile -> {
                    if (userProfile == null) {
                        return getUserProfile();
                    }
                    return Observable.just(userProfile);
                });
    }

    private void saveUserProfile(UserProfile userProfile) {
        //todo settings
        DataTypeUtils.glucoseMeasureType = userProfile.getGlucoseUnit();

        Gson gson = new Gson();
        String jsonInString = gson.toJson(userProfile);
        prefs.setUserProfleJson(jsonInString);
    }

    @Override
    public Completable setUserPic(String path) {
        File file = new File(path);

        MultipartBody.Part filePart = MultipartBody.Part.createFormData("document",
                file.getName(), RequestBody.create(MediaType.parse("image/*"), file));

        return api.setUserPic(filePart, prefs.getAuthToken());
    }

    @Override
    public void logout() {
        prefs.removeAuthToken();
        prefs.removeUserInfoJson();
        prefs.removeUserProfleJson();
        prefs.removeIsFirstRun();
        prefs.removeModifyDate();
        realmHelper.removeAllData();
    }

    @Override
    public boolean isUserLogged() {
        return !prefs.getAuthToken().isEmpty();
    }

    @Override
    public Observable<Response<ForgetPasswordResponse>> forgetPassword(String email) {
        return api.forget(email);
    }

    @Override
    public Observable<Response<RegisterResponse>> signup(String first, String last, String email,
                                                         String password,
                                                         double height,
                                                         double weight, int join, int age) {

        return api.signup(first, last, email, password, height, weight, join, age);
    }
}
