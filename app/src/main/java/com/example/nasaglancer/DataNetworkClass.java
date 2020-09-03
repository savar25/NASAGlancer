package com.example.nasaglancer;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DataNetworkClass {


    @GET("apod?api_key=QYNe14kvNAaXAAO3x9GS3LGUpxmVpu9O2ULj5I6Y")
    Call<ImViewerItem> getImg(@Query( "date") String date);
}
