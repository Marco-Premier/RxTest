package com.prem.test.swrve.dagger.modules;

import com.prem.test.swrve.network.endpoint.ApiInterface;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * Created by prem on 23/02/2018.
 */
@Module
public class NetworkModule {

    @Provides
    @Singleton
    OkHttpClient provideHttpClient() {
        return new OkHttpClient().newBuilder().build();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(OkHttpClient okHttpClient) {
        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("http://www.google.com")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io())).build();
        return retrofit;
    }

    @Provides
    @Singleton
    ApiInterface provideNetworkService(Retrofit retrofit) {
        return retrofit.create(ApiInterface.class);
    }

}
