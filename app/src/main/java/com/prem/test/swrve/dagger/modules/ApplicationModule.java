package com.prem.test.swrve.dagger.modules;

import android.content.Context;
import android.support.annotation.NonNull;

import com.prem.test.swrve.Swrve;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by prem on 23/02/2018.
 */
@Module
public class ApplicationModule {

    private final Context context;

    public ApplicationModule(@NonNull Context context){
        this.context = context;
    }

    @Provides
    @Singleton
    Context provideContext(){
        return context;
    }

}
