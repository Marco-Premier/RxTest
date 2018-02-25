package com.prem.test.swrve.model.persistent.dao;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by prem on 18/02/2018.
 */

public abstract class BaseDao {

    private static Scheduler scheduler;

    protected static Scheduler getScheduler(){
        scheduler = (null == scheduler) ? (AndroidSchedulers.mainThread()) : scheduler;
        return scheduler;
    }

}
