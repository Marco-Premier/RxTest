package com.prem.test.rxtest.presenter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.prem.test.rxtest.RxTest;
import com.prem.test.rxtest.presenter.binders.BindDownloadImageState;
import com.prem.test.rxtest.presenter.contract.BasePresenter;
import com.prem.test.rxtest.view.contract.ImageView;

import javax.inject.Inject;

/**
 * Created by prem on 19/02/2018.
 */

public class ImagePresenter extends BasePresenter<ImageView> {

    @Inject
    BindDownloadImageState stateBinder;

    @Override
    public void attachView(ImageView view) {
        super.attachView(view);

        RxTest.getDefaultComponent().inject(this);

        if(null != stateBinder.getState().getImagePath()){
            Bitmap bitmap = BitmapFactory.decodeFile(stateBinder.getState().getImagePath());
            ifViewAttached(ui -> ui.displayImage(bitmap));
        }

    }

}
