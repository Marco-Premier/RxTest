package com.prem.test.swrve;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by prem on 17/02/2018.
 */

public class Swrve extends Application {

    //Fields
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();

    }

    public static Context getContext(){
        return context;
    }

}