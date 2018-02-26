package com.prem.test.swrve;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.prem.test.swrve.dagger.components.DaggerDefaultComponent;
import com.prem.test.swrve.dagger.components.DefaultComponent;
import com.prem.test.swrve.dagger.modules.ApplicationModule;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by prem on 17/02/2018.
 */

public class Swrve extends Application {

    //Fields
    private Context context;
    private static RealmConfiguration realmConfig;
    private static DefaultComponent defaultComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

        defaultComponent = DaggerDefaultComponent.builder().applicationModule(new ApplicationModule(context)).build();

        Realm.init(context);
        realmConfig = new RealmConfiguration.Builder()
                .schemaVersion(1)
                .build();
        Realm.setDefaultConfiguration(realmConfig);

    }

    @Override
    public void finalize(){
        //stop location service
        Log.i("PREM","FINALIZE");
    }

    public static DefaultComponent getDefaultComponent(){
        return defaultComponent;
    }

}