package com.example.nasaglancer;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CoverClass {

    @SerializedName("data")
    @Expose
    ArrayList<innerData> dataList;

    public CoverClass(ArrayList<innerData> dataList) {
        this.dataList = dataList;
    }

    public ArrayList<innerData> getDataList() {
        return dataList;
    }

    public void setDataList(ArrayList<innerData> dataList) {
        this.dataList = dataList;
    }
}
