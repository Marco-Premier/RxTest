package com.prem.test.swrve.dagger.modules;

import com.bluelinelabs.conductor.Controller;
import com.prem.test.swrve.view.controller.ImageController;
import com.prem.test.swrve.view.controller.UrlFormController;
import com.prem.test.swrve.view.controller.UrlListController;

import dagger.Module;
import dagger.Provides;

/**
 * Created by prem on 23/02/2018.
 */
@Module
public class ViewModule {

    @Provides
    public UrlFormController provideUrlFormController() {
        UrlFormController urlFormController = new UrlFormController();
        urlFormController.setRetainViewMode(Controller.RetainViewMode.RETAIN_DETACH);
        return urlFormController;
    }

    @Provides
    public UrlListController provideUrlListController() {
        UrlListController urlListController = new UrlListController();
        urlListController.setRetainViewMode(Controller.RetainViewMode.RETAIN_DETACH);
        return urlListController;
    }

    @Provides
    public ImageController provideImageController() {
        ImageController imageController = new ImageController();
        imageController.setRetainViewMode(Controller.RetainViewMode.RETAIN_DETACH);
        return imageController;
    }

}
