package com.prem.test.swrve.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.prem.test.swrve.R;
import com.prem.test.swrve.Swrve;
import com.prem.test.swrve.model.persistent.realm.SearchHistoryWrapper;
import com.prem.test.swrve.presenter.state.DownloadImageState;
import com.prem.test.swrve.presenter.state.enum_type.REQUEST_STATUS;
import com.prem.test.swrve.view.adapter.holder.UrlListViewHolder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.inject.Inject;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

/**
 * Created by prem on 18/02/2018.
 */

public class UrlListViewAdapter extends RealmRecyclerViewAdapter<SearchHistoryWrapper,UrlListViewHolder> {


    private View.OnClickListener onDownloadImageListener;
    private View.OnClickListener onOpenImageListener;
    private List<SearchHistoryWrapper> urls;
    private DownloadImageState downloadImageState;
    @Inject Context context;

    public UrlListViewAdapter(OrderedRealmCollection<SearchHistoryWrapper> urls, View.OnClickListener onDownloadImageListener, View.OnClickListener onOpenImageListener, DownloadImageState downloadImageState){
        super(urls,true);

        Swrve.getDefaultComponent().inject(this);

        this.onDownloadImageListener = onDownloadImageListener;
        this.onOpenImageListener = onOpenImageListener;
        this.downloadImageState = downloadImageState;
    }

    public void refreshStore(List<SearchHistoryWrapper> urls){
        this.urls = urls;
        notifyDataSetChanged();
    }

    public void refreshState(DownloadImageState downloadImageState){
        this.downloadImageState = downloadImageState;
        notifyDataSetChanged();
    }

    @Override
    public UrlListViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.recycle_url_list_item, viewGroup, false);

        return new UrlListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(UrlListViewHolder urlListViewHolder, int position) {

        final SearchHistoryWrapper searchHistoryWrapper = this.urls.get(position);
        String urlPath = "..." + searchHistoryWrapper.getUrl().substring(searchHistoryWrapper.getUrl().lastIndexOf("/")+1);
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        urlListViewHolder.rlWrapperUrl.setTag(searchHistoryWrapper.getIdUrl());
        urlListViewHolder.tvUrl.setText(urlPath);
        urlListViewHolder.tvDateTime.setText(df.format(searchHistoryWrapper.getDownloadedAt()));

        hideButtons(urlListViewHolder);

        if (downloadImageState.getRequestStatus().equals(REQUEST_STATUS.IN_FLIGHT.toString()) && downloadImageState.getCurrentIdUrl().equals(searchHistoryWrapper.getIdUrl())) {
            urlListViewHolder.pbLoader.setVisibility(View.VISIBLE);
        }else if (!downloadImageState.getRequestStatus().equals(REQUEST_STATUS.IN_FLIGHT.toString())){
            urlListViewHolder.tvActionLabel.setVisibility(View.VISIBLE);
            if (downloadImageState.getRequestStatus().equals(REQUEST_STATUS.FAILURE.toString()) && downloadImageState.getCurrentIdUrl().equals(searchHistoryWrapper.getIdUrl())) {
                urlListViewHolder.tvActionLabel.setText(context.getResources().getString(R.string.tv_label_error));
                urlListViewHolder.tvActionLabel.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
            }else if (null != downloadImageState.getCurrentIdUrl() && downloadImageState.getCurrentIdUrl().equals(searchHistoryWrapper.getIdUrl())){
                urlListViewHolder.itemView.setOnClickListener(onOpenImageListener);
                urlListViewHolder.tvActionLabel.setText(context.getResources().getString(R.string.tv_label_open_image));
                urlListViewHolder.tvActionLabel.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));
            }else{
                urlListViewHolder.itemView.setOnClickListener(onDownloadImageListener);
                urlListViewHolder.tvActionLabel.setText(context.getResources().getString(R.string.tv_label_download_image));
                urlListViewHolder.tvActionLabel.setTextColor(context.getResources().getColor(android.R.color.holo_orange_dark));
            }
        }

    }

    private void hideButtons(UrlListViewHolder urlListViewHolder){
        urlListViewHolder.tvActionLabel.setVisibility(View.GONE);
        urlListViewHolder.pbLoader.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        int count = (null == urls) ? 0 : urls.size();
        return count;
    }
}

