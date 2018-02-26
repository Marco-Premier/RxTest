package com.prem.test.swrve.presenter;

import com.prem.test.swrve.Swrve;
import com.prem.test.swrve.model.BaseModel;
import com.prem.test.swrve.model.action.BaseAction;
import com.prem.test.swrve.model.action.CheckUrlAction;
import com.prem.test.swrve.model.action.DownloadImageAction;
import com.prem.test.swrve.model.action.ShowSearchAction;
import com.prem.test.swrve.presenter.binders.BindDownloadImageState;
import com.prem.test.swrve.presenter.contract.ActionBasePresenter;
import com.prem.test.swrve.presenter.state.enum_type.REQUEST_STATUS;
import com.prem.test.swrve.view.contract.UrlFormView;
import com.prem.test.swrve.view.event.ChangeUrlEvent;
import com.prem.test.swrve.view.event.CheckUrlEvent;
import com.prem.test.swrve.view.event.DownloadImageEvent;
import com.prem.test.swrve.view.event.NewSearchEvent;
import com.prem.test.swrve.view.event.UiEvent;

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

public class UrlFormPresenter extends ActionBasePresenter<UrlFormView> {

    private Observable<UiEvent> uiEvents;

    @Inject
    @Named("DownloadImageModel")
    BaseModel model;

    @Inject
    BindDownloadImageState stateBinder;

    private CompositeDisposable disposables = new CompositeDisposable();

    public UrlFormPresenter(Observable<UiEvent> uiEvents){
        this.uiEvents = uiEvents;
        Swrve.getDefaultComponent().inject(this);
    }

    @Override
    public void attachView(UrlFormView view) {
        super.attachView(view);

        model.setup(setupActions());
        disposables.add(stateBinder.bindState(model.getResults())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(downloadImageState -> {
                            if (downloadImageState.isValidUrl()) {
                                ifViewAttached(ui -> ui.enableDownaloButton());
                            } else {
                                ifViewAttached(ui -> ui.disableDownaloButton());
                            }
                            if (downloadImageState.isShowEtError()) {
                                ifViewAttached(ui -> ui.showInvalidUrlError());
                            } else {
                                ifViewAttached(ui -> ui.hideInvalidUrlError());
                            }
                            if (downloadImageState.getRequestStatus().equals(REQUEST_STATUS.IN_FLIGHT.toString())) {
                                ifViewAttached(ui -> ui.showLoadingStatus(downloadImageState.getCurrentUrl()));
                            }
                            if (downloadImageState.getRequestStatus().equals(REQUEST_STATUS.SUCCESS.toString())) {
                                ifViewAttached(ui -> ui.showSuccessStatus(downloadImageState.getCurrentUrl()));
                            }
                            if (downloadImageState.getRequestStatus().equals(REQUEST_STATUS.FAILURE.toString())) {
                                ifViewAttached(ui -> ui.showErrorStatus(downloadImageState.getCurrentUrl()));
                            }
                            if (downloadImageState.getRequestStatus().equals(REQUEST_STATUS.IDLE.toString())) {
                                ifViewAttached(ui -> ui.showIdleStatus());
                            }
                            if (downloadImageState.isResetUrlText()) {
                                ifViewAttached(ui -> ui.setUrlText(null));
                            }
                            ;
                        }));
    }

    @Override
    public void detachView(){
        super.detachView();

        disposables.clear();

    }

    @Override
    protected Observable<BaseAction> setupActions(){
        return uiEvents.compose(new ObservableTransformer<UiEvent, BaseAction>(){

            @Override
            public ObservableSource<BaseAction> apply(Observable<UiEvent> upstream) {
                return upstream.map(new Function<UiEvent, BaseAction>() {
                    @Override
                    public BaseAction apply(UiEvent uiEvent) throws Exception {
                        if(uiEvent instanceof CheckUrlEvent)
                            return new CheckUrlAction(((CheckUrlEvent)uiEvent).getUrl());
                        if(uiEvent instanceof DownloadImageEvent) {
                            if(null != stateBinder.getState().getCurrentIdUrl())
                                return new DownloadImageAction(((DownloadImageEvent) uiEvent).getUrl(), stateBinder.getState().getCurrentIdUrl());
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
