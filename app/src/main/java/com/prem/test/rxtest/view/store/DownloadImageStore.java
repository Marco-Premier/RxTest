package com.prem.test.rxtest.view.store;

import com.jakewharton.rxrelay2.BehaviorRelay;

import io.reactivex.Observable;

/**
 * Created by prem on 14/02/2018.
 */

public class DownloadImageStore extends BaseStore {

    public enum REQUEST_STATUS{
        IDLE,
        IN_FLIGHT,
        SUCCESS,
        FAILURE
    }
    private boolean isValidUrl;
    private boolean showEtError;
    private REQUEST_STATUS requestStatus;
    private String imagePath;
    private BehaviorRelay<DownloadImageStore> relay;

    private static DownloadImageStore instance;
    protected DownloadImageStore(){}

    public static DownloadImageStore  getInstance(){
        if(null == instance)
            instance = defaultState();
        return instance;
    }

    private static DownloadImageStore defaultState() {
        DownloadImageStore downloadImageStore = new DownloadImageStore();
        downloadImageStore.isValidUrl = false;
        downloadImageStore.showEtError = false;
        downloadImageStore.requestStatus = REQUEST_STATUS.IDLE;
        return downloadImageStore;
    }

    public void setImagePath(String path){
        imagePath = path;
    }

    public void setIsValidUrl(boolean isValidUrl){
        this.isValidUrl = isValidUrl;
        relay.accept(this);
    }

    public void setShowEtError(boolean showEtError) {
        this.showEtError = showEtError;
        relay.accept(this);
    }

    public void setRequestStatus(REQUEST_STATUS requestStatus) {
        this.requestStatus = requestStatus;
        relay.accept(this);
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

    public REQUEST_STATUS getRequestStatus(){
        return requestStatus;
    }

    public Observable<DownloadImageStore> getRelay(){
        if(null == relay) {
            relay = BehaviorRelay.createDefault(this);
        }
        return relay;
    }
}
