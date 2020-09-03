package com.example.nasaglancer;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ImageResource {

    @SerializedName("href")
    @Expose
    String jpegUrl;

    public ImageResource(String jpegUrl) {
        this.jpegUrl = jpegUrl;
    }

    public String getJpegUrl() {
        return jpegUrl;
    }

    public void setJpegUrl(String jpegUrl) {
        this.jpegUrl = jpegUrl;
    }
}
