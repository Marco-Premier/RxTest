package com.prem.test.rxtest.utils.file;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

import com.prem.test.rxtest.RxTest;
import com.prem.test.rxtest.model.action.DownloadImageAction;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Function;

/**
 * Created by prem on 16/02/2018.
 */

public class ImageDownloader {

    @Inject
    public ImageDownloader(){
        int x = 2;
    }

    /**
     * This method process the stream of data coming from the internet twice. The first time it just gets the image size
     * and then it download it scaling down the image considering the device screen's size in order to
     * optimize memory consumption.
     * @param action
     * @return
     */
    public Observable<Bitmap> downloadImage(final DownloadImageAction action) {
        return Observable.create(new ObservableOnSubscribe<Bitmap>() {
            @Override
            public void subscribe(ObservableEmitter<Bitmap> emitter) throws Exception {

                try {


                    URL url = new URL(action.getUrl());
                    InputStream inputStream = url.openStream();
                    final BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    final Bitmap sampledBitmap = BitmapFactory.decodeStream(inputStream, null, options);
                    // Calculate inSampleSize
                    int reqHeight = (options.outHeight > options.outWidth) ? getDisplaySize().y : getDisplaySize().x;
                    int reqWidth = (options.outHeight > options.outWidth) ? getDisplaySize().x : getDisplaySize().y;
                    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
                    // Decode bitmap with inSampleSize set
                    options.inJustDecodeBounds = false;
                    inputStream.close();
                    inputStream = url.openStream();
                    final Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options);

                    emitter.onNext(bitmap);
                    emitter.onComplete();

                }catch (Exception e){
                    emitter.onError(e);
                }

            }
        });
    }

    private Point getDisplaySize(){
        Display display = ((WindowManager) RxTest.getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    private int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight || (halfWidth / inSampleSize) > reqHeight) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

}