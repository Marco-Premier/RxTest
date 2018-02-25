package com.prem.test.swrve.dagger.components;

import android.content.Context;

import com.prem.test.swrve.MainActivity;
import com.prem.test.swrve.dagger.modules.ApplicationModule;
import com.prem.test.swrve.utils.file.FileManager;
import com.prem.test.swrve.view.adapter.UrlListViewAdapter;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by prem on 23/02/2018.
 */
@Component(modules={ApplicationModule.class})
@Singleton
public interface ApplicationComponent {

    Context context();

    void inject(UrlListViewAdapter urlListViewAdapter);
    void inject(FileManager fileManager);

}