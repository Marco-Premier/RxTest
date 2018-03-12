package com.prem.test.rxtest.dagger.modules;

import com.prem.test.rxtest.presenter.binders.BindDownloadImageState;
import com.prem.test.rxtest.presenter.state.DownloadImageState;
import com.prem.test.rxtest.view.event.ImagesListItemTapEvent;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by prem on 23/02/2018.
 */
@Module
public class ViewModule {

    @Provides
    public PublishSubject<ImagesListItemTapEvent> provideImagesListTapPublisher() {
        return PublishSubject.create();
    }

    @Provides
    @Singleton
    DownloadImageState provideState(){
        return new DownloadImageState();
    }

    @Provides
    @Singleton
    BindDownloadImageState provideBinder(){
        return new BindDownloadImageState();
    }

}
