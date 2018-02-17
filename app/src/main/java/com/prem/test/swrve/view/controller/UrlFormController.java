package com.prem.test.swrve.view.controller;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bluelinelabs.conductor.Controller;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.prem.test.swrve.R;
import com.prem.test.swrve.model.UrlFormModel;
import com.prem.test.swrve.presenter.UrlFormPresenter;
import com.prem.test.swrve.utils.annotation.Font;
import com.prem.test.swrve.view.contract.UrlFormView;
import com.prem.test.swrve.view.event.CheckUrlEvent;
import com.prem.test.swrve.view.event.DownloadImageEvent;
import com.prem.test.swrve.view.event.UiEvent;

import org.reactivestreams.Subscription;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

/**
 * Created by prem on 12/02/2018.
 */

public class UrlFormController extends BaseController<UrlFormView,UrlFormPresenter> implements UrlFormView {

    private Unbinder unbinder;

    @BindView(R.id.tilEmail) protected TextInputLayout tilEmail;
    @BindView(R.id.ivImage) protected ImageView ivImage;
    @Font(Font.Fonts.ROMAN) @BindView(R.id.etEmail) protected EditText etEmail;
    @Font(Font.Fonts.LIGHT) @BindView(R.id.btnDownload) protected Button btnDownload;

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        View rootView = inflater.inflate(R.layout.url_form_view, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    protected void onAttach(@NonNull View view) {

    }

    @Override
    protected void onDetach(@NonNull View view) {

    }

    @Override
    protected void onDestroyView(View view) {
        unbinder.unbind();
    }

    @NonNull
    @Override
    public UrlFormPresenter createPresenter() {
        return new UrlFormPresenter(setupEvents());
    }

    private Observable<UiEvent> setupEvents(){
        Observable<CheckUrlEvent> checkUrlEvent = RxTextView.afterTextChangeEvents(etEmail)
                .map(text -> new CheckUrlEvent(etEmail.getText().toString()));

        Observable<DownloadImageEvent> downloadImageEvent = RxView.clicks(btnDownload)
                .map(__ -> new DownloadImageEvent(etEmail.getText().toString()));

        return Observable.merge(checkUrlEvent,downloadImageEvent);
    }

    @Override
    public void enableDownaloButton() {
        btnDownload.setEnabled(true);
    }

    @Override
    public void disableDownaloButton() {
        btnDownload.setEnabled(false);
    }

    @Override
    public void showInvalidUrlError() {
        tilEmail.setError(getActivity().getResources().getString(R.string.ufw_et_url_error));
    }

    @Override
    public void hideInvalidUrlError() {
        tilEmail.setError(null);
    }

    @Override
    public void displayImage(Bitmap bitmap) {
        ivImage.setImageBitmap(bitmap);
    }
}
