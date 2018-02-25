package com.prem.test.swrve.model;

import com.prem.test.swrve.model.action.BaseAction;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

/**
 * Created by prem on 23/02/2018.
 */

public interface BaseModel {

    Disposable subscribe(Observable<BaseAction> actionStream);

}
