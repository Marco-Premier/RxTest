package com.prem.test.rxtest.view.event;

/**
 * Created by prem on 14/02/2018.
 */

public class CheckUrlEvent extends UiEvent {

    private String url;

    public CheckUrlEvent(String url){
        this.url = url;
    }

    public String getUrl(){
        return url;
    }

}
