package com.prem.test.swrve.view.controller;

import com.hannesdorfmann.mosby3.conductor.viewstate.MvpViewStateController;
import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;
import com.hannesdorfmann.mosby3.mvp.conductor.MvpController;
import com.hannesdorfmann.mosby3.mvp.viewstate.ViewState;
import com.prem.test.swrve.view.contract.BaseView;

/**
 * Created by prem on 14/02/2018.
 */

public abstract class BaseController<V extends MvpView, T extends MvpPresenter<V>> extends MvpController<V,T> implements BaseView{

    protected final static int DEFAULT_PUSH_TRANSITION_DURATION = 220;

}
