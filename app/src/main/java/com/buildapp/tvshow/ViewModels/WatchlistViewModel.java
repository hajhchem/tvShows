package com.buildapp.tvshow.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.buildapp.tvshow.database.TVShowsDatabase;
import com.buildapp.tvshow.models.Tvshow;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

public class WatchlistViewModel extends AndroidViewModel {

    private TVShowsDatabase tvShowsDatabase;

    public WatchlistViewModel (@NonNull Application application){
        super(application);
        tvShowsDatabase = TVShowsDatabase.getTvShowsDatabase(application);

    }

    public Flowable<List<Tvshow>> loadWatchlist(){
        return tvShowsDatabase.tvShowDao().getWatchlist();
    }

    public Completable removeTVShowFromWatchlist(Tvshow tvshow){
        return tvShowsDatabase.tvShowDao().removeFromWatchlist(tvshow);
    }
}
