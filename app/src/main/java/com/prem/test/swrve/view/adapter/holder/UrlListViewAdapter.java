package com.prem.test.swrve.view.adapter.holder;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.prem.test.swrve.R;
import com.prem.test.swrve.model.persistent.dao.UrlDao;
import com.prem.test.swrve.model.persistent.dto.UrlDto;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

/**
 * Created by prem on 18/02/2018.
 */

public class UrlListViewAdapter extends RealmRecyclerViewAdapter<UrlDto,UrlListViewHolder> {



    private View.OnClickListener recycleItemListener;
    private List<UrlDto> urls;

    public UrlListViewAdapter(OrderedRealmCollection<UrlDto> urls, View.OnClickListener recycleItemListener){
        super(urls,true);
        this.recycleItemListener = recycleItemListener;
    }

    public void refreshData(List<UrlDto> urls){
        this.urls = urls;
        notifyDataSetChanged();
    }

    @Override
    public UrlListViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycle_url_list_item, viewGroup, false);
        v.setOnClickListener(recycleItemListener);
        return new UrlListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(UrlListViewHolder urlListViewHolder, int position) {
        //final UrlDto urlDto = getItem(position);
        final UrlDto urlDto = this.urls.get(position);
        String urlPath = urlDto.getUrl();
        try {
            final URL url = new URL(urlDto.getUrl());
            urlPath = "..." + url.getFile();
        }catch (MalformedURLException exception){}
        //set tag
        urlListViewHolder.rlWrapperUrl.setTag(urlDto.getIdUrl());
        urlListViewHolder.tvUrl.setText(urlPath);
        urlListViewHolder.tvDateTime.setText(urlDto.getDownloadedAt().toString());
    }

    @Override
    public int getItemCount() {
        int count = (null == urls) ? 0 : urls.size();
        return count;
    }
}

