package com.buildapp.tvshow.Response;

import com.buildapp.tvshow.models.TvShowsDetails;
import com.google.gson.annotations.SerializedName;

public class TVShowDetailsResponse {

    @SerializedName("tvShow")

    private TvShowsDetails tvShowsDetals;

    public TvShowsDetails getTvShowsDetals() {
        return tvShowsDetals;
    }
}
