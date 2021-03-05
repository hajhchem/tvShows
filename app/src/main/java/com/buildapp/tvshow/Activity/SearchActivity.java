package com.buildapp.tvshow.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;


import com.buildapp.tvshow.Adapters.TVShowAdapter;
import com.buildapp.tvshow.R;
import com.buildapp.tvshow.ViewModels.SearchViewModel;
import com.buildapp.tvshow.databinding.ActivitySearchBinding;
import com.buildapp.tvshow.listener.TVShowListener;
import com.buildapp.tvshow.models.Tvshow;


import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SearchActivity extends AppCompatActivity implements TVShowListener {

    private ActivitySearchBinding activitySearchBinding;
    private SearchViewModel viewModel;
    private  List<Tvshow> tvShows = new ArrayList<>();
    private TVShowAdapter tvShowAdapter;
    private int currentPage = 1;
    private int totalAvilablePage = 1;
    private Timer timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySearchBinding = DataBindingUtil.setContentView(this,R.layout.activity_search);
        doInitialization();
    }

    private void doInitialization(){
        activitySearchBinding.imageback.setOnClickListener(view -> onBackPressed());
        activitySearchBinding.tvShowrecyclerView.setHasFixedSize(true);
        viewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        tvShowAdapter = new TVShowAdapter(tvShows,this);
        activitySearchBinding.tvShowrecyclerView.setAdapter(tvShowAdapter);

        activitySearchBinding.inputsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (timer != null){
                    timer.cancel();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().trim().isEmpty()){
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            new Handler(Looper.getMainLooper()).post(() -> {
                                currentPage = 1;
                                totalAvilablePage = 1;
                                searchTVShow(editable.toString());
                            });

                        }
                    },800);
                }else{
                    tvShows.clear();
                    tvShowAdapter.notifyDataSetChanged();
                }
            }
        });

        activitySearchBinding.tvShowrecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!activitySearchBinding.tvShowrecyclerView.canScrollVertically(1)){

                    if (!activitySearchBinding.inputsearch.getText().toString().isEmpty()){
                        if (currentPage < totalAvilablePage){
                            currentPage += 1;
                            searchTVShow(activitySearchBinding.inputsearch.getText().toString());
                        }
                    }
                }
            }
        });
        activitySearchBinding.inputsearch.requestFocus();
    }

    private void searchTVShow(String query){

        toggleLoading();
        viewModel.searchTVShow(query,currentPage).observe(this,tvShowResponse -> {
            toggleLoading();
            if (tvShowResponse != null){
                totalAvilablePage = tvShowResponse.getTotalpages();
                if (tvShowResponse.getTvshows() != null){
                    int oldcount = tvShows.size();
                    tvShows.addAll(tvShowResponse.getTvshows());
                    tvShowAdapter.notifyItemRangeInserted(oldcount, tvShows.size());
                }
            }
        });

    }

    private void toggleLoading(){
        if (currentPage==1){
            activitySearchBinding.setIsLoading(activitySearchBinding.getIsLoading() == null || !activitySearchBinding.getIsLoading());
        }else {
            activitySearchBinding.setIsLoadingMore(activitySearchBinding.getIsLoadingMore() == null || !activitySearchBinding.getIsLoadingMore());
        }

    }

    @Override
    public void OnTVShowClicked(Tvshow tvshow) {
        Intent intent = new Intent(getApplicationContext(),TVShowDetailsActivity.class);
        intent.putExtra("tvShow" ,tvshow);
        startActivity(intent);
    }
}