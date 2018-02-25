package com.prem.test.swrve.model;

import com.prem.test.swrve.DefaultModel;
import com.prem.test.swrve.Swrve;
import com.prem.test.swrve.model.action.BaseAction;
import com.prem.test.swrve.model.action.CheckUrlAction;
import com.prem.test.swrve.model.action.DownloadImageAction;
import com.prem.test.swrve.model.action.ShowSearchAction;
import com.prem.test.swrve.model.persistent.dao.DownloadImageDao;
import com.prem.test.swrve.model.persistent.dao.SearchHistoryDao;
import com.prem.test.swrve.model.persistent.state.DownloadImageState;
import com.prem.test.swrve.model.result.BaseResult;
import com.prem.test.swrve.model.result.CheckUrlResult;
import com.prem.test.swrve.model.result.DownloadImageResult;
import com.prem.test.swrve.network.ServiceProvider;
import com.prem.test.swrve.network.endpoint.ApiInterface;
import com.prem.test.swrve.utils.annotation.validator.EditTextUrlValidator;
import com.prem.test.swrve.utils.file.FileManager;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * Created by prem on 14/02/2018.
 */

public class DownloadImageModel extends DefaultModel implements BaseModel{

    private EditTextUrlValidator editTextUrlValidator = new EditTextUrlValidator();
    private Observable<DownloadImageState> store;
    @Inject ApiInterface apiInterface;
    @Inject FileManager fileManager;

    @Inject
    public DownloadImageModel(){
        Swrve.getModelComponent().inject(this);
    }

    @Override
    public ObservableTransformer<BaseAction,BaseResult> setupTransformers(){

        ObservableTransformer<CheckUrlAction,CheckUrlResult> checkUrlTransformer =
                actions -> actions
                        .flatMap(action -> editTextUrlValidator.isValidUrl(action.getUrl()))
                        .map(response -> response ? CheckUrlResult.success() : CheckUrlResult.failure())
                        .onErrorReturn(e -> CheckUrlResult.failure())
                        .observeOn(AndroidSchedulers.mainThread());

        //ApiInterface apiInterface = ServiceProvider.createService();
        ObservableTransformer<DownloadImageAction,DownloadImageResult> downloadImage =
                actions -> actions
                        .flatMap(action -> apiInterface.downloadFile(action.getUrl())
                                .flatMap(fileManager.processResponse())
                                .delay(4, TimeUnit.SECONDS)
                                .map(response -> DownloadImageResult.success(response.getPath()))
                                .onErrorReturn(e -> DownloadImageResult.failure())
                                .observeOn(AndroidSchedulers.mainThread())
                                .startWith(DownloadImageResult.inFlight(action.getUrl(),action.getIdUrl())));

        ObservableTransformer<ShowSearchAction,DownloadImageResult> showSearchTransformer =
                actions -> actions
                        .map(action -> action.isResetUrlText() ? DownloadImageResult.idle(true) : DownloadImageResult.idle(false))
                        .observeOn(AndroidSchedulers.mainThread());

        ObservableTransformer<BaseAction,BaseResult> resultsTransformers =
                actions -> actions.publish(shared -> Observable.merge(
                        shared.ofType(CheckUrlAction.class).compose(checkUrlTransformer),
                        shared.ofType(DownloadImageAction.class).compose(downloadImage),
                        shared.ofType(ShowSearchAction.class).compose(showSearchTransformer)
                ));

        return resultsTransformers;
    }

    @Override
    public void create(Observable<BaseAction> actionStream) {
        DownloadImageState defaultState = DownloadImageDao.defaultState();
        Observable<BaseResult> results = actionStream.compose(setupTransformers());

        store = results
                .scan(defaultState, (state, result) -> {
                    if (result instanceof CheckUrlResult) {
                        if (((CheckUrlResult) result).getResult() == CheckUrlResult.CHECK_URL_RESULT.VALID) {
                            DownloadImageDao.validUrl();
                        } else {
                            DownloadImageDao.invalidUrl();
                        }
                    } else if (result instanceof DownloadImageResult) {

                        if (((DownloadImageResult) result).getDownloadStatus() == DownloadImageResult.DOWNLOAD_STATUS.IDLE) {
                            DownloadImageDao.idle(((DownloadImageResult) result).isResetUrlText());
                        }else if (((DownloadImageResult) result).getDownloadStatus() == DownloadImageResult.DOWNLOAD_STATUS.IN_FLIGHT) {
                            if (null == ((DownloadImageResult) result).getIdUrl())
                                //Create search history first
                                DownloadImageDao.inFlight(((DownloadImageResult) result).getUrl(), SearchHistoryDao.saveSearchHistory(((DownloadImageResult) result).getUrl()));
                            else {
                                SearchHistoryDao.updateUrl(((DownloadImageResult) result).getUrl(), ((DownloadImageResult) result).getIdUrl());
                                DownloadImageDao.inFlight(((DownloadImageResult) result).getUrl(), ((DownloadImageResult) result).getIdUrl());
                            }
                        } else if (((DownloadImageResult) result).getDownloadStatus() == DownloadImageResult.DOWNLOAD_STATUS.FAILURE) {
                            DownloadImageDao.failure();
                        } else if (((DownloadImageResult) result).getDownloadStatus() == DownloadImageResult.DOWNLOAD_STATUS.SUCCESS) {
                            DownloadImageDao.success(((DownloadImageResult) result).getImagePaht());
                        }
                        if (((DownloadImageResult) result).isResetUrlText()) {
                            DownloadImageDao.resetUrlText();
                        }
                    }
                    return DownloadImageDao.defaultState();

                });
    }

    @Override
    public Disposable subscribe(Observable<BaseAction> actionStream) {
        create(actionStream);
        return store.subscribe();
    }
}
