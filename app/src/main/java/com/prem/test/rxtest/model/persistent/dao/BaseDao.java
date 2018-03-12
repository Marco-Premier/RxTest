package com.prem.test.rxtest.model.persistent.dao;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by prem on 25/02/2018.
 */

public class BaseDao {

    private static Scheduler scheduler;

    protected static Scheduler getScheduler(){
        scheduler = (null == scheduler) ? (AndroidSchedulers.mainThread()) : scheduler;
        return scheduler;
    }

}
