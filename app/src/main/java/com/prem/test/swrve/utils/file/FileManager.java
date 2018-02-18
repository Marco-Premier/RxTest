package com.prem.test.swrve.utils.file;

import android.content.Context;
import android.util.Log;

import com.prem.test.swrve.Swrve;
import com.prem.test.swrve.model.persistent.dao.UrlDao;

import org.reactivestreams.Subscriber;

import java.io.File;
import java.io.IOException;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import okhttp3.Headers;
import okhttp3.ResponseBody;
import okio.BufferedSink;
import okio.Okio;
import retrofit2.Response;

/**
 * Created by prem on 16/02/2018.
 */

public class FileManager {

    public static Function<Response<ResponseBody>, Observable<File>> processResponse() {
        return new Function<Response<ResponseBody>, Observable<File>>() {
            @Override
            public Observable<File> apply(Response<ResponseBody> responseBodyResponse) throws Exception {
                UrlDao.saveUrl(responseBodyResponse.raw().networkResponse().request().url().toString());
                return saveToDiskRx(responseBodyResponse);
            }
        };
    }

    private static Observable<File> saveToDiskRx(final Response<ResponseBody> response) {
        return Observable.create(new ObservableOnSubscribe<File>() {
            @Override
            public void subscribe(ObservableEmitter<File> emitter) throws Exception {
                try {
                    Log.i("PREM","SAVE FILE");
                    Headers headerList = response.headers();
                    for (int i = 0; i  < headerList.size(); i++){
                        Log.i("PREM","HEADER: "+headerList.name(i));
                    }
                    String header = response.headers().get("Content-Type");
                    Log.i("PREM","SAVE FILE2: "+header);
                    String filename = "downaloded_image.jpg";//header.replace("attachment; filename=", "");
                    Log.i("PREM","FILENAME: "+filename);
                    File destinationFile = new File(Swrve.getContext().getFilesDir(), filename);

                    BufferedSink bufferedSink = Okio.buffer(Okio.sink(destinationFile));
                    bufferedSink.writeAll(response.body().source());
                    bufferedSink.close();

                    emitter.onNext(destinationFile);
                    emitter.onComplete();
                } catch (IOException e) {
                    Log.i("PREM","ERROR: "+e.getMessage());
                    e.printStackTrace();
                    emitter.onError(e);
                }
            }
        });
    }

}
