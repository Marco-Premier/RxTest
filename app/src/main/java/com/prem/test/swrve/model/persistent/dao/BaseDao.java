package com.prem.test.swrve.model.persistent.dao;

import android.os.HandlerThread;

import java.util.concurrent.Executors;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by prem on 18/02/2018.
 */

public class BaseDao {

    private static Scheduler scheduler;
    private static HandlerThread handlerThread;

    protected static Scheduler getScheduler(){
        scheduler = (null == scheduler) ? (AndroidSchedulers.mainThread()) : scheduler;
        return scheduler;
    }
}
