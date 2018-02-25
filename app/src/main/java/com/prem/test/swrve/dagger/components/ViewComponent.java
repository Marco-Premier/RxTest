package com.prem.test.swrve.dagger.components;

import com.prem.test.swrve.MainActivity;
import com.prem.test.swrve.dagger.modules.ViewModule;
import com.prem.test.swrve.view.controller.UrlFormController;
import com.prem.test.swrve.view.controller.UrlListController;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by prem on 23/02/2018.
 */
@Singleton
@Component(modules={ViewModule.class})
public interface ViewComponent {

    void inject(MainActivity mainActivity);
    void inject(UrlFormController urlFormController);
    void inject(UrlListController urlListController);
}