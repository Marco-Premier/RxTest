package com.prem.test.swrve.model.action;

/**
 * Created by prem on 16/02/2018.
 */

public class DownloadImageAction extends BaseAction {

    private String url;

    public DownloadImageAction(String url){
        this.url = url;
    }

    public String getUrl(){
        return url;
    }

}
