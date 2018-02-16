package com.prem.test.swrve.model;

import android.support.annotation.CheckResult;
import android.util.Log;

import com.prem.test.swrve.model.action.BaseAction;
import com.prem.test.swrve.model.action.CheckUrlAction;
import com.prem.test.swrve.model.result.BaseResult;
import com.prem.test.swrve.model.result.CheckUrlResult;
import com.prem.test.swrve.model.state.BaseState;
import com.prem.test.swrve.model.state.UrlFormState;
import com.prem.test.swrve.utils.annotation.validator.EditTextUrlValidator;
import com.prem.test.swrve.view.event.CheckUrlEvent;
import com.prem.test.swrve.view.event.UiEvent;

import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

/**
 * Created by prem on 14/02/2018.
 */

public class UrlFormModel {

    private Observable<UrlFormState> urlFormState;
    private EditTextUrlValidator editTextUrlValidator = new EditTextUrlValidator();

    private ObservableTransformer<CheckUrlAction,CheckUrlResult> checkUrlTransformer;
    private ObservableTransformer<CheckUrlAction,CheckUrlResult> checkUrlTransformer2;

    private Observable<BaseResult> results;

    public UrlFormModel(Observable<BaseAction> actionsStream){

        Disposable etCardSub2 = actionsStream
                .subscribe(event -> {
                    Log.i("PREM","textChange: ");
                }, error -> Log.i("PREM","ERRORE: "+error.getMessage()));


        results = actionsStream.compose(setupTransformers());

        Disposable etCardSub = results
                .subscribe(event -> {
                    Log.i("PREM","textChange: ");
                }, error -> Log.i("PREM","ERRORE: "+error.getMessage()));

        /*UrlFormState defaultState = new UrlFormState();
        urlFormState =  results
                .onErrorReturn(e -> {Log.i("PREM","ERRORE1!!!"); return null;})
                .scan(defaultState, (state, result) -> {
                    Log.d("PREM","state: "+state.toString());
                    Log.d("PREM","result: "+result.toString());
                    return UrlFormState.setIsValidUrl(true);
                })
                .onErrorReturn(e -> {Log.i("PREM","ERRORE!!!"); return UrlFormState.setIsValidUrl(true);});*/



    }

    private ObservableTransformer<BaseAction,BaseResult> setupTransformers(){

        ObservableTransformer<CheckUrlAction,CheckUrlResult> checkUrlTransformer =
                actions -> actions.doOnNext(next -> {Log.i("PREM","doOnNext1");}).flatMap(action -> editTextUrlValidator.isValidUrl(action.getUrl()))
                        .doOnNext(next -> {Log.i("PREM","doOnNext");})
                        .map(response -> CheckUrlResult.success())
                        .onErrorReturn(e -> CheckUrlResult.failure())
                        .observeOn(AndroidSchedulers.mainThread());

        /*ObservableTransformer<CheckUrlAction,CheckUrlResult> checkUrlTransformer =
                actions -> actions.flatMap(action -> editTextUrlValidator.isValidUrl(action.getUrl())
                        .map(response -> CheckUrlResult.success())
                        .onErrorReturn(e -> {Log.i("PREM","mmmmm2"); return CheckUrlResult.failure();})
                        .observeOn(AndroidSchedulers.mainThread()));*/

        ObservableTransformer<CheckUrlAction,CheckUrlResult> checkUrlTransformer2 =
                actions -> actions.flatMap(action -> editTextUrlValidator.isValidUrl(action.getUrl())
                        .map(response -> CheckUrlResult.success())
                        .onErrorReturn(e -> CheckUrlResult.failure())
                        .observeOn(AndroidSchedulers.mainThread())
                        .startWith(CheckUrlResult.defaultState()));

        ObservableTransformer<BaseAction,BaseResult> resultsTransformers =
                actions -> actions.publish(shared -> Observable.merge(
                        shared.ofType(CheckUrlAction.class).compose(checkUrlTransformer),
                        shared.ofType(CheckUrlAction.class).compose(checkUrlTransformer)
                ));

        return resultsTransformers;
    }

    public Observable<UrlFormState> getState() {
        return urlFormState;
    }


}
