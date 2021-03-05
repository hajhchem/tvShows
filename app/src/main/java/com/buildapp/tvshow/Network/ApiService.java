package com.buildapp.tvshow.Network;

import com.buildapp.tvshow.Response.TVShowDetailsResponse;
import com.buildapp.tvshow.Response.TvShowResponse;
import com.buildapp.tvshow.models.Tvshow;
import com.google.gson.annotations.SerializedName;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("most-popular")
    Call<TvShowResponse> getMostPopularTVShows(@Query("page") int page);

    @GET("show-details")
    Call<TVShowDetailsResponse> getTVShowDetails(@Query("q") String tvShowId);

    @GET("search")
    Call<TvShowResponse> searchTvShow(@Query("q") String query, @Query("page") int page);

}
