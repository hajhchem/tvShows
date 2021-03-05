package com.buildapp.tvshow.Response;

import com.buildapp.tvshow.models.Tvshow;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TvShowResponse {

    @SerializedName("page")
    private int page;

    @SerializedName("pages")
    private int totalpages;

    @SerializedName("tv_shows")
    private List<Tvshow> tvshows;

    public int getPage() {
        return page;
    }

    public int getTotalpages() {
        return totalpages;
    }

    public List<Tvshow> getTvshows() {
        return tvshows;
    }
}
