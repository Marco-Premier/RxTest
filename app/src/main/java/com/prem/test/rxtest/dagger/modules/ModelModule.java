package com.prem.test.rxtest.dagger.modules;

import com.prem.test.rxtest.model.BaseModel;
import com.prem.test.rxtest.model.DownloadImageModel;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * Created by prem on 23/02/2018.
 */
@Module
public class ModelModule {

    @Named("DownloadImageModel")
    @Provides
    BaseModel provideDownloadImageModel() {
        return new DownloadImageModel();
    }

}
