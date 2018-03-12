package com.prem.test.rxtest.model.persistent.dao;

import com.prem.test.rxtest.model.persistent.realm.SearchHistoryWrapper;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposables;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by prem on 18/02/2018.
 */

public class SearchHistoryDao extends BaseDao {

    public static Observable<List<SearchHistoryWrapper>> getSearchHistoryStore() {
        return Observable.create((ObservableOnSubscribe<List<SearchHistoryWrapper>>) emitter -> {
            Realm realm = Realm.getDefaultInstance();
            final RealmResults<SearchHistoryWrapper> dbUrls = realm.where(SearchHistoryWrapper.class).findAll().sort("idUrl",Sort.DESCENDING);
            final RealmChangeListener<RealmResults<SearchHistoryWrapper>> realmChangeListener = element -> {
                if(element.isLoaded() && !emitter.isDisposed()) {
                    //List<SearchHistoryWrapper> urls = mapFrom(element);
                    if(!emitter.isDisposed()) {
                        emitter.onNext(element);
                    }
                }
            };
            emitter.setDisposable(Disposables.fromAction(() -> {
                if(dbUrls.isValid()) {
                    dbUrls.removeChangeListener(realmChangeListener);
                }
                realm.close();
            }));
            if(dbUrls.isValid()) {
                //First set of data
                emitter.onNext(dbUrls);
            }
            dbUrls.addChangeListener(realmChangeListener);
        })
                .subscribeOn(getScheduler())
                .unsubscribeOn(getScheduler());
    }

    public static List<SearchHistoryWrapper> findAll(){
        try(Realm realm = Realm.getDefaultInstance()) {
            return realm.where(SearchHistoryWrapper.class)
                    .findAll();
        }
    }

    public static SearchHistoryWrapper findUrlById(long idUrl){
        try(Realm realm = Realm.getDefaultInstance()) {
            return realm.where(SearchHistoryWrapper.class)
                    .equalTo("idUrl",idUrl)
                    .findFirst();
        }
    }

    public static long saveSearchHistory(String url){
        try(Realm realm = Realm.getDefaultInstance()) {
            realm.beginTransaction();
            SearchHistoryWrapper searchHistoryWrapper = createUrlDto(url,realm);
            realm.insertOrUpdate(searchHistoryWrapper);
            realm.commitTransaction();
            return searchHistoryWrapper.getIdUrl();
        }
    }

    public static void updateUrl(String newUrl, long idUrl){
        try(Realm realm = Realm.getDefaultInstance()) {
            realm.executeTransaction(realmInstance -> {
                SearchHistoryWrapper searchHistoryWrapper = realmInstance.where(SearchHistoryWrapper.class)
                        .equalTo("idUrl",idUrl)
                        .findFirst();
                searchHistoryWrapper.setUrl(newUrl);
                realmInstance.copyToRealmOrUpdate(searchHistoryWrapper);

            });
        }
    }

    private static SearchHistoryWrapper createUrlDto(String url, Realm realm){
        SearchHistoryWrapper searchHistoryWrapper = new SearchHistoryWrapper();
        Date now = Calendar.getInstance().getTime();
        searchHistoryWrapper.setUrl(url);
        searchHistoryWrapper.setDownloadedAt(now);
        searchHistoryWrapper.setIdUrl(getNextId(realm));
        return searchHistoryWrapper;
    }

    private static int getNextId(Realm realm) {
        try {
            Number number = realm.where(SearchHistoryWrapper.class).max("idUrl");
            if (number != null) {
                return number.intValue() + 1;
            } else {
                return 0;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            return 0;
        }
    }

}
