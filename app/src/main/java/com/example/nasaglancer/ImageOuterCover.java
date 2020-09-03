package com.example.nasaglancer;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ImageOuterCover {

    @SerializedName("items")
    @Expose
    ArrayList<ImageResource> imageResources;

    public ImageOuterCover(ArrayList<ImageResource> imageResources) {
        this.imageResources = imageResources;
    }

    public ArrayList<ImageResource> getImageResources() {
        return imageResources;
    }

    public void setImageResources(ArrayList<ImageResource> imageResources) {
        this.imageResources = imageResources;
    }
}
