package com.ixbiopharma.glucose.repository;

import android.support.annotation.Nullable;

import com.ixbiopharma.glucose.model.UserInfo;
import com.ixbiopharma.glucose.model.UserProfile;
import com.ixbiopharma.glucose.model.api.ForgetPasswordResponse;
import com.ixbiopharma.glucose.model.api.LoginResponse;
import com.ixbiopharma.glucose.model.api.RegisterResponse;
import com.ixbiopharma.glucose.model.api.Response;

import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * UserRepository
 * <p>
 * Created by ivan on 21.05.17.
 */

public interface UserRepository {

    Observable<Response<LoginResponse>> login(String email, String password);

    void saveAuthToken(String token);

    void saveUserInfo(UserInfo userInfo);

    Observable<UserProfile> getUserProfile();

    Completable setUserProfile(UserProfile userProfile);

    Observable<UserProfile> getUserProfileCache();

    Completable setUserPic(String path);

    Observable<Response<ForgetPasswordResponse>> forgetPassword(String email);

    Observable<Response<RegisterResponse>> signup(String first,
                                                  String last,
                                                  String email,
                                                  String password,
                                                  double height,
                                                  double weight,
                                                  int join,
                                                  int age);

    void logout();

    boolean isUserLogged();

    @Nullable UserProfile getUserProfileFromPrefs();

    Completable newPassword(String password);

    Completable emailCode(String code, String password);

    boolean isFirstRun();

    void setRunned();
}
