package com.prem.test.swrve.presenter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.prem.test.swrve.model.persistent.dao.UrlDao;
import com.prem.test.swrve.model.persistent.dto.UrlDto;
import com.prem.test.swrve.view.contract.UrlListView;
import com.prem.test.swrve.view.store.DownloadImageStore;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by prem on 18/02/2018.
 */

public class UrlListPresenter extends BasePresenter<UrlListView> {

    private Observable<List<UrlDto>> urls;
    private CompositeDisposable disposables = new CompositeDisposable();

    @Override
    public void attachView(UrlListView view){
        super.attachView(view);


        disposables.add(UrlDao.getUrlsFromDb().subscribe(model -> {
            Log.i("PREM","ecco il model");
            ifViewAttached(ui -> ui.refreshData(model));
        }));

        disposables.add(DownloadImageStore
                .getInstance()
                .getRelay()
                .debounce(200, TimeUnit.MILLISECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(downloadImageStore -> {
                    if(downloadImageStore.getRequestStatus() == DownloadImageStore.REQUEST_STATUS.IN_FLIGHT){
                        ifViewAttached(ui -> ui.showToast("IN FLIGHT"));
                    }
                    if(downloadImageStore.getRequestStatus() == DownloadImageStore.REQUEST_STATUS.SUCCESS){
                        Bitmap bitmap = BitmapFactory.decodeFile(downloadImageStore.getImagePath());
                        ifViewAttached(ui -> ui.showToast("SUCCESS"));
                    }
                    if(downloadImageStore.getRequestStatus() == DownloadImageStore.REQUEST_STATUS.FAILURE){
                        ifViewAttached(ui -> ui.showToast("FAILURE"));
                    }
                    Log.i("PREM","eccoci");
                }));
    }

    @Override
    public void detachView(){
        super.detachView();

        disposables.clear();

    }

}
