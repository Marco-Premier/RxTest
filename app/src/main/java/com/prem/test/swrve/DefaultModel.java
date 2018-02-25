package com.prem.test.swrve;

import com.prem.test.swrve.model.action.BaseAction;
import com.prem.test.swrve.model.result.BaseResult;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;

/**
 * Created by prem on 23/02/2018.
 */

public abstract class DefaultModel {

    protected abstract ObservableTransformer<BaseAction,BaseResult> setupTransformers();
    protected abstract void create(Observable<BaseAction> actionStream);

}
