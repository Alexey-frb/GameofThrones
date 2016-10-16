package ru.flashrainbow.gameofthrones.data.network;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.flashrainbow.gameofthrones.data.network.interceptors.HeaderInterceptor;
import ru.flashrainbow.gameofthrones.utils.AppConfig;
import ru.flashrainbow.gameofthrones.utils.DevApplication;

public class ServiceGenerator {

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static Retrofit.Builder sBuilder =
            new Retrofit.Builder()
                    .baseUrl(AppConfig.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());

    public static <S> S createService(Class<S> serviceClass) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        httpClient.addInterceptor(new HeaderInterceptor());
        httpClient.addInterceptor(logging);
        httpClient.addNetworkInterceptor(new StethoInterceptor());

        httpClient.cache(new Cache(DevApplication.getContext().getCacheDir(), Integer.MAX_VALUE));
        httpClient.connectTimeout(AppConfig.MAX_CONNECT_TIMEOUT, TimeUnit.MILLISECONDS);
        httpClient.readTimeout(AppConfig.MAX_READ_TIMEOUT, TimeUnit.MILLISECONDS);

        Retrofit retrofit = sBuilder
                .client(httpClient.build())
                .build();

        return retrofit.create(serviceClass);
    }
}
