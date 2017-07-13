package com.ixbiopharma.glucose.api;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ixbiopharma.glucose.api.gson.BadDoubleDeserializer;
import com.ixbiopharma.glucose.api.gson.BadIntegerDeserializer;
import com.ixbiopharma.glucose.model.DateNews;
import com.ixbiopharma.glucose.model.News;
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
import com.ixbiopharma.glucose.model.api.PasswordSetRequest;
import com.ixbiopharma.glucose.model.api.RegisterRequest;
import com.ixbiopharma.glucose.model.api.RegisterResponse;
import com.ixbiopharma.glucose.model.api.Response;
import com.ixbiopharma.glucose.model.api.SearchRequest;
import com.ixbiopharma.glucose.model.api.UserProfileRequest;
import com.ixbiopharma.glucose.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * ApiImpl
 * <p>
 * Created by ivan on 14.05.17.
 */

public class ApiImpl {

    private static final long TIMEOUT = TimeUnit.MINUTES.toMillis(1);
    private Api mApi;

    public ApiImpl() {

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Double.class, new BadDoubleDeserializer())
                .registerTypeAdapter(Integer.class, new BadIntegerDeserializer())
                .create();

        OkHttpClient okHttpClient = createOkHttp();
        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(Api.API_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        mApi = retrofit.create(Api.class);
    }

    private static OkHttpClient createOkHttp() {
        HttpLoggingInterceptor.Logger logger = message -> Log.e("logger", message);
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(logger);
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(new AuthInterceptor())
                .addInterceptor(logging)
                .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT, TimeUnit.SECONDS);

        return builder.build();
    }

    public Observable<Response<LoginResponse>> login(String email, String password) {
        return mApi.login(new LoginRequest(email, password));
    }

    public Observable<Response<ForgetPasswordResponse>> forget(String email) {
        return mApi.forgetPassword(new LoginRequest(email, ""));
    }

    public Observable<Response<RegisterResponse>> signup(String first,
                                                         String last,
                                                         String email,
                                                         String password,
                                                         double height,
                                                         double weight,
                                                         int join, int age) {

        return mApi.signup(
                new RegisterRequest(first, last, email, password, height, weight, join, age));
    }

    public Observable<List<DateNews>> getNewsAndAdvice(String token, Date from, Date to) {
        return mApi.getNews(
                new NewsRequest(
                        token,
                        TimeUtils.dateToNewsDateFormat(from),
                        TimeUtils.dateToNewsDateFormat(to)))
                .map(Response::getData);
    }

    public Observable<List<News>> getNewsList(String token, Date from, Date to) {
        return getNewsAndAdvice(token, from, to)
                .map(dateNews -> {
                    List<News> newsList = new ArrayList<>();

                    for (int i = 0; i < dateNews.size(); i++) {
                        DateNews dateNews1 = dateNews.get(i);
                        newsList.addAll(dateNews1.getNews());
                    }

                    return newsList;
                });
    }

    public Observable<NewsDetailWrapper> getNewsDetails(String token, int id) {
        return mApi.getNewsDetail(new NewsDetailRequest(token, id)).map(Response::getData);
    }

    public Observable<UserProfile> getUserProfile(String token) {
        return mApi.getUserProfile(new UserProfileRequest(token)).map(Response::getData);
    }

    public Observable<Response<UserProfile>> setUserProfile(String token, UserProfile
            userProfile) {
        userProfile.setAuthorization_key(token);
        return mApi.setUserProfile(userProfile);
    }

    public Observable<List<News>> search(String token, String query) {
        return mApi.search(new SearchRequest(token, query))
                .map(newsWrapperResponse -> newsWrapperResponse.getData().getNews());
    }

    public Completable setUserPic(MultipartBody.Part document, String token) {
        return mApi.setUserPhoto(document, token);
    }

    public Observable<SyncData> sync(String token, SyncData syncData) {
        syncData.setAuthorization_key(token);
        return mApi.sync(syncData);
    }

    public Observable<DayAdviceResponse> getDayAdvice(String token) {
        return mApi.getDayAdvice(new DayAdviceRequest(token))
                .map(listResponse -> listResponse.getData().get(0));
    }

    public Completable enterNewPassword(String token, String password) {
        return mApi.passwordSet(new PasswordSetRequest(token, password));
    }

    public Completable enterEmailCode(String code, String password) {
        return mApi.resetPasswordEmailCode(code, password);
    }
}
