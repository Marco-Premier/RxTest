package com.prem.test.swrve.model.result;

/**
 * Created by prem on 14/02/2018.
 */

public class CheckUrlResult extends BaseResult {

    public enum CHECK_URL_RESULT {VALID, INVALID};

    private CHECK_URL_RESULT checkUrlResult;

    private CheckUrlResult(CHECK_URL_RESULT checkUrlResult){
        this.checkUrlResult = checkUrlResult;
    }

    public static CheckUrlResult success(){
        return new CheckUrlResult(CHECK_URL_RESULT.VALID);
    }

    public static CheckUrlResult failure(){
        return new CheckUrlResult(CHECK_URL_RESULT.INVALID);
    }

    public CHECK_URL_RESULT getResult(){
        return checkUrlResult;
    }
}
