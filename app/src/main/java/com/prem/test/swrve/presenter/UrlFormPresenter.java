package com.prem.test.swrve.presenter;

import android.support.annotation.NonNull;
import android.util.Log;

import com.prem.test.swrve.model.UrlFormModel;
import com.prem.test.swrve.model.action.BaseAction;
import com.prem.test.swrve.model.action.CheckUrlAction;
import com.prem.test.swrve.view.contract.UrlFormView;
import com.prem.test.swrve.view.controller.UrlFormController;
import com.prem.test.swrve.view.event.CheckUrlEvent;
import com.prem.test.swrve.view.event.UiEvent;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.functions.Function;

/**
 * Created by prem on 14/02/2018.
 */

public class UrlFormPresenter extends BasePresenter<UrlFormView> {

    private Observable<UiEvent> uiEvents;
    private UrlFormModel model;
    private CompositeDisposable disposables = new CompositeDisposable();

    public UrlFormPresenter(Observable<UiEvent> uiEvents){
        this.uiEvents = uiEvents;
    }

    @Override
    public void attachView(UrlFormView view){
        super.attachView(view);

        model = new UrlFormModel(getActions());
        /*disposables.add(model.getState().subscribe(model -> {
            Log.i("PREM","MODEL RICEVUTO!");
        }));*/

    }

    @Override
    public void detachView(){
        super.detachView();

        disposables.clear();

    }

    private Observable<BaseAction> getActions(){
        return uiEvents.compose(new ObservableTransformer<UiEvent, BaseAction>(){

            //returns new observable that can emit distinct values from source observable
            @Override
            public ObservableSource<BaseAction> apply(Observable<UiEvent> upstream) {
                return upstream.map(new Function<UiEvent, BaseAction>() {
                    @Override
                    public BaseAction apply(UiEvent uiEvent) throws Exception {
                        if(uiEvent instanceof CheckUrlEvent)
                            return new CheckUrlAction(((CheckUrlEvent)uiEvent).getUrl());
                        return null;
                    }
                });
            }});
    }

}
