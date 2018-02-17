package com.prem.test.swrve.view.contract;


import android.graphics.Bitmap;

/**
 * Created by prem on 12/02/2018.
 */

public interface UrlFormView extends BaseView {

    void enableDownaloButton();
    void disableDownaloButton();
    void showInvalidUrlError();
    void hideInvalidUrlError();
    void displayImage(Bitmap bitmap);

}
