package com.prem.test.swrve.presenter;

import android.util.Log;

import com.prem.test.swrve.Swrve;
import com.prem.test.swrve.model.DownloadImageModel;
import com.prem.test.swrve.model.action.BaseAction;
import com.prem.test.swrve.model.action.CheckUrlAction;
import com.prem.test.swrve.model.action.DownloadImageAction;
import com.prem.test.swrve.model.action.ShowSearchAction;
import com.prem.test.swrve.model.persistent.dao.DownloadImageDao;
import com.prem.test.swrve.model.persistent.state.DownloadImageState;
import com.prem.test.swrve.model.persistent.state.enum_type.REQUEST_STATUS;
import com.prem.test.swrve.view.contract.UrlFormView;
import com.prem.test.swrve.view.event.ChangeUrlEvent;
import com.prem.test.swrve.view.event.CheckUrlEvent;
import com.prem.test.swrve.view.event.DownloadImageEvent;
import com.prem.test.swrve.view.event.NewSearchEvent;
import com.prem.test.swrve.view.event.UiEvent;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Named;

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

    @Inject
    //@Named("DownloadImageModel")
    DownloadImageModel model;

    private CompositeDisposable disposables = new CompositeDisposable();

    public UrlFormPresenter(Observable<UiEvent> uiEvents){
        this.uiEvents = uiEvents;
        Swrve.getModelComponent().inject(this);
        //this.model = new DownloadImageModel();
    }

    @Override
    public void attachView(UrlFormView view) {
        super.attachView(view);

        disposables.add(model.subscribe(getActions()));

        disposables.add(DownloadImageDao
                .getDownloadImageState()
                .debounce(200, TimeUnit.MILLISECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(downloadImageState -> {
                    Log.i("PREM","REQUEST STATUS: "+downloadImageState.getRequestStatus()+" - "+REQUEST_STATUS.SUCCESS.toString());
                    if(downloadImageState.isValidUrl()){
                        ifViewAttached(ui -> ui.enableDownaloButton());
                    }else{
                        ifViewAttached(ui -> ui.disableDownaloButton());
                    }
                    if(downloadImageState.isShowEtError()){
                        ifViewAttached(ui -> ui.showInvalidUrlError());
                    }else{
                        ifViewAttached(ui -> ui.hideInvalidUrlError());
                    }
                    if(downloadImageState.getRequestStatus().equals(REQUEST_STATUS.IN_FLIGHT.toString())){
                        ifViewAttached(ui -> ui.showLoadingStatus(downloadImageState.getCurrentUrl()));
                    }
                    if(downloadImageState.getRequestStatus().equals(REQUEST_STATUS.SUCCESS.toString())){
                        ifViewAttached(ui -> ui.showSuccessStatus(downloadImageState.getCurrentUrl()));
                    }
                    if(downloadImageState.getRequestStatus().equals(REQUEST_STATUS.FAILURE.toString())){
                        ifViewAttached(ui -> ui.showErrorStatus(downloadImageState.getCurrentUrl()));
                    }
                    if(downloadImageState.getRequestStatus().equals(REQUEST_STATUS.IDLE.toString())){
                        ifViewAttached(ui -> ui.showIdleStatus());
                    }
                    if(downloadImageState.isResetUrlText()){
                        ifViewAttached(ui -> ui.setUrlText(null));
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
                        if(uiEvent instanceof DownloadImageEvent) {
                            DownloadImageState downloadImageState = DownloadImageDao.defaultState();
                            Log.i("PREM","id: "+downloadImageState.getCurrentIdUrl());
                            if(null != downloadImageState.getCurrentIdUrl())
                                return new DownloadImageAction(((DownloadImageEvent) uiEvent).getUrl(), downloadImageState.getCurrentIdUrl());
                            else
                                return new DownloadImageAction(((DownloadImageEvent) uiEvent).getUrl(), null);
                        }if(uiEvent instanceof NewSearchEvent)
                            return new ShowSearchAction(true);
                        if(uiEvent instanceof ChangeUrlEvent)
                            return new ShowSearchAction(false);
                        throw new IllegalArgumentException("unknown event");
                    }
                });
            }});
    }

}
