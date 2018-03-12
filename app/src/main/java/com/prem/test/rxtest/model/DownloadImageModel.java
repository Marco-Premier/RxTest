package com.prem.test.rxtest.model;

import com.prem.test.rxtest.RxTest;
import com.prem.test.rxtest.model.action.BaseAction;
import com.prem.test.rxtest.model.action.CheckUrlAction;
import com.prem.test.rxtest.model.action.DownloadImageAction;
import com.prem.test.rxtest.model.action.ShowSearchAction;
import com.prem.test.rxtest.model.result.BaseResult;
import com.prem.test.rxtest.model.result.CheckUrlResult;
import com.prem.test.rxtest.model.result.DownloadImageResult;
import com.prem.test.rxtest.network.endpoint.ApiInterface;
import com.prem.test.rxtest.utils.annotation.validator.EditTextUrlValidator;
import com.prem.test.rxtest.utils.file.FileManager;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by prem on 14/02/2018.
 */

public class DownloadImageModel extends BaseModel {

    @Inject EditTextUrlValidator editTextUrlValidator;
    @Inject ApiInterface apiInterface;
    @Inject FileManager fileManager;

    @Inject
    public DownloadImageModel(){
        RxTest.getDefaultComponent().inject(this);
    }

    @Override
    public ObservableTransformer<BaseAction,BaseResult> setupTransformers(){

        ObservableTransformer<CheckUrlAction,CheckUrlResult> checkUrlTransformer =
                actions -> actions
                        .flatMap(action -> editTextUrlValidator.isValidUrl(action.getUrl()))
                        .map(response -> response ? CheckUrlResult.success() : CheckUrlResult.failure())
                        .onErrorReturn(e -> CheckUrlResult.failure())
                        .observeOn(AndroidSchedulers.mainThread());

        ObservableTransformer<DownloadImageAction,DownloadImageResult> downloadImage =
                actions -> actions
                        .flatMap(action -> apiInterface.downloadFile(action.getUrl()).inter
                        .flatMap(action -> fileManager.saveToDiskRx(action).subscribeOn(Schedulers.io())
                                //.flatMap(fileManager.processResponse())
                                .delay(4, TimeUnit.SECONDS)
                                //.map(response -> DownloadImageResult.success(response.getPath()))
                                .map(response -> DownloadImageResult.success("path"))
                                .onErrorReturn(e -> DownloadImageResult.failure())
                                .observeOn(AndroidSchedulers.mainThread())
                                //.startWith(DownloadImageResult.inFlight(action.getUrl(),action.getIdUrl())));
                                .startWith(DownloadImageResult.inFlight("path",null)));

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
    public void setup(Observable<BaseAction> actionStream) {

        results = actionStream.compose(setupTransformers());

    }

}
