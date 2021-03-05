package com.buildapp.tvshow.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;


import com.buildapp.tvshow.Adapters.TVShowAdapter;
import com.buildapp.tvshow.listener.TVShowListener;
import com.buildapp.tvshow.models.Tvshow;
import com.buildapp.tvshow.R;
import com.buildapp.tvshow.ViewModels.MostPopulaireTVShowViewModel;
import com.buildapp.tvshow.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

import static com.buildapp.tvshow.BR.tvshow;

public class MainActivity extends AppCompatActivity implements TVShowListener {


    private ActivityMainBinding activityMainBinding;
    private MostPopulaireTVShowViewModel viewModel;
    private  List<Tvshow> tvshows = new ArrayList<>();
    private TVShowAdapter tvShowAdapter;
    private int currentpage = 1;
    private int totalAvailblePage = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        RecyclerView myList = (RecyclerView) findViewById(R.id.tvShowrecyclerView);
        myList.setLayoutManager(layoutManager);
        doInitization();


    }

    private void doInitization(){
        activityMainBinding.tvShowrecyclerView.setHasFixedSize(true);
        viewModel = new ViewModelProvider(this).get(MostPopulaireTVShowViewModel.class);
        tvShowAdapter= new TVShowAdapter(tvshows,this);
        activityMainBinding.tvShowrecyclerView.setAdapter(tvShowAdapter);
        activityMainBinding.tvShowrecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!activityMainBinding.tvShowrecyclerView.canScrollVertically(1)){
                    if (currentpage <= totalAvailblePage){
                        currentpage += 1;
                        getMostPopularTVShows();
                    }
                }
            }
        });

        activityMainBinding.imagewatchlist.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(),WatchlistActivity.class)));
        activityMainBinding.imageSearch.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(),SearchActivity.class)));
        getMostPopularTVShows();
    }

    private void getMostPopularTVShows() {
        toggleLoading();
        viewModel.getMostPopularTVShows(currentpage).observe(this, mostPopularTVShowsResponse -> {
            toggleLoading();
            if(mostPopularTVShowsResponse != null){
                totalAvailblePage = mostPopularTVShowsResponse.getTotalpages();
                if(mostPopularTVShowsResponse.getTvshows() != null){
                    int oldcount = tvshows.size();
                    tvshows.addAll(mostPopularTVShowsResponse.getTvshows());
                    tvShowAdapter.notifyItemRangeInserted(oldcount,tvshows.size());
                }
            }


        });
    }
    private void toggleLoading(){
        if (currentpage==1){
         if (activityMainBinding.getIsLoading() != null && activityMainBinding.getIsLoading()){
             activityMainBinding.setIsLoading(false);
         }else {
             activityMainBinding.setIsLoading(true);
         }
    }else {
            if (activityMainBinding.getIsLoadingMore() != null && activityMainBinding.getIsLoadingMore()){
                activityMainBinding.setIsLoadingMore(false);
            }else {
                activityMainBinding.setIsLoadingMore(true);
            }
        }

    }

    @Override
    public void OnTVShowClicked(Tvshow tvshow) {
        Intent intent = new Intent(getApplicationContext(),TVShowDetailsActivity.class);
       intent.putExtra("tvShow" ,tvshow);
        startActivity(intent);
    }
}