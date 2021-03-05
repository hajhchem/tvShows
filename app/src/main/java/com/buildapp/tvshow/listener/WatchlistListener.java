package com.buildapp.tvshow.listener;

import com.buildapp.tvshow.models.Tvshow;

public interface WatchlistListener {

    void onTVShowClicked(Tvshow tvshow);

    void remouvTVShowFromWatchlist (Tvshow tvshow, int position);
}
