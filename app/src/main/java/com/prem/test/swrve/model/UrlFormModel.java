package com.prem.test.swrve.model;

import android.util.Log;

import com.prem.test.swrve.model.action.BaseAction;
import com.prem.test.swrve.model.action.CheckUrlAction;
import com.prem.test.swrve.model.action.DownloadImageAction;
import com.prem.test.swrve.model.persistent.dao.UrlDao;
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
import com.prem.test.swrve.view.state.UrlFormState;

/**
 * Created by prem on 14/02/2018.
 */

public class UrlFormModel {

    private Observable<UrlFormState> urlFormState;
    private EditTextUrlValidator editTextUrlValidator = new EditTextUrlValidator();

    private ObservableTransformer<CheckUrlAction,CheckUrlResult> checkUrlTransformer;
    private ObservableTransformer<CheckUrlAction,CheckUrlResult> checkUrlTransformer2;

    private Observable<BaseResult> results;

    public UrlFormModel(Observable<BaseAction> actionsStream, UrlFormState defaultState){

        results = actionsStream.compose(setupTransformers());

        urlFormState =  results
                .scan(defaultState, (state, result) -> {
                    if(result instanceof CheckUrlResult){
                        if(((CheckUrlResult)result).getResult() == CheckUrlResult.CHECK_URL_RESULT.VALID) {
                            defaultState.setIsValidUrl(true);
                            return defaultState.setShowEtError(false);
                        }else {
                            defaultState.setIsValidUrl(false);
                            return defaultState.setShowEtError(true);
                        }
                    }else if(result instanceof DownloadImageResult){
                        if(((DownloadImageResult)result).getDownloadStatus() == DownloadImageResult.DOWNLOAD_STATUS.IN_FLIGHT)
                            return defaultState.setIsValidUrl(true);
                        else if(((DownloadImageResult)result).getDownloadStatus() == DownloadImageResult.DOWNLOAD_STATUS.SUCCESS)
                            return UrlFormState.setImagePath(((DownloadImageResult) result).getImagePaht());
                    }
                    throw new IllegalArgumentException("wrong result: "+result);
                });



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
                        //.doOnNext(destinationFile -> UrlDao.saveUrl(destinationFile.getPath()))
                        .flatMap(FileManager.processResponse())
                        .doOnNext(destinationFile -> {Log.i("PREM","destinationFile: "+destinationFile);})
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

    public Observable<UrlFormState> getState() {
        return urlFormState;
    }


}
