package com.prem.test.rxtest.view.contract;

import com.prem.test.rxtest.model.persistent.realm.SearchHistoryWrapper;
import com.prem.test.rxtest.presenter.state.DownloadImageState;

import java.util.List;

/**
 * Created by prem on 18/02/2018.
 */

public interface UrlListView extends BaseView {

    public void refreshStore(List<SearchHistoryWrapper> store);
    public void showEmptyMessage();
    public void refreshState(DownloadImageState state);
    public void setupRecyclerView(DownloadImageState state);

}
