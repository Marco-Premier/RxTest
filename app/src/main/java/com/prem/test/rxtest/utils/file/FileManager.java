package com.prem.test.rxtest.utils.file;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

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

public class FileManager {

    private Context context;

    @Inject
    public FileManager(Context context){
        this.context = context;
    }

    public Function<DownloadImageAction, Observable<Bitmap>> processResponse() {
        return new Function<DownloadImageAction, Observable<Bitmap>>() {
            @Override
            public Observable<Bitmap> apply(DownloadImageAction responseBodyResponse) throws Exception {
                return saveToDiskRx(responseBodyResponse);
            }
        };
    }

    public Observable<Bitmap> saveToDiskRx(final DownloadImageAction response) {
        return Observable.create(new ObservableOnSubscribe<Bitmap>() {
            @Override
            public void subscribe(ObservableEmitter<Bitmap> emitter) throws Exception {

                try {


                    URL url = new URL("http://freebigpictures.com/wp-content/uploads/2009/09/summer-sky.jpg");
                    InputStream inputStream = url.openStream();
                    final BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    final Bitmap bitmap1 = BitmapFactory.decodeStream(inputStream, null, options);
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


                /*InputStream inputStream = response.body().byteStream();
                Headers h = response.headers();
                Integer contentLength = Integer.parseInt(h.get("Content-Length"));
                boolean ma = inputStream.markSupported();

                File f = new File("http://freebigpictures.com/wp-content/uploads/2009/09/summer-sky.jpg");
                long a = f.length();

                //InputStream is1 = new ByteArrayInputStream(response.body().bytes());

                final BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                final Bitmap bitmap1 = BitmapFactory.decodeStream(inputStream, null, options);
                // Calculate inSampleSize
                int reqHeight = (options.outHeight > options.outWidth) ?  getDisplaySize().y : getDisplaySize().x;
                int reqWidth= (options.outHeight > options.outWidth) ?  getDisplaySize().x : getDisplaySize().y;
                options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
                // Decode bitmap with inSampleSize set
                options.inJustDecodeBounds = false;
                //final Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options);
                final Bitmap bitmap = BitmapFactory.decodeByteArray(response.body().bytes(), 0,contentLength, options);

                String fileName = "downaloded_image.jpg";
                File destinationFile = new File(context.getFilesDir(), fileName);
                try {
                    FileOutputStream fos = new FileOutputStream(destinationFile);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    fos.close();
                    emitter.onNext(destinationFile);
                    emitter.onComplete();
                } catch (FileNotFoundException e) {
                    emitter.onError(e);
                } catch (IOException e) {
                    emitter.onError(e);
                }*/
            }
        });
    }

    private Point getDisplaySize(){
        Display display = ((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
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
            while ((halfHeight / inSampleSize) > reqHeight
                    || (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        List<Integer> mergedList = new ArrayList<Integer>();
        return inSampleSize;
    }

}
