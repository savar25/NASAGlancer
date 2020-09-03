package com.example.nasaglancer;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface AssetImageNetworkClass {

    @GET("asset/{id}")
    Call<ImageCollection> getReqImg(@Path("id") String ID);
}
