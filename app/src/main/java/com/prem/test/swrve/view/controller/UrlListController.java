package com.prem.test.swrve.view.controller;

import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.prem.test.swrve.R;
import com.prem.test.swrve.model.persistent.dto.UrlDto;
import com.prem.test.swrve.presenter.UrlFormPresenter;
import com.prem.test.swrve.presenter.UrlListPresenter;
import com.prem.test.swrve.utils.annotation.Font;
import com.prem.test.swrve.view.adapter.holder.UrlListViewAdapter;
import com.prem.test.swrve.view.contract.UrlFormView;
import com.prem.test.swrve.view.contract.UrlListView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by prem on 18/02/2018.
 */

public class UrlListController extends BaseController<UrlListView,UrlListPresenter> implements UrlListView {

    private Unbinder unbinder;
    private View.OnClickListener recycleItemListener;
    private UrlListViewAdapter urlListViewAdapter;

    @BindView(R.id.rvUrlList) protected RecyclerView rvUrlList;

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        View rootView = inflater.inflate(R.layout.url_list_view, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        setupRecycleView();
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
    public UrlListPresenter createPresenter() {
        return new UrlListPresenter();
    }

    private void setupRecycleView(){
        rvUrlList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rvUrlList.setLayoutManager(llm);
        rvUrlList.addItemDecoration(
                new DividerItemDecoration(getActivity(), LinearLayout.VERTICAL));

        recycleItemListener = new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                Log.i("PREM", "onUrlClick()");

            }
        };

        urlListViewAdapter = new UrlListViewAdapter(null,
                recycleItemListener);
        rvUrlList.setAdapter(urlListViewAdapter);

    }

    @Override
    public void refreshData(List<UrlDto> data) {
        this.urlListViewAdapter.refreshData(data);
    }
}