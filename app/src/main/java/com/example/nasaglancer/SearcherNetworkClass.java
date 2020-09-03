package com.example.nasaglancer;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SearcherNetworkClass {

    @GET("search")
    Call<collection> getSearchResult(@Query("q") CharSequence query);
}
