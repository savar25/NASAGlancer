package com.example.nasaglancer;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ImageCollection {


    @SerializedName("collection")
    @Expose
    ImageOuterCover imageOuterCover;

    public ImageCollection(ImageOuterCover imageOuterCover) {
        this.imageOuterCover = imageOuterCover;
    }

    public ImageOuterCover getImageOuterCover() {
        return imageOuterCover;
    }

    public void setImageOuterCover(ImageOuterCover imageOuterCover) {
        this.imageOuterCover = imageOuterCover;
    }
}
