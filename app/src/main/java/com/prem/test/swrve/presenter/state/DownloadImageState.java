package com.prem.test.swrve.presenter.state;

import com.prem.test.swrve.presenter.state.enum_type.REQUEST_STATUS;

import javax.inject.Inject;

/**
 * Created by prem on 19/02/2018.
 */

public class DownloadImageState extends BaseState {

    private String requestStatus;
    private boolean isValidUrl;
    private boolean showEtError;
    private String imagePath;
    private Long currentIdUrl;
    private String currentUrl;
    private boolean resetUrlText;

    @Inject
    public DownloadImageState(){
        isValidUrl = false;
        showEtError = false;
        imagePath = null;
        requestStatus = REQUEST_STATUS.IDLE.toString();
        currentIdUrl = null;
        currentUrl = null;
        resetUrlText = false;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }

    public boolean isValidUrl() {
        return isValidUrl;
    }

    public void setValidUrl(boolean validUrl) {
        isValidUrl = validUrl;
    }

    public boolean isShowEtError() {
        return showEtError;
    }

    public void setShowEtError(boolean showEtError) {
        this.showEtError = showEtError;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Long getCurrentIdUrl() {
        return currentIdUrl;
    }

    public void setCurrentIdUrl(Long currentIdUrl) {
        this.currentIdUrl = currentIdUrl;
    }

    public String getCurrentUrl() {
        return currentUrl;
    }

    public void setCurrentUrl(String currentUrl) {
        this.currentUrl = currentUrl;
    }

    public boolean isResetUrlText() {
        return resetUrlText;
    }

    public void setResetUrlText(boolean resetUrlText) {
        this.resetUrlText = resetUrlText;
    }
}
