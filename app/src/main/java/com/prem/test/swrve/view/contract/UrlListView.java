package com.prem.test.swrve.view.contract;

import com.prem.test.swrve.model.persistent.state.DownloadImageState;
import com.prem.test.swrve.model.persistent.store.SearchHistoryStore;

import java.util.List;

/**
 * Created by prem on 18/02/2018.
 */

public interface UrlListView extends BaseView {

    public void refreshStore(List<SearchHistoryStore> store);
    public void showEmptyMessage();
    public void refreshState(DownloadImageState state);
    public void setupRecyclerView(DownloadImageState state);

}
