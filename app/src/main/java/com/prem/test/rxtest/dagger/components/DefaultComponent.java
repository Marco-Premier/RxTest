package com.prem.test.rxtest.dagger.components;

import android.content.Context;

import com.prem.test.rxtest.dagger.modules.ApplicationModule;
import com.prem.test.rxtest.dagger.modules.ModelModule;
import com.prem.test.rxtest.dagger.modules.NetworkModule;
import com.prem.test.rxtest.dagger.modules.UtilsModule;
import com.prem.test.rxtest.dagger.modules.ViewModule;
import com.prem.test.rxtest.model.DownloadImageModel;
import com.prem.test.rxtest.presenter.ImagePresenter;
import com.prem.test.rxtest.presenter.UrlFormPresenter;
import com.prem.test.rxtest.presenter.UrlListPresenter;
import com.prem.test.rxtest.presenter.binders.BindDownloadImageState;
import com.prem.test.rxtest.utils.file.FileManager;
import com.prem.test.rxtest.view.adapter.UrlListViewAdapter;
import com.prem.test.rxtest.view.controller.UrlListController;

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