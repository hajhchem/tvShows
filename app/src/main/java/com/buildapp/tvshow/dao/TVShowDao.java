package com.buildapp.tvshow.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.buildapp.tvshow.models.Tvshow;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
@Dao
public interface TVShowDao {

    @Query("SELECT * FROM tvShows")
    Flowable<List<Tvshow>> getWatchlist();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable addToWatchlist(Tvshow tvshow);

    @Delete
    Completable removeFromWatchlist(Tvshow tvshow);

    @Query("SELECT * FROM tvShows WHERE id = :tvShowId")
    Flowable<Tvshow> getTVShowfromWatchlist(String tvShowId);
}