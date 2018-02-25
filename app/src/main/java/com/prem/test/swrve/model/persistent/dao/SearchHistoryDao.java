package com.prem.test.swrve.model.persistent.dao;

import com.prem.test.swrve.model.persistent.store.SearchHistoryStore;

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

public class SearchHistoryDao extends BaseDao{

    public static Observable<List<SearchHistoryStore>> getSearchHistoryStore() {
        return Observable.create((ObservableOnSubscribe<List<SearchHistoryStore>>) emitter -> {
            Realm realm = Realm.getDefaultInstance();
            final RealmResults<SearchHistoryStore> dbUrls = realm.where(SearchHistoryStore.class).findAll().sort("idUrl",Sort.DESCENDING);
            final RealmChangeListener<RealmResults<SearchHistoryStore>> realmChangeListener = element -> {
                if(element.isLoaded() && !emitter.isDisposed()) {
                    //List<SearchHistoryStore> urls = mapFrom(element);
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

    public static List<SearchHistoryStore> findAll(){
        try(Realm realm = Realm.getDefaultInstance()) {
            return realm.where(SearchHistoryStore.class)
                    .findAll();
        }
    }

    public static SearchHistoryStore findUrlById(long idUrl){
        try(Realm realm = Realm.getDefaultInstance()) {
            return realm.where(SearchHistoryStore.class)
                    .equalTo("idUrl",idUrl)
                    .findFirst();
        }
    }

    public static long saveSearchHistory(String url){
        try(Realm realm = Realm.getDefaultInstance()) {
            realm.beginTransaction();
            SearchHistoryStore searchHistoryStore = createUrlDto(url,realm);
            realm.insertOrUpdate(searchHistoryStore);
            realm.commitTransaction();
            return searchHistoryStore.getIdUrl();
        }
    }

    public static void updateUrl(String newUrl, long idUrl){
        try(Realm realm = Realm.getDefaultInstance()) {
            realm.executeTransaction(realmInstance -> {
                SearchHistoryStore searchHistoryStore = realmInstance.where(SearchHistoryStore.class)
                        .equalTo("idUrl",idUrl)
                        .findFirst();
                searchHistoryStore.setUrl(newUrl);
                realmInstance.copyToRealmOrUpdate(searchHistoryStore);

            });
        }
    }

    private static SearchHistoryStore createUrlDto(String url, Realm realm){
        SearchHistoryStore searchHistoryStore = new SearchHistoryStore();
        Date now = Calendar.getInstance().getTime();
        searchHistoryStore.setUrl(url);
        searchHistoryStore.setDownloadedAt(now);
        searchHistoryStore.setIdUrl(getNextId(realm));
        return searchHistoryStore;
    }

    private static int getNextId(Realm realm) {
        try {
            Number number = realm.where(SearchHistoryStore.class).max("idUrl");
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
