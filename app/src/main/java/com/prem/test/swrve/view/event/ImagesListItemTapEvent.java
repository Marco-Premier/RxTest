package com.prem.test.swrve.view.event;

/**
 * Created by prem on 19/02/2018.
 */

public class ImagesListItemTapEvent extends UiEvent {

    private long idUrl;

    public ImagesListItemTapEvent(long idUrl){
        this.idUrl = idUrl;
    }

    public long getIdUrl(){
        return idUrl;
    }
}
