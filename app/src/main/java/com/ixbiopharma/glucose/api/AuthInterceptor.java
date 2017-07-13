package com.ixbiopharma.glucose.api;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * AuthInterceptor
 *
 * Created by ivan on 14.05.17.
 */

public class AuthInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder requestBuilder = request.newBuilder();
        requestBuilder.addHeader("X-API-KEY", "V6RGDEGQOSMoWf4YFzPmAbBGepWJaCCioSpnWXg2");
        return chain.proceed(requestBuilder.build());
    }
}
