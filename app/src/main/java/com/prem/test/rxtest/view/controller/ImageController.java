package com.prem.test.rxtest.view.controller;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.prem.test.rxtest.R;
import com.prem.test.rxtest.presenter.ImagePresenter;
import com.prem.test.rxtest.view.contract.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by prem on 19/02/2018.
 */

public class ImageController extends BaseController<ImageView,ImagePresenter> implements ImageView {

    private Unbinder unbinder;

    @BindView(R.id.ivImage) protected android.widget.ImageView ivImage;

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        View rootView = inflater.inflate(R.layout.image_view, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @NonNull
    @Override
    public ImagePresenter createPresenter() {
        return new ImagePresenter();
    }

    @Override
    public void displayImage(Bitmap bitmap) {
        ivImage.setImageBitmap(bitmap);
    }
}
