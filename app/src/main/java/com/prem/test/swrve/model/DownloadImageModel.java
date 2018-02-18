package com.prem.test.swrve.model;

import android.util.Log;

import com.prem.test.swrve.model.action.BaseAction;
import com.prem.test.swrve.model.action.CheckUrlAction;
import com.prem.test.swrve.model.action.DownloadImageAction;
import com.prem.test.swrve.model.result.BaseResult;
import com.prem.test.swrve.model.result.CheckUrlResult;
import com.prem.test.swrve.model.result.DownloadImageResult;
import com.prem.test.swrve.network.ServiceProvider;
import com.prem.test.swrve.network.endpoint.ApiInterface;
import com.prem.test.swrve.utils.annotation.validator.EditTextUrlValidator;
import com.prem.test.swrve.utils.file.FileManager;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

import com.prem.test.swrve.view.store.DownloadImageStore;
import com.prem.test.swrve.view.store.factory.DefaultStoreFactory;
import com.prem.test.swrve.view.store.factory.STORE_TYPE;

import java.util.concurrent.TimeUnit;

/**
 * Created by prem on 14/02/2018.
 */

public class DownloadImageModel {

    private EditTextUrlValidator editTextUrlValidator = new EditTextUrlValidator();

    private Observable<DownloadImageStore> store;

    public DownloadImageModel(Observable<BaseAction> actionsStream){

        DownloadImageStore defaultStore = (DownloadImageStore)DefaultStoreFactory.getInstance().getStore(STORE_TYPE.DOWNLOAD_IMAGE_STORE);
        Observable<BaseResult> results = actionsStream.compose(setupTransformers());

        store = results
                .scan(defaultStore, (state, result) -> {
                    if(result instanceof CheckUrlResult){
                        if(((CheckUrlResult)result).getResult() == CheckUrlResult.CHECK_URL_RESULT.VALID) {
                            defaultStore.setIsValidUrl(true);
                            defaultStore.setShowEtError(false);
                            return defaultStore;
                        }else {
                            defaultStore.setIsValidUrl(false);
                            defaultStore.setShowEtError(true);
                            return defaultStore;
                        }
                    }else if(result instanceof DownloadImageResult){
                        if(((DownloadImageResult)result).getDownloadStatus() == DownloadImageResult.DOWNLOAD_STATUS.IN_FLIGHT) {
                            defaultStore.setRequestStatus(DownloadImageStore.REQUEST_STATUS.IN_FLIGHT);
                            return defaultStore;
                        }else if(((DownloadImageResult)result).getDownloadStatus() == DownloadImageResult.DOWNLOAD_STATUS.FAILURE) {
                            defaultStore.setRequestStatus(DownloadImageStore.REQUEST_STATUS.FAILURE);
                            return defaultStore;
                        }else if(((DownloadImageResult)result).getDownloadStatus() == DownloadImageResult.DOWNLOAD_STATUS.SUCCESS) {
                            defaultStore.setRequestStatus(DownloadImageStore.REQUEST_STATUS.SUCCESS);
                            defaultStore.setImagePath(((DownloadImageResult) result).getImagePaht());
                            return defaultStore;
                        }
                    }
                    throw new IllegalArgumentException("wrong result: "+result);
                });

    }

    public Disposable subscribe(){
        return store.subscribe();
    }

    private ObservableTransformer<BaseAction,BaseResult> setupTransformers(){

        ObservableTransformer<CheckUrlAction,CheckUrlResult> checkUrlTransformer =
                actions -> actions.flatMap(action -> editTextUrlValidator.isValidUrl(action.getUrl()))
                        .map(response -> response ? CheckUrlResult.success() : CheckUrlResult.failure())
                        .onErrorReturn(e -> CheckUrlResult.failure())
                        .observeOn(AndroidSchedulers.mainThread());

        ApiInterface apiInterface = ServiceProvider.createService();
        ObservableTransformer<DownloadImageAction,DownloadImageResult> downloadImage =
                actions -> actions.flatMap(action -> apiInterface.downloadFile(action.getUrl())
                        .delay(5, TimeUnit.SECONDS)
                        .flatMap(FileManager.processResponse())
                        .map(response -> DownloadImageResult.success(response.getPath()))
                        .onErrorReturn(e -> DownloadImageResult.failure())
                        .observeOn(AndroidSchedulers.mainThread())
                        .startWith(DownloadImageResult.inFlight()));

        ObservableTransformer<BaseAction,BaseResult> resultsTransformers =
                actions -> actions.publish(shared -> Observable.merge(
                        shared.ofType(CheckUrlAction.class).compose(checkUrlTransformer),
                        shared.ofType(DownloadImageAction.class).compose(downloadImage)
                ));

        return resultsTransformers;
    }

}
