package com.prem.test.rxtest.model.result;

import android.graphics.Bitmap;

import static com.prem.test.rxtest.model.result.DownloadImageResult.DOWNLOAD_STATUS.FAILURE;
import static com.prem.test.rxtest.model.result.DownloadImageResult.DOWNLOAD_STATUS.IDLE;
import static com.prem.test.rxtest.model.result.DownloadImageResult.DOWNLOAD_STATUS.IN_FLIGHT;
import static com.prem.test.rxtest.model.result.DownloadImageResult.DOWNLOAD_STATUS.SUCCESS;

/**
 * Created by prem on 16/02/2018.
 */

public class DownloadImageResult extends BaseResult {

    public enum DOWNLOAD_STATUS {IDLE, IN_FLIGHT, SUCCESS, FAILURE};

    private DOWNLOAD_STATUS downloadStatus;
    private Bitmap bitmap;
    private boolean resetUrlText;
    private Long idUrl;
    private String url;

    private DownloadImageResult(DOWNLOAD_STATUS downloadStatus){
        this.downloadStatus = downloadStatus;
    }

    public static DownloadImageResult inFlight(String url, Long idUrl){
        DownloadImageResult downloadImageResult = new DownloadImageResult(IN_FLIGHT);
        downloadImageResult.idUrl = idUrl;
        downloadImageResult.url = url;
        return downloadImageResult;
    }

    public static DownloadImageResult idle(boolean resetUrlText){
        DownloadImageResult downloadImageResult = new DownloadImageResult(IDLE);
        downloadImageResult.resetUrlText = resetUrlText;
        if(resetUrlText){
            downloadImageResult.bitmap = null;
            downloadImageResult.idUrl = null;
            downloadImageResult.url = null;
        }
        return downloadImageResult;
    }

    public static DownloadImageResult success(Bitmap bitmap){
        DownloadImageResult downloadImageResult = new DownloadImageResult(SUCCESS);
        downloadImageResult.bitmap = bitmap;
        return downloadImageResult;
    }

    public static DownloadImageResult failure(){
        return new DownloadImageResult(FAILURE);
    }

    public DOWNLOAD_STATUS getDownloadStatus(){
        return downloadStatus;
    }

    public Bitmap getBitmap(){
        return bitmap;
    }

    public Long getIdUrl(){
        return idUrl;
    }

    public String getUrl(){
        return url;
    }

    public boolean isResetUrlText() {
        return resetUrlText;
    }
}