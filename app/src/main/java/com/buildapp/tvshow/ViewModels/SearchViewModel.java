package com.buildapp.tvshow.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.buildapp.tvshow.Repisotories.SearchTVShowRepisotory;
import com.buildapp.tvshow.Response.TvShowResponse;

public class SearchViewModel extends ViewModel {

    private SearchTVShowRepisotory searchTVShowRepisotory;

    public SearchViewModel(){
        searchTVShowRepisotory = new SearchTVShowRepisotory();
    }
    public LiveData<TvShowResponse> searchTVShow(String query,int page){
        return searchTVShowRepisotory.searchTvShow(query, page);
    }
}
