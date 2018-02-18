package com.prem.test.swrve.view.store.factory;

import com.prem.test.swrve.view.store.BaseStore;
import com.prem.test.swrve.view.store.DownloadImageStore;

/**
 * Created by prem on 18/02/2018.
 */

public class DefaultStoreFactory implements StoreFactory {

    private static DefaultStoreFactory instance;
    private DefaultStoreFactory(){}

    public static DefaultStoreFactory getInstance(){
        if(null == instance)
            instance = new DefaultStoreFactory();
        return instance;
    }

    @Override
    public BaseStore getStore(STORE_TYPE stateType) {
        switch (stateType){
            case DOWNLOAD_IMAGE_STORE:
                return DownloadImageStore.getInstance();
            default:
                throw new IllegalArgumentException("unknown state");
        }
    }

}
