package com.prem.test.swrve.presenter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.prem.test.swrve.model.persistent.dao.DownloadImageDao;
import com.prem.test.swrve.view.contract.ImageView;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by prem on 19/02/2018.
 */

public class ImagePresenter extends BasePresenter<ImageView> {

    private CompositeDisposable disposables = new CompositeDisposable();

    @Override
    public void attachView(ImageView view) {
        super.attachView(view);

        disposables.add(DownloadImageDao
                .getDownloadImageState()
                .debounce(200, TimeUnit.MILLISECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(downloadImageState -> {
                    if(null != downloadImageState.getImagePath()){
                        Bitmap bitmap = BitmapFactory.decodeFile(downloadImageState.getImagePath());
                        ifViewAttached(ui -> ui.displayImage(bitmap));
                    }
                }));

        /*disposables.add(DownloadImageStore
                .getInstance()
                .getRelay()
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(downloadImageStore -> {
                    Bitmap bitmap = BitmapFactory.decodeFile(downloadImageStore.getImagePath());
                    ifViewAttached(ui -> ui.displayImage(bitmap));
                }));*/

    }

    @Override
    public void detachView(){
        super.detachView();

        disposables.clear();

    }

}
