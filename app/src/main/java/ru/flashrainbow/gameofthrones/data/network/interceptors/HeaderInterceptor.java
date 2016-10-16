package ru.flashrainbow.gameofthrones.data.network.interceptors;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

public class HeaderInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        okhttp3.Request original = chain.request();
        okhttp3.Request.Builder requestBuilder = original.newBuilder()
                .header("User-Agent", "GameOfThronesApp");

        okhttp3.Request request = requestBuilder.build();

        return chain.proceed(request);
    }
}
