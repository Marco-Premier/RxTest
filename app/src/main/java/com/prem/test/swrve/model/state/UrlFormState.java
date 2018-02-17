package com.prem.test.swrve.model.state;

import retrofit2.http.Url;

/**
 * Created by prem on 14/02/2018.
 */

public class UrlFormState{

    private boolean isValidUrl;
    private boolean requestInFlight;
    private boolean showSuccess;
    private boolean showFailure;
    private String imagePath;

    protected UrlFormState(){}

    protected UrlFormState(boolean isValidUrl){
        this.isValidUrl = isValidUrl;
    }

    public static UrlFormState defaultState() {
        UrlFormState urlFormState = new UrlFormState(false);
        return urlFormState;
    }

    public static UrlFormState setImagePath(String path){
        UrlFormState urlFormState = new UrlFormState();
        urlFormState.imagePath = path;
        return urlFormState;
    }

    public static UrlFormState setIsValidUrl(boolean isValidUrl){
        return new UrlFormState(isValidUrl);
    }

    public boolean getIsValidUrl(){
        return isValidUrl;
    }

    public String getImagePath(){
        return imagePath;
    }


}
