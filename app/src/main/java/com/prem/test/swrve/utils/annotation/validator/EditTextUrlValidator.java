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

        String regex = "(.*\\/)*.+\\.(png|jpg|gif|bmp|jpeg|PNG|JPG|GIF|BMP)$";
        return Observable.just(url).map(stringUrl -> (stringUrl.length() > 0 && stringUrl.matches(regex)));

    }

}
