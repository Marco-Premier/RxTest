package com.prem.test.rxtest.view.event;

/**
 * Created by prem on 16/02/2018.
 */

public class DownloadImageEvent extends UiEvent {

    private String url;

    public DownloadImageEvent(String url){
        this.url = url;
    }

    public String getUrl(){
        return url;
    }

}
