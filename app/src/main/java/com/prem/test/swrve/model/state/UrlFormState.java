package com.prem.test.swrve.model.state;

/**
 * Created by prem on 14/02/2018.
 */

public class UrlFormState{

    private boolean isValidUrl;

    public UrlFormState(){}

    protected UrlFormState(boolean isValidUrl){
        this.isValidUrl = isValidUrl;
    }

    protected void defaultState() {
        isValidUrl = false;
    }

    public static UrlFormState setIsValidUrl(boolean isValidUrl){
        return new UrlFormState(isValidUrl);
    }

}
