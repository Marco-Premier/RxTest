package com.prem.test.swrve.dagger.components;

import android.content.Context;

import com.prem.test.swrve.dagger.modules.ApplicationModule;
import com.prem.test.swrve.dagger.modules.ModelModule;
import com.prem.test.swrve.dagger.modules.NetworkModule;
import com.prem.test.swrve.dagger.modules.UtilsModule;
import com.prem.test.swrve.dagger.modules.ViewModule;
import com.prem.test.swrve.model.DownloadImageModel;
import com.prem.test.swrve.presenter.ImagePresenter;
import com.prem.test.swrve.presenter.UrlFormPresenter;
import com.prem.test.swrve.presenter.UrlListPresenter;
import com.prem.test.swrve.presenter.binders.BindDownloadImageState;
import com.prem.test.swrve.utils.file.FileManager;
import com.prem.test.swrve.view.adapter.UrlListViewAdapter;
import com.prem.test.swrve.view.controller.UrlListController;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by prem on 23/02/2018.
 */
@Component(modules={ApplicationModule.class, ModelModule.class, NetworkModule.class, UtilsModule.class, ViewModule.class})
@Singleton
public interface DefaultComponent {

    Context context();

    void inject(UrlListViewAdapter urlListViewAdapter);
    void inject(FileManager fileManager);
    void inject(UrlFormPresenter urlFormPresenter);
    void inject(UrlListPresenter urlListPresenter);
    void inject(ImagePresenter imagePresenter);
    void inject(DownloadImageModel downloadImageModel);
    void inject(BindDownloadImageState bindDownloadImageState);
    void inject(UrlListController urlListController);

}