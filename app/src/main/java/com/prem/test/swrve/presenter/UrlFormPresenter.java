package com.prem.test.swrve.presenter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.prem.test.swrve.model.DownloadImageModel;
import com.prem.test.swrve.model.action.BaseAction;
import com.prem.test.swrve.model.action.CheckUrlAction;
import com.prem.test.swrve.model.action.DownloadImageAction;
import com.prem.test.swrve.view.store.DownloadImageStore;
import com.prem.test.swrve.view.contract.UrlFormView;
import com.prem.test.swrve.view.event.CheckUrlEvent;
import com.prem.test.swrve.view.event.DownloadImageEvent;
import com.prem.test.swrve.view.event.UiEvent;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;

/**
 * Created by prem on 14/02/2018.
 */

public class UrlFormPresenter extends BasePresenter<UrlFormView> {

    private Observable<UiEvent> uiEvents;
    private DownloadImageModel model;
    private CompositeDisposable disposables = new CompositeDisposable();

    public UrlFormPresenter(Observable<UiEvent> uiEvents){
        this.uiEvents = uiEvents;
        model = new DownloadImageModel(getActions());
    }

    @Override
    public void attachView(UrlFormView view) {
        super.attachView(view);

        disposables.add(model.subscribe());

        disposables.add(DownloadImageStore
                .getInstance()
                .getRelay()
                .debounce(200, TimeUnit.MILLISECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(downloadImageStore -> {
                    if(downloadImageStore.getIsValidUrl()){
                        ifViewAttached(ui -> ui.hideInvalidUrlError());
                        ifViewAttached(ui -> ui.enableDownaloButton());
                    }else{
                        ifViewAttached(ui -> ui.showInvalidUrlError());
                        ifViewAttached(ui -> ui.disableDownaloButton());
                    }
                    if(downloadImageStore.getRequestStatus() == DownloadImageStore.REQUEST_STATUS.IN_FLIGHT){
                        ifViewAttached(ui -> ui.showToast("IN FLIGHT"));
                    }
                    if(downloadImageStore.getRequestStatus() == DownloadImageStore.REQUEST_STATUS.SUCCESS){
                        Bitmap bitmap = BitmapFactory.decodeFile(downloadImageStore.getImagePath());
                        ifViewAttached(ui -> ui.showToast("SUCCESS"));
                        ifViewAttached(ui -> ui.displayImage(bitmap));
                    }
                    if(downloadImageStore.getRequestStatus() == DownloadImageStore.REQUEST_STATUS.FAILURE){
                        ifViewAttached(ui -> ui.showToast("FAILURE"));
                    }
                    Log.i("PREM","eccoci");
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
