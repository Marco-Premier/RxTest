package com.prem.test.swrve.view.controller;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.bluelinelabs.conductor.Controller;
import com.bluelinelabs.conductor.RouterTransaction;
import com.bluelinelabs.conductor.changehandler.HorizontalChangeHandler;
import com.jakewharton.rxbinding2.view.RxView;
import com.prem.test.swrve.R;
import com.prem.test.swrve.model.persistent.state.DownloadImageState;
import com.prem.test.swrve.model.persistent.store.SearchHistoryStore;
import com.prem.test.swrve.presenter.UrlListPresenter;
import com.prem.test.swrve.utils.annotation.Font;
import com.prem.test.swrve.utils.annotation.processor.FontInjector;
import com.prem.test.swrve.view.adapter.UrlListViewAdapter;
import com.prem.test.swrve.view.contract.UrlListView;
import com.prem.test.swrve.view.event.ImagesListItemTapEvent;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by prem on 18/02/2018.
 */

public class UrlListController extends BaseController<UrlListView,UrlListPresenter> implements UrlListView {

    private PublishSubject<ImagesListItemTapEvent> itemViewClickSubject = PublishSubject.create();
    private Unbinder unbinder;
    private View.OnClickListener onDownloadImageListener;
    private View.OnClickListener onOpenImageListener;
    private UrlListViewAdapter urlListViewAdapter;

    @BindView(R.id.rvUrlList) protected RecyclerView rvUrlList;
    @Font(Font.Fonts.LIGHT) @BindView(R.id.btnStartSearchs) protected Button btnStartSearchs;
    @BindView(R.id.cvWrapperEmptyMessage) protected CardView cvWrapperEmptyMessage;
    @BindView(R.id.toolbar) protected Toolbar toolbar;

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        View rootView = inflater.inflate(R.layout.url_list_view, container, false);

        unbinder = ButterKnife.bind(this, rootView);
        FontInjector.injectFont(this,getActivity());
        setupToolbar(toolbar,true);
        setHasOptionsMenu(true);

        RxView.clicks(btnStartSearchs).subscribe(click -> {
            getRouter().popController(this);
        });

        return rootView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                getRouter().popCurrentController();
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
    public UrlListPresenter createPresenter() {
        return new UrlListPresenter(itemViewClickSubject);
    }

    @Override
    public void setupRecyclerView(DownloadImageState state){
        rvUrlList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rvUrlList.setLayoutManager(llm);
        rvUrlList.addItemDecoration(
                new DividerItemDecoration(getActivity(), LinearLayout.VERTICAL));

        onDownloadImageListener = new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                itemViewClickSubject.onNext(new ImagesListItemTapEvent(Long.parseLong(view.getTag().toString())));
            }
        };

        onOpenImageListener = new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                ImageController imageController = new ImageController();
                imageController.setRetainViewMode(Controller.RetainViewMode.RETAIN_DETACH);
                getRouter().pushController(
                        RouterTransaction.with(imageController)
                                .pushChangeHandler(new HorizontalChangeHandler(DEFAULT_PUSH_TRANSITION_DURATION))
                                .popChangeHandler(new HorizontalChangeHandler(DEFAULT_PUSH_TRANSITION_DURATION)));
            }
        };

        urlListViewAdapter = new UrlListViewAdapter(null,
                onDownloadImageListener,
                onOpenImageListener,
                state);
        rvUrlList.setAdapter(urlListViewAdapter);

    }

    @Override
    public void refreshStore(List<SearchHistoryStore> store) {
        this.urlListViewAdapter.refreshStore(store);
        cvWrapperEmptyMessage.setVisibility(View.GONE);
        rvUrlList.setVisibility(View.VISIBLE);
    }

    @Override
    public void showEmptyMessage() {
        rvUrlList.setVisibility(View.GONE);
        cvWrapperEmptyMessage.setVisibility(View.VISIBLE);
    }

    @Override
    public void refreshState(DownloadImageState state) {
        this.urlListViewAdapter.refreshState(state);
    }

}