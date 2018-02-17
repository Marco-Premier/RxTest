package com.prem.test.swrve.utils.annotation.validator;

import android.util.Log;

import com.prem.test.swrve.model.result.CheckUrlResult;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Predicate;
import io.reactivex.Observable;

/**
 * Created by prem on 15/02/2018.
 */

public class EditTextUrlValidator {

    public Observable<Boolean> isValidUrl(String url){

        String regex = "^(https?:\\/\\/)?([\\da-z\\.-]+)\\.([a-z\\.]{2,6})([\\/\\w \\.-]*)*.(?:jpg|gif|png)\\/?$";
        return Observable.just(url).map(stringUrl -> (stringUrl.length() > 0 && stringUrl.matches(regex)));

    }


}
