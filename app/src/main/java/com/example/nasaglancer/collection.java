package com.example.nasaglancer;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class collection {

    @SerializedName("collection")
    @Expose
    OuterObject major;

    public collection(OuterObject major) {
        this.major = major;
    }

    public OuterObject getMajor() {
        return major;
    }

    public void setMajor(OuterObject major) {
        this.major = major;
    }
}
