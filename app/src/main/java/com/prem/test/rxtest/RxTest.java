package com.prem.test.rxtest;

import android.app.Application;
import android.content.Context;

import com.prem.test.rxtest.dagger.components.DaggerDefaultComponent;
import com.prem.test.rxtest.dagger.components.DefaultComponent;
import com.prem.test.rxtest.dagger.modules.ApplicationModule;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by prem on 17/02/2018.
 */

public class RxTest extends Application {

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

    public static DefaultComponent getDefaultComponent(){
        return defaultComponent;
    }

}