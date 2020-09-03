package com.example.nasaglancer;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class OuterObject {

    @SerializedName("items")
    @Expose
    List<CoverClass> coverObject;

    public OuterObject(List<CoverClass> coverObject) {
        this.coverObject = coverObject;
    }

    public List<CoverClass> getCoverObject() {
        return coverObject;
    }

    public void setCoverObject(List<CoverClass> coverObject) {
        this.coverObject = coverObject;
    }
}
