package com.prem.test.swrve.presenter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.prem.test.swrve.Swrve;
import com.prem.test.swrve.presenter.binders.BindDownloadImageState;
import com.prem.test.swrve.presenter.contract.BasePresenter;
import com.prem.test.swrve.view.contract.ImageView;

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

        Swrve.getDefaultComponent().inject(this);

        if(null != stateBinder.getState().getImagePath()){
            Bitmap bitmap = BitmapFactory.decodeFile(stateBinder.getState().getImagePath());
            ifViewAttached(ui -> ui.displayImage(bitmap));
        }

    }

}
