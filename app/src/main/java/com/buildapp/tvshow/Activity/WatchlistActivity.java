package com.buildapp.tvshow.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;


import com.buildapp.tvshow.Adapters.WatchlistAdapter;
import com.buildapp.tvshow.R;
import com.buildapp.tvshow.Utilities.TempDataHolder;
import com.buildapp.tvshow.ViewModels.WatchlistViewModel;

import com.buildapp.tvshow.databinding.ActivityWatchlistBinding;
import com.buildapp.tvshow.listener.WatchlistListener;
import com.buildapp.tvshow.models.Tvshow;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class WatchlistActivity extends AppCompatActivity implements WatchlistListener {

    private ActivityWatchlistBinding activityWatchlistBinding;
    private WatchlistViewModel viewModel;
    private WatchlistAdapter watchlistAdapter;
    private List<Tvshow> watchlist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityWatchlistBinding = DataBindingUtil.setContentView(this, R.layout.activity_watchlist);
        doInitialization();

    }

    private void doInitialization() {
        viewModel = new ViewModelProvider(this).get(WatchlistViewModel.class);
        activityWatchlistBinding.imageback.setOnClickListener(view -> onBackPressed());
        watchlist = new ArrayList<>();
        loadWatchlist();

    }

    private void loadWatchlist() {
        activityWatchlistBinding.setIsLoading(true);
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(viewModel.loadWatchlist().subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tvshows -> {
                    activityWatchlistBinding.setIsLoading(false);
                    if (watchlist.size() > 0) {
                        watchlist.clear();
                    }
                    watchlist.addAll(tvshows);
                    watchlistAdapter = new WatchlistAdapter(watchlist, this);
                    activityWatchlistBinding.watchlistRecycleview.setAdapter(watchlistAdapter);
                    activityWatchlistBinding.watchlistRecycleview.setVisibility(View.VISIBLE);
                    compositeDisposable.dispose();
                }));


    }

    @Override
    protected void onResume() {
        super.onResume();
        if(TempDataHolder.IS_WATCHLIST_UPDATED){
            loadWatchlist();
            TempDataHolder.IS_WATCHLIST_UPDATED =false;
        }

    }

    @Override
    public void onTVShowClicked(Tvshow tvshow) {
        Intent intent = new Intent(getApplicationContext(),TVShowDetailsActivity.class);
        intent.putExtra("tvShow" ,tvshow);
        startActivity(intent);

    }

    @Override
    public void remouvTVShowFromWatchlist(Tvshow tvshow, int position) {
        CompositeDisposable compositeDisposableForDelete = new CompositeDisposable();
        compositeDisposableForDelete.add(viewModel.removeTVShowFromWatchlist(tvshow)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    watchlist.remove(position);
                    watchlistAdapter.notifyItemRemoved(position);
                    watchlistAdapter.notifyItemRangeChanged(position, watchlistAdapter.getItemCount());
                    compositeDisposableForDelete.dispose();
                }));

    }
}