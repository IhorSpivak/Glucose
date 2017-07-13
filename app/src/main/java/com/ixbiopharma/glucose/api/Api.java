package com.ixbiopharma.glucose.api;

import com.ixbiopharma.glucose.model.DateNews;
import com.ixbiopharma.glucose.model.NewsDetailWrapper;
import com.ixbiopharma.glucose.model.SyncData;
import com.ixbiopharma.glucose.model.UserProfile;
import com.ixbiopharma.glucose.model.api.DayAdviceRequest;
import com.ixbiopharma.glucose.model.api.DayAdviceResponse;
import com.ixbiopharma.glucose.model.api.ForgetPasswordResponse;
import com.ixbiopharma.glucose.model.api.LoginRequest;
import com.ixbiopharma.glucose.model.api.LoginResponse;
import com.ixbiopharma.glucose.model.api.NewsDetailRequest;
import com.ixbiopharma.glucose.model.api.NewsRequest;
import com.ixbiopharma.glucose.model.api.NewsWrapper;
import com.ixbiopharma.glucose.model.api.PasswordSetRequest;
import com.ixbiopharma.glucose.model.api.RegisterRequest;
import com.ixbiopharma.glucose.model.api.RegisterResponse;
import com.ixbiopharma.glucose.model.api.Response;
import com.ixbiopharma.glucose.model.api.SearchRequest;
import com.ixbiopharma.glucose.model.api.UserProfileRequest;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Api
 * <p>
 * Created by ivan on 14.05.17.
 */

interface Api {

    String API_URL = "https://app.entity-health.com/api/";

    @POST("login")
    Observable<Response<LoginResponse>> login(@Body LoginRequest loginRequest);

    @POST("forgot")
    Observable<Response<ForgetPasswordResponse>> forgetPassword(@Body LoginRequest loginRequest);

    @POST("signUp")
    Observable<Response<RegisterResponse>> signup(@Body RegisterRequest registerRequest);

    @POST("news_headers")
    Observable<Response<List<DateNews>>> getNews(@Body NewsRequest newsRequest);

    @POST("news_details")
    Observable<Response<NewsDetailWrapper>> getNewsDetail(@Body NewsDetailRequest newsRequest);

    @POST("profile_get")
    Observable<Response<UserProfile>> getUserProfile(@Body UserProfileRequest userProfileRequest);

    @POST("profile_set")
    Observable<Response<UserProfile>> setUserProfile(@Body UserProfile userProfile);

    @POST("advice_day")
    Observable<Response<List<DayAdviceResponse>>> getDayAdvice(@Body DayAdviceRequest dayAdviceRequest);

    @POST("psswd_reset")
    Completable resetPasswordEmailCode(@Query("code") String code,
                                       @Query("password") String password);

    @POST("psswd_set")
    Completable passwordSet(@Body PasswordSetRequest passwordSetRequest);

    @POST("news_search")
    Observable<Response<NewsWrapper>> search(@Body SearchRequest searchRequest);

    @Multipart
    @POST("userphoto_set")
    Completable setUserPhoto(@Part() MultipartBody.Part document,
                             @Query("authorization_key") String authorization_key);

    @POST("sync")
    Observable<SyncData> sync(@Body SyncData syncData);
}
