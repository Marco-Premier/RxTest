package com.prem.test.swrve.dagger.components;

/**
 * Created by prem on 23/02/2018.
 */

import com.prem.test.swrve.dagger.modules.ApplicationModule;
import com.prem.test.swrve.dagger.modules.NetworkModule;
import com.prem.test.swrve.dagger.modules.UtilsModule;
import com.prem.test.swrve.model.DownloadImageModel;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules={NetworkModule.class, UtilsModule.class, ApplicationModule.class})
@Singleton
public interface NetworkComponent {

    void inject(DownloadImageModel downloadImageModel);

}