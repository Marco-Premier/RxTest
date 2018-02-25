package com.prem.test.swrve.network;

import com.prem.test.swrve.network.endpoint.ApiInterface;

import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * Created by prem on 16/02/2018.
 */

public class ServiceProvider {


    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static Retrofit retrofit = new Retrofit.Builder()
                    .client(httpClient.build())
                    .baseUrl("http://www.google.com")
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io())).build();

    public static ApiInterface createService(){
         return retrofit.create(ApiInterface.class);
    }

}
