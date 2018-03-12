package com.prem.test.rxtest.view.controller;

import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bluelinelabs.conductor.Controller;
import com.bluelinelabs.conductor.RouterTransaction;
import com.bluelinelabs.conductor.changehandler.HorizontalChangeHandler;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.prem.test.rxtest.R;
import com.prem.test.rxtest.presenter.UrlFormPresenter;
import com.prem.test.rxtest.utils.annotation.Font;
import com.prem.test.rxtest.utils.annotation.processor.FontInjector;
import com.prem.test.rxtest.utils.keyboard.KeyboardManager;
import com.prem.test.rxtest.view.contract.UrlFormView;
import com.prem.test.rxtest.view.event.ChangeUrlEvent;
import com.prem.test.rxtest.view.event.CheckUrlEvent;
import com.prem.test.rxtest.view.event.DownloadImageEvent;
import com.prem.test.rxtest.view.event.NewSearchEvent;
import com.prem.test.rxtest.view.event.UiEvent;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;

/**
 * Created by prem on 12/02/2018.
 */

public class UrlFormController extends EventBaseController<UrlFormView,UrlFormPresenter> implements UrlFormView {

    private Unbinder unbinder;

    @BindView(R.id.toolbar) protected Toolbar toolbar;
    @BindView(R.id.tilUrl) protected TextInputLayout tilUrl;
    @BindView(R.id.rlWrapperSeach) protected RelativeLayout rlWrapperSeach;
    @BindView(R.id.cvWrapperStatus) protected CardView cvWrapperLoadingStatus;
    @BindView(R.id.pbLoader) protected ProgressBar pbLoader;
    @BindView(R.id.llWrapperActions) protected LinearLayout llWrapperActions;
    @Font(Font.Fonts.BOOK) @BindView(R.id.tvIntro) protected TextView tvIntro;
    @Font(Font.Fonts.ROMAN) @BindView(R.id.tvUrl) protected TextView tvUrl;
    @Font(Font.Fonts.ROMAN) @BindView(R.id.etUrl) protected EditText etUrl;
    @Font(Font.Fonts.LIGHT) @BindView(R.id.btnDownload) protected Button btnDownload;
    @Font(Font.Fonts.LIGHT) @BindView(R.id.btnShowImage) protected Button btnShowImage;
    @Font(Font.Fonts.LIGHT) @BindView(R.id.btnNewSearch) protected Button btnNewSearch;
    @Font(Font.Fonts.LIGHT) @BindView(R.id.btnChangeUrl) protected Button btnChangeUrl;

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        View rootView = inflater.inflate(R.layout.url_form_view, container, false);

        unbinder = ButterKnife.bind(this, rootView);
        FontInjector.injectFont(this,getActivity());
        setupToolbar(toolbar,false);
        setHasOptionsMenu(true);

        RxView.clicks(btnShowImage).subscribe(click -> {
            displayImage();
        });

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.url_form_view_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.item_list:
                hideKeyboard(etUrl);
                UrlListController urlListController = new UrlListController();
                urlListController.setRetainViewMode(Controller.RetainViewMode.RETAIN_DETACH);
                getRouter().pushController(
                        RouterTransaction.with(urlListController)
                                .pushChangeHandler(new HorizontalChangeHandler(DEFAULT_PUSH_TRANSITION_DURATION))
                                .popChangeHandler(new HorizontalChangeHandler(DEFAULT_PUSH_TRANSITION_DURATION)));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

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

    @Override
    protected Observable<UiEvent> setupEvents(){

        Observable<CheckUrlEvent> checkUrlEvent = RxTextView.afterTextChangeEvents(etUrl)
                .skip(1)
                .debounce(300, TimeUnit.MILLISECONDS)
                .map(text -> new CheckUrlEvent(etUrl.getText().toString()));

        Observable<DownloadImageEvent> downloadImageEvent = RxView.clicks(btnDownload)
                .doOnNext(__ -> {KeyboardManager.hideKeyboard(getActivity(),etUrl);})
                .map(__ -> new DownloadImageEvent(etUrl.getText().toString()));

        Observable<NewSearchEvent> newSearchEvent = RxView.clicks(btnNewSearch)
                .map(__ -> new NewSearchEvent());

        Observable<ChangeUrlEvent> changeUrlEvent = RxView.clicks(btnChangeUrl)
                .map(__ -> new ChangeUrlEvent());

        return Observable.merge(checkUrlEvent,downloadImageEvent,newSearchEvent,changeUrlEvent);
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
        tilUrl.setError(getActivity().getResources().getString(R.string.ufw_et_url_error));
    }

    @Override
    public void hideInvalidUrlError() {
        tilUrl.setError(null);
    }

    @Override
    public void showLoadingStatus(String url) {
        tvUrl.setText(url);
        btnChangeUrl.setVisibility(View.GONE);
        rlWrapperSeach.setVisibility(View.GONE);
        llWrapperActions.setVisibility(View.GONE);
        tvIntro.setText(getActivity().getResources().getString(R.string.ufw_tv_downaloding));
        pbLoader.setVisibility(View.VISIBLE);
        cvWrapperLoadingStatus.setVisibility(View.VISIBLE);
    }

    @Override
    public void showErrorStatus(String url) {
        tvUrl.setText(url);
        rlWrapperSeach.setVisibility(View.GONE);
        llWrapperActions.setVisibility(View.GONE);
        tvIntro.setText(getActivity().getResources().getString(R.string.ufw_tv_error));
        pbLoader.setVisibility(View.GONE);
        cvWrapperLoadingStatus.setVisibility(View.VISIBLE);
        btnChangeUrl.setVisibility(View.VISIBLE);
    }

    @Override
    public void showSuccessStatus(String url) {
        tvUrl.setText(url);
        btnChangeUrl.setVisibility(View.GONE);
        rlWrapperSeach.setVisibility(View.GONE);
        cvWrapperLoadingStatus.setVisibility(View.VISIBLE);
        llWrapperActions.setVisibility(View.VISIBLE);
        pbLoader.setVisibility(View.GONE);
        tvIntro.setText(getActivity().getResources().getString(R.string.ufw_tv_downaloded));
    }

    @Override
    public void showIdleStatus() {
        cvWrapperLoadingStatus.setVisibility(View.GONE);
        rlWrapperSeach.setVisibility(View.VISIBLE);
    }

    @Override
    public void setUrlText(String urlText) {
        etUrl.setText(urlText);
    }

    public void displayImage() {
        ImageController imageController = new ImageController();
        imageController.setRetainViewMode(Controller.RetainViewMode.RETAIN_DETACH);
        getRouter().pushController(
                RouterTransaction.with(imageController)
                        .pushChangeHandler(new HorizontalChangeHandler(DEFAULT_PUSH_TRANSITION_DURATION))
                        .popChangeHandler(new HorizontalChangeHandler(DEFAULT_PUSH_TRANSITION_DURATION)));
    }

}
