package com.prem.test.swrve.model.persistent.dao;

import com.prem.test.swrve.model.persistent.state.DownloadImageState;
import com.prem.test.swrve.model.persistent.state.enum_type.REQUEST_STATUS;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposables;
import io.realm.Realm;
import io.realm.RealmChangeListener;

/**
 * Created by prem on 19/02/2018.
 */

public class DownloadImageDao extends BaseDao{

    private static Long ID_STATE = 1L;

    public static Observable<DownloadImageState> getDownloadImageState() {
        return Observable.create((ObservableOnSubscribe<DownloadImageState>) emitter -> {
            Realm realm = Realm.getDefaultInstance();
            final DownloadImageState downloadImageState = realm.where(DownloadImageState.class).findFirst();
            final RealmChangeListener<DownloadImageState> realmChangeListener = element -> {
                if(element.isLoaded() && !emitter.isDisposed()) {
                    //List<SearchHistoryStore> urls = mapFrom(element);
                    if(!emitter.isDisposed()) {
                        emitter.onNext(element);
                    }
                }
            };
            emitter.setDisposable(Disposables.fromAction(() -> {
                if(downloadImageState.isValid()) {
                    downloadImageState.removeChangeListener(realmChangeListener);
                }
                realm.close();
            }));
            if(downloadImageState.isValid()) {
                //First set of data
                emitter.onNext(downloadImageState);
            }
            downloadImageState.addChangeListener(realmChangeListener);
        })
                .subscribeOn(getScheduler())
                .unsubscribeOn(getScheduler());
    }

    public static DownloadImageState defaultState() {
        try(Realm realm = Realm.getDefaultInstance()) {
            DownloadImageState downloadImageState = realm.where(DownloadImageState.class).equalTo("idState",ID_STATE).findFirst();
            if(null == downloadImageState) {
                realm.beginTransaction();
                downloadImageState = new DownloadImageState();
                downloadImageState.setIdState(ID_STATE);
                downloadImageState.setValidUrl(false);
                downloadImageState.setShowEtError(false);
                downloadImageState.setImagePath(null);
                downloadImageState.setRequestStatus(REQUEST_STATUS.IDLE.toString());
                downloadImageState.setCurrentIdUrl(null);
                downloadImageState.setCurrentUrl(null);
                downloadImageState.setResetUrlText(false);
                realm.insertOrUpdate(downloadImageState);
                realm.commitTransaction();
            }
            return downloadImageState;
        }
    }

    public static void idle(boolean isNewSearch){
        try(Realm realm = Realm.getDefaultInstance()) {
            DownloadImageState state = defaultState();
            realm.executeTransaction(realmInstance -> {
                state.setRequestStatus(REQUEST_STATUS.IDLE.toString());
                if(isNewSearch) {
                    state.setShowEtError(false);
                    state.setValidUrl(false);
                    state.setCurrentUrl(null);
                    state.setCurrentIdUrl(null);
                }
                realmInstance.insertOrUpdate(state);
            });
        }
    }

    public static void resetUrlText(){
        try(Realm realm = Realm.getDefaultInstance()) {
            DownloadImageState state = defaultState();
            realm.executeTransaction(realmInstance -> {
                state.setResetUrlText(true);
                realmInstance.insertOrUpdate(state);
            });
        }
    }

    public static void inFlight(String url, Long idUrl){
        try(Realm realm = Realm.getDefaultInstance()) {
            DownloadImageState state = defaultState();
            realm.executeTransaction(realmInstance -> {
                state.setRequestStatus(REQUEST_STATUS.IN_FLIGHT.toString());
                state.setCurrentIdUrl(idUrl);
                state.setCurrentUrl(url);
                realmInstance.insertOrUpdate(state);
            });
        }
    }

    public static void failure(){
        try(Realm realm = Realm.getDefaultInstance()) {
            DownloadImageState state = defaultState();
            realm.executeTransaction(realmInstance -> {
                state.setRequestStatus(REQUEST_STATUS.FAILURE.toString());
                realmInstance.insertOrUpdate(state);
            });
        }
    }

    public static void success(String localPath){
        try(Realm realm = Realm.getDefaultInstance()) {
            DownloadImageState state = defaultState();
            realm.executeTransaction(realmInstance -> {
                state.setRequestStatus(REQUEST_STATUS.SUCCESS.toString());
                state.setImagePath(localPath);
                realmInstance.insertOrUpdate(state);
            });
        }
    }

    public static void validUrl(){
        try(Realm realm = Realm.getDefaultInstance()) {
            DownloadImageState state = defaultState();
            realm.executeTransaction(realmInstance -> {
                state.setValidUrl(true);
                state.setShowEtError(false);
                state.setResetUrlText(false);
                realmInstance.insertOrUpdate(state);
            });
        }
    }

    public static void invalidUrl(){
        try(Realm realm = Realm.getDefaultInstance()) {
            DownloadImageState state = defaultState();
            realm.executeTransaction(realmInstance -> {
                state.setValidUrl(false);
                state.setShowEtError(true);
                state.setResetUrlText(false);
                realmInstance.insertOrUpdate(state);
            });
        }
    }

}
