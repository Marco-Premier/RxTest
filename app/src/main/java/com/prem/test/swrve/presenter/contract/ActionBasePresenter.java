package com.prem.test.swrve.presenter.contract;

import com.hannesdorfmann.mosby3.mvp.MvpView;
import com.prem.test.swrve.model.action.BaseAction;

import io.reactivex.Observable;

/**
 * Created by prem on 25/02/2018.
 */

public abstract class ActionBasePresenter<V extends MvpView> extends BasePresenter<V> {
    protected abstract Observable<BaseAction> setupActions();
}
