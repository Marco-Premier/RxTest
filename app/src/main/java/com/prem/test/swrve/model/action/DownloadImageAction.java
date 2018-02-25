package com.prem.test.swrve.model.action;

/**
 * Created by prem on 16/02/2018.
 */

public class DownloadImageAction extends BaseAction {

    private String url;
    private Long idUrl;

    public DownloadImageAction(String url, Long idUrl){
        this.url = url;
        this.idUrl = idUrl;
    }

    public String getUrl(){
        return url;
    }

    public Long getIdUrl(){
        return idUrl;
    }

}
