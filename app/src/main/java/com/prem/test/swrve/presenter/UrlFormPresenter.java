package com.prem.test.swrve.presenter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.util.Log;

import com.prem.test.swrve.model.UrlFormModel;
import com.prem.test.swrve.model.action.BaseAction;
import com.prem.test.swrve.model.action.CheckUrlAction;
import com.prem.test.swrve.model.action.DownloadImageAction;
import com.prem.test.swrve.model.state.UrlFormState;
import com.prem.test.swrve.view.contract.UrlFormView;
import com.prem.test.swrve.view.controller.UrlFormController;
import com.prem.test.swrve.view.event.CheckUrlEvent;
import com.prem.test.swrve.view.event.DownloadImageEvent;
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
        disposables.add(model.getState().subscribe(model -> {
            if(model.getIsValidUrl()) {
                ifViewAttached(ui -> ui.enableDownaloButton());
                ifViewAttached(ui -> ui.hideInvalidUrlError());
            }else{
                ifViewAttached(ui -> ui.disableDownaloButton());
                ifViewAttached(ui -> ui.showInvalidUrlError());

            }
            if(null != model.getImagePath()){
                Bitmap bitmap = BitmapFactory.decodeFile(model.getImagePath());
                ifViewAttached(ui -> ui.displayImage(bitmap));
            }
        }));

    }

    @Override
    public void detachView(){
        super.detachView();

        disposables.clear();

    }

    private Observable<BaseAction> getActions(){
        return uiEvents.compose(new ObservableTransformer<UiEvent, BaseAction>(){

            @Override
            public ObservableSource<BaseAction> apply(Observable<UiEvent> upstream) {
                return upstream.map(new Function<UiEvent, BaseAction>() {
                    @Override
                    public BaseAction apply(UiEvent uiEvent) throws Exception {
                        if(uiEvent instanceof CheckUrlEvent)
                            return new CheckUrlAction(((CheckUrlEvent)uiEvent).getUrl());
                        if(uiEvent instanceof DownloadImageEvent)
                            return new DownloadImageAction(((DownloadImageEvent)uiEvent).getUrl());
                        return null;
                    }
                });
            }});
    }

}
