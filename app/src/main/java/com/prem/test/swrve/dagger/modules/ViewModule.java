package com.prem.test.swrve.dagger.modules;

import com.prem.test.swrve.presenter.binders.BindDownloadImageState;
import com.prem.test.swrve.presenter.state.DownloadImageState;
import com.prem.test.swrve.view.event.ImagesListItemTapEvent;

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
