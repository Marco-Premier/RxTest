package com.prem.test.rxtest.presenter.binders;

import com.jakewharton.rxrelay2.BehaviorRelay;
import com.prem.test.rxtest.RxTest;
import com.prem.test.rxtest.model.persistent.dao.SearchHistoryDao;
import com.prem.test.rxtest.model.result.BaseResult;
import com.prem.test.rxtest.model.result.CheckUrlResult;
import com.prem.test.rxtest.model.result.DownloadImageResult;
import com.prem.test.rxtest.presenter.state.DownloadImageState;
import com.prem.test.rxtest.presenter.state.enum_type.REQUEST_STATUS;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

/**
 * Created by prem on 25/02/2018.
 */

public class BindDownloadImageState extends BaseBinder {


    @Inject DownloadImageState state;

    @Inject
    public BindDownloadImageState(){
        RxTest.getDefaultComponent().inject(this);
        observableState = BehaviorRelay.createDefault(state);
    }

    @Override
    public Observable<DownloadImageState> bindState(Observable<BaseResult> results) {
        Disposable disposable = results.doOnNext(result -> {
            if (result instanceof CheckUrlResult) {
                state.setResetUrlText(false);
                if (((CheckUrlResult) result).getResult() == CheckUrlResult.CHECK_URL_RESULT.VALID) {
                    state.setValidUrl(true);
                    state.setShowEtError(false);
                } else {
                    state.setValidUrl(false);
                    state.setShowEtError(true);
                }
                observableState.accept(state);
            } else if (result instanceof DownloadImageResult) {

                if (((DownloadImageResult) result).getDownloadStatus() == DownloadImageResult.DOWNLOAD_STATUS.IDLE) {
                    state.setRequestStatus(REQUEST_STATUS.IDLE.toString());
                    if (((DownloadImageResult) result).isResetUrlText()) {
                        state.setShowEtError(false);
                        state.setValidUrl(false);
                        state.setCurrentIdUrl(null);
                        state.setCurrentUrl(null);

                    }
                    observableState.accept(state);
                } else if (((DownloadImageResult) result).getDownloadStatus() == DownloadImageResult.DOWNLOAD_STATUS.IN_FLIGHT) {
                    state.setRequestStatus(REQUEST_STATUS.IN_FLIGHT.toString());
                    state.setCurrentUrl(((DownloadImageResult) result).getUrl());
                    if (null == ((DownloadImageResult) result).getIdUrl()) {
                        //Create search history first
                        state.setCurrentIdUrl(SearchHistoryDao.saveSearchHistory(((DownloadImageResult) result).getUrl()));
                    } else {
                        SearchHistoryDao.updateUrl(((DownloadImageResult) result).getUrl(), ((DownloadImageResult) result).getIdUrl());
                        state.setCurrentIdUrl(((DownloadImageResult) result).getIdUrl());
                    }
                    observableState.accept(state);
                } else if (((DownloadImageResult) result).getDownloadStatus() == DownloadImageResult.DOWNLOAD_STATUS.FAILURE) {
                    state.setRequestStatus(REQUEST_STATUS.FAILURE.toString());
                    observableState.accept(state);
                } else if (((DownloadImageResult) result).getDownloadStatus() == DownloadImageResult.DOWNLOAD_STATUS.SUCCESS) {
                    state.setRequestStatus(REQUEST_STATUS.SUCCESS.toString());
                    state.setImagePath(((DownloadImageResult) result).getImagePaht());
                    observableState.accept(state);
                }
                if (((DownloadImageResult) result).isResetUrlText()) {
                    state.setResetUrlText(true);
                    observableState.accept(state);
                }
            }
        }).subscribe();

        disposables.add(disposable);

        return observableState;

    }

    @Override
    public DownloadImageState getState(){
        return state;
    }

}
