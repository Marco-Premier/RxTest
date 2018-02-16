package com.prem.test.swrve.model.result;

/**
 * Created by prem on 14/02/2018.
 */

public class CheckUrlResult extends BaseResult {

    private boolean isValid;

    private CheckUrlResult(boolean isValid){
        this.isValid = isValid;
    }

    public static CheckUrlResult defaultState(){
        return new CheckUrlResult(false);
    }

    public static CheckUrlResult success(){
        return new CheckUrlResult(true);
    }

    public static CheckUrlResult failure(){
        return new CheckUrlResult(false);
    }

}
