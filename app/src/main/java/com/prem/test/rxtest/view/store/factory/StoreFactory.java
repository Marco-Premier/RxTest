package com.prem.test.rxtest.view.store.factory;

import com.prem.test.rxtest.view.store.BaseStore;

/**
 * Created by prem on 18/02/2018.
 */

public interface StoreFactory {

    public BaseStore getStore(STORE_TYPE stateType);

}
