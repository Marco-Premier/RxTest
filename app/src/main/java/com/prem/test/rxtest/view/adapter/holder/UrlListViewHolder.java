package com.prem.test.rxtest.view.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.prem.test.rxtest.R;

/**
 * Created by prem on 18/02/2018.
 */

public class UrlListViewHolder extends RecyclerView.ViewHolder {

    public RelativeLayout rlWrapperUrl;
    public TextView tvUrl;
    public TextView tvDateTime;
    public TextView tvActionLabel;
    public ProgressBar pbLoader;

    public UrlListViewHolder(View itemView) {
        super(itemView);
        rlWrapperUrl = itemView.findViewById(R.id.rlWrapperUrl);
        tvUrl = itemView.findViewById(R.id.tvUrl);
        tvDateTime = itemView.findViewById(R.id.tvDateTime);
        tvActionLabel = itemView.findViewById(R.id.tvActionLabel);
        pbLoader = itemView.findViewById(R.id.pbLoader);
    }

}
