package com.buildapp.tvshow.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;


import com.buildapp.tvshow.Repisotories.TVShowDetailsRepisotory;
import com.buildapp.tvshow.Response.TVShowDetailsResponse;
import com.buildapp.tvshow.database.TVShowsDatabase;
import com.buildapp.tvshow.models.Tvshow;

import io.reactivex.Completable;
import io.reactivex.Flowable;

public class TVShowDetailsViewModel extends AndroidViewModel {

    private TVShowDetailsRepisotory tvShowDetailsRepisotory;
    private TVShowsDatabase tvShowsDatabase;

    public TVShowDetailsViewModel(@NonNull Application application){
        super(application);
        tvShowDetailsRepisotory = new TVShowDetailsRepisotory();
        tvShowsDatabase = TVShowsDatabase.getTvShowsDatabase(application);
    }

    public LiveData<TVShowDetailsResponse> getTVShowDetails (String tvShowId){
        return tvShowDetailsRepisotory.getTVShowDetails(tvShowId);
    }

    public Completable addToWatchlist(Tvshow tvshow){
        return tvShowsDatabase.tvShowDao().addToWatchlist(tvshow);
    }

    public Flowable<Tvshow> getTVShowFromWatchlist(String tvShowId){
        return tvShowsDatabase.tvShowDao().getTVShowfromWatchlist(tvShowId);
    }

    public Completable remouveTVShowFromWatchlist(Tvshow tvshow){
        return tvShowsDatabase.tvShowDao().removeFromWatchlist(tvshow);
    }
}
