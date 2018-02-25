package com.prem.test.swrve.utils.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.prem.test.swrve.model.persistent.dao.DownloadImageDao;

/**
 * Created by prem on 20/02/2018.
 */

public class LifeCycleService  extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        DownloadImageDao.idle(true);
    }
}
