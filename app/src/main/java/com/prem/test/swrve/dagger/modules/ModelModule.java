package com.prem.test.swrve.dagger.modules;

import com.prem.test.swrve.model.BaseModel;
import com.prem.test.swrve.model.DownloadImageModel;
import com.prem.test.swrve.model.persistent.state.DownloadImageState;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by prem on 23/02/2018.
 */
@Module
public class ModelModule {

    //@Named("DownloadImageModel")
    @Provides
    BaseModel provideDownloadImageModel() {
        return new DownloadImageModel();
    }

    @Provides
    @Singleton
    DownloadImageState provideState(){
        return new DownloadImageState();
    }

}
