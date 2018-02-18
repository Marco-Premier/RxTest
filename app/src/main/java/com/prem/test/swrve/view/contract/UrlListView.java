package com.prem.test.swrve.view.contract;

import com.hannesdorfmann.mosby3.mvp.MvpView;
import com.prem.test.swrve.model.persistent.dto.UrlDto;

import java.util.List;

/**
 * Created by prem on 18/02/2018.
 */

public interface UrlListView extends BaseView {

    public void refreshData(List<UrlDto> data);
    public void showToast(String text);

}
