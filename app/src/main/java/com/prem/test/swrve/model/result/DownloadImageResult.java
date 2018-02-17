package com.prem.test.swrve.model.result;

import static com.prem.test.swrve.model.result.DownloadImageResult.DOWNLOAD_STATUS.FAILURE;
import static com.prem.test.swrve.model.result.DownloadImageResult.DOWNLOAD_STATUS.IDLE;
import static com.prem.test.swrve.model.result.DownloadImageResult.DOWNLOAD_STATUS.IN_FLIGHT;
import static com.prem.test.swrve.model.result.DownloadImageResult.DOWNLOAD_STATUS.SUCCESS;

/**
 * Created by prem on 16/02/2018.
 */

public class DownloadImageResult extends BaseResult {

    public enum DOWNLOAD_STATUS {IDLE, IN_FLIGHT, SUCCESS, FAILURE};

    private DOWNLOAD_STATUS downloadStatus;
    private String imagePaht;

    private DownloadImageResult(DOWNLOAD_STATUS downloadStatus){
        this.downloadStatus = downloadStatus;
    }

    public static DownloadImageResult defaultState(){
        return new DownloadImageResult(IDLE);
    }

    public static DownloadImageResult inFlight(){
        return new DownloadImageResult(IN_FLIGHT);
    }

    public static DownloadImageResult success(String path){
        DownloadImageResult downloadImageResult = new DownloadImageResult(SUCCESS);
        downloadImageResult.imagePaht = path;
        return downloadImageResult;
    }

    public static DownloadImageResult failure(){
        return new DownloadImageResult(FAILURE);
    }

    public DOWNLOAD_STATUS getDownloadStatus(){
        return downloadStatus;
    }

    public String getImagePaht(){
        return imagePaht;
    }

}
