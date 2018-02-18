package com.prem.test.swrve.view.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.prem.test.swrve.R;
import com.prem.test.swrve.model.persistent.dto.UrlDto;

/**
 * Created by prem on 18/02/2018.
 */

public class UrlListViewHolder extends RecyclerView.ViewHolder {

    RelativeLayout rlWrapperUrl;
    TextView tvUrl;
    TextView tvDateTime;

    UrlListViewHolder(View itemView) {
        super(itemView);
        rlWrapperUrl = itemView.findViewById(R.id.rlWrapperUrl);
        tvUrl = itemView.findViewById(R.id.tvUrl);
        tvDateTime = itemView.findViewById(R.id.tvDateTime);
    }

}
