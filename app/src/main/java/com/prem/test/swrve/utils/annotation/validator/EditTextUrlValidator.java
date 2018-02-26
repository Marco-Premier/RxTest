package com.prem.test.swrve.utils.annotation.validator;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by prem on 15/02/2018.
 */

public class EditTextUrlValidator {

    @Inject
    public EditTextUrlValidator(){}

    public Observable<Boolean> isValidUrl(String url){

        String regex = "(.*\\/)*.+\\.(png|jpg|gif|bmp|jpeg|PNG|JPG|GIF|BMP)$";
        return Observable.just(url).map(stringUrl -> (stringUrl.length() > 0 && stringUrl.matches(regex)));

    }

}
