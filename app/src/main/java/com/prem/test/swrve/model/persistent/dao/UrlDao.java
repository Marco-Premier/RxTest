package com.prem.test.swrve.model.persistent.dao;

import com.prem.test.swrve.model.persistent.dto.UrlDto;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by prem on 18/02/2018.
 */

public class UrlDao extends BaseDao{

    //realm.where(Foo.class).findAllAsync().asObservable()
    public static Observable<List<UrlDto>> getUrlsFromDb() {
        return Observable.create((ObservableOnSubscribe<List<UrlDto>>) emitter -> {
            Realm realm = Realm.getDefaultInstance();
            final RealmResults<UrlDto> dbUrls = realm.where(UrlDto.class).findAll().sort("idUrl",Sort.DESCENDING);
            final RealmChangeListener<RealmResults<UrlDto>> realmChangeListener = element -> {
                if(element.isLoaded() && !emitter.isDisposed()) {
                    //List<UrlDto> urls = mapFrom(element);
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

    public static Disposable saveUrl(String url){
        return Single.create((SingleOnSubscribe<Void>) singleSubscriber -> {
            try(Realm realm = Realm.getDefaultInstance()) {
                realm.executeTransaction(realmInstance -> {
                    realmInstance.insertOrUpdate(createUrlDto(url,realmInstance));
                });
            }
        }).subscribeOn(getScheduler()).subscribe();
    }

    private static UrlDto createUrlDto(String url, Realm realm){
        UrlDto urlDto = new UrlDto();
        Date today = Calendar.getInstance().getTime();
        urlDto.setUrl(url);
        urlDto.setDownloadedAt(today);
        urlDto.setIdUrl(getNextId(realm));
        return urlDto;
    }

    private static int getNextId(Realm realm) {
        try {
            Number number = realm.where(UrlDto.class).max("idUrl");
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
