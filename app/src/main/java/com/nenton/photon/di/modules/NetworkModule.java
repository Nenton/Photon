package com.nenton.photon.di.modules;

import com.nenton.photon.data.network.RestService;
import com.nenton.photon.utils.AppConfig;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetworkModule {

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient() {
        return createClient();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(OkHttpClient okHttpClient) {
        return createRetrofit(okHttpClient);
    }

    @Provides
    @Singleton
    RestService provideRestService(Retrofit retrofit) {
        return retrofit.create(RestService.class);
    }

    private OkHttpClient createClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .retryOnConnectionFailure(true)
                .connectTimeout(AppConfig.MAX_CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(AppConfig.MAX_READ_TIMEOUT, TimeUnit.MILLISECONDS)
                .writeTimeout(AppConfig.MAX_WRITE_TIMEOUT, TimeUnit.MILLISECONDS)
                .build();
    }


    private Retrofit createRetrofit(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(AppConfig.BASE_URL)
                .addConverterFactory(createConverterFactory())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()) // add rx adapter
                .client(okHttpClient)
                .build();
    }

    private Converter.Factory createConverterFactory() {
        return GsonConverterFactory.create();
    }


}
