package com.prem.test.swrve.view.contract;


/**
 * Created by prem on 12/02/2018.
 */

public interface UrlFormView extends BaseView {

    void enableDownaloButton();
    void disableDownaloButton();
    void showInvalidUrlError();
    void hideInvalidUrlError();
    void showLoadingStatus(String url);
    void showErrorStatus(String url);
    void showSuccessStatus(String url);
    void showIdleStatus();
    void setUrlText(String urlText);

}
