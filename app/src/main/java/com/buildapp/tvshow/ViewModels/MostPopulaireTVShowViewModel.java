package com.buildapp.tvshow.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.buildapp.tvshow.Repisotories.MostPopularTVShowRepisotory;
import com.buildapp.tvshow.Response.TvShowResponse;

public class MostPopulaireTVShowViewModel extends ViewModel {

    private MostPopularTVShowRepisotory mostPopularTVShowRepisotory;

    public MostPopulaireTVShowViewModel (){
        mostPopularTVShowRepisotory = new MostPopularTVShowRepisotory();
    }
    public LiveData<TvShowResponse> getMostPopularTVShows (int page){

        return mostPopularTVShowRepisotory.getMostPopularTVShows(page);
    }
}
