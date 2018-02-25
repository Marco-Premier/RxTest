package com.prem.test.swrve;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.prem.test.swrve.dagger.components.ApplicationComponent;
import com.prem.test.swrve.dagger.components.DaggerApplicationComponent;
import com.prem.test.swrve.dagger.components.DaggerModelComponent;
import com.prem.test.swrve.dagger.components.DaggerNetworkComponent;
import com.prem.test.swrve.dagger.components.ModelComponent;
import com.prem.test.swrve.dagger.components.NetworkComponent;
import com.prem.test.swrve.dagger.modules.ApplicationModule;
import com.prem.test.swrve.model.persistent.dao.DownloadImageDao;
import com.prem.test.swrve.utils.service.LifeCycleService;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by prem on 17/02/2018.
 */

public class Swrve extends Application {

    //Fields
    private Context context;
    private static RealmConfiguration realmConfig;
    private static NetworkComponent networkComponent;
    private static ModelComponent modelComponent;
    private static ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

        //applicationComponent = DaggerApplicationComponent.builder().applicationModule(new ApplicationModule(context)).build();
        //networkComponent = DaggerNetworkComponent.builder().build();
        modelComponent = DaggerModelComponent.builder().applicationModule(new ApplicationModule(context)).build();

        Realm.init(context);
        realmConfig = new RealmConfiguration.Builder()
                .schemaVersion(1)
                .build();
        Realm.setDefaultConfiguration(realmConfig);

        //Initialize states if necessary
        DownloadImageDao.defaultState();

        //Start lifecycleservice
        Intent stickyService = new Intent(this, LifeCycleService.class);
        startService(stickyService);

    }

    @Override
    public void finalize(){
        //stop location service
        Log.i("PREM","FINALIZE");
    }

    public static NetworkComponent getNetworkComponent(){
        return networkComponent;
    }

    public static ModelComponent getModelComponent(){
        return modelComponent;
    }

    public static ApplicationComponent getApplicationComponent(){
        return applicationComponent;
    }


}