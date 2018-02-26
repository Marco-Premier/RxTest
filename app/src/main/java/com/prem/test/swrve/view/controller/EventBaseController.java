package com.prem.test.swrve.view.controller;

import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.prem.test.swrve.view.contract.BaseView;
import com.prem.test.swrve.view.event.UiEvent;

import io.reactivex.Observable;

/**
 * Created by prem on 25/02/2018.
 */

public abstract class EventBaseController<V extends BaseView,P extends MvpPresenter<V>> extends BaseController<V,P> {

    protected abstract Observable<UiEvent> setupEvents();

}
