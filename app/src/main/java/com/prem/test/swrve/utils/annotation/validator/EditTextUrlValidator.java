package com.prem.test.swrve.utils.annotation.validator;

import android.util.Log;

import io.reactivex.functions.Predicate;
import io.reactivex.Observable;

/**
 * Created by prem on 15/02/2018.
 */

public class EditTextUrlValidator {

    public Observable<String> isValidUrl(String url){
        return Observable.defer(() -> {
            try {
                return Observable.just(checkValue(url));
            } catch (Exception e) {
                return Observable.error(e);
            }
        });
        /*return Observable.just(url)
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(String s) throws Exception {
                        Log.i("PREM","return false, string: "+s);
                        return false;
                    }

                });*/
    }

    public String checkValue(String url) throws Exception{
        return "true";
    }

}
