package com.prem.test.swrve.model.persistent.dto;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by prem on 18/02/2018.
 */

public class UrlDto extends RealmObject {

    @PrimaryKey
    private long idUrl;
    private String url;
    private Date downloadedAt;

    public long getIdUrl() {
        return idUrl;
    }

    public void setIdUrl(long idUrl) {
        this.idUrl = idUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getDownloadedAt() {
        return downloadedAt;
    }

    public void setDownloadedAt(Date downloadedAt) {
        this.downloadedAt = downloadedAt;
    }
}
