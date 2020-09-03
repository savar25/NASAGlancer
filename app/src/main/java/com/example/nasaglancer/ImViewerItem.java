package com.example.nasaglancer;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ImViewerItem {

    @SerializedName("date")
    @Expose
    String Date;

    @SerializedName("url")
    @Expose
    String url;

    @SerializedName("media_type")
    @Expose
    String type;


    public ImViewerItem(String date, String url, String type) {
        Date = date;
        this.url = url;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
