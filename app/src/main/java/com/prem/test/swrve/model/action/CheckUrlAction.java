package com.prem.test.swrve.model.action;

/**
 * Created by prem on 14/02/2018.
 */

public class CheckUrlAction extends BaseAction {

    private String url;

    public CheckUrlAction(String url){
        this.url = url;
    }

    public String getUrl(){
        return url;
    }

}
