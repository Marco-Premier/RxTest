package com.prem.test.swrve.presenter.binders;

import com.jakewharton.rxrelay2.BehaviorRelay;
import com.prem.test.swrve.model.result.BaseResult;
import com.prem.test.swrve.presenter.state.BaseState;
import com.prem.test.swrve.presenter.state.DownloadImageState;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by prem on 25/02/2018.
 */

public abstract class BaseBinder {

    protected BehaviorRelay<DownloadImageState> observableState;
    protected CompositeDisposable disposables = new CompositeDisposable();

    public abstract Observable<DownloadImageState> bindState(Observable<BaseResult> results);
    public abstract BaseState getState();

}
