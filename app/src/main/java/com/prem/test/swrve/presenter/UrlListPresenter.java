package com.prem.test.swrve.presenter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.prem.test.swrve.model.UrlFormModel;
import com.prem.test.swrve.model.persistent.dao.UrlDao;
import com.prem.test.swrve.model.persistent.dto.UrlDto;
import com.prem.test.swrve.view.contract.UrlFormView;
import com.prem.test.swrve.view.contract.UrlListView;

import java.util.List;

import io.reactivex.Observable;
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

    }

    @Override
    public void detachView(){
        super.detachView();

        disposables.clear();

    }

}
