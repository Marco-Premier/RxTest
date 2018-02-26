package com.prem.test.swrve.model;

import com.prem.test.swrve.model.action.BaseAction;
import com.prem.test.swrve.model.result.BaseResult;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;

/**
 * Created by prem on 23/02/2018.
 */

public abstract class BaseModel {

    public Observable<BaseResult> results;

    public Observable<BaseResult> getResults(){
        return results;
    }

    public abstract ObservableTransformer<BaseAction,BaseResult> setupTransformers();
    public abstract void setup(Observable<BaseAction> actionStream);


}
