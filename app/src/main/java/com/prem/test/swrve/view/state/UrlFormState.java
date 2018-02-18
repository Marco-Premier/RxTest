package com.prem.test.swrve.view.state;

/**
 * Created by prem on 14/02/2018.
 */

public class UrlFormState{

    private boolean isValidUrl;
    private boolean requestInFlight;
    private boolean showSuccess;
    private boolean showFailure;
    private boolean showEtError;
    private String imagePath;

    protected UrlFormState(){}

    protected UrlFormState(boolean isValidUrl){
        this.isValidUrl = isValidUrl;
    }

    public static UrlFormState defaultState() {
        UrlFormState urlFormState = new UrlFormState(false);
        urlFormState.showEtError = false;
        return urlFormState;
    }

    public static UrlFormState setImagePath(String path){
        UrlFormState urlFormState = new UrlFormState();
        urlFormState.imagePath = path;
        return urlFormState;
    }

    public UrlFormState setIsValidUrl(boolean isValidUrl){
        this.isValidUrl = isValidUrl;
        return this;
    }

    public boolean getIsValidUrl(){
        return isValidUrl;
    }

    public String getImagePath(){
        return imagePath;
    }

    public boolean getShowEtError() {
        return showEtError;
    }

    public UrlFormState setShowEtError(boolean showEtError) {
        this.showEtError = showEtError;
        return this;
    }
}
