package com.buildapp.tvshow.Repisotories;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.buildapp.tvshow.Network.ApiClient;
import com.buildapp.tvshow.Network.ApiService;
import com.buildapp.tvshow.Response.TvShowResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchTVShowRepisotory {

    private ApiService apiService;

    public SearchTVShowRepisotory(){

        apiService = ApiClient.getRetrofit().create(ApiService.class);

    }

    public LiveData<TvShowResponse> searchTvShow(String query, int page){
        MutableLiveData<TvShowResponse> data = new MutableLiveData<>();

        apiService.searchTvShow(query,page).enqueue(new Callback<TvShowResponse>() {
            @Override
            public void onResponse(@NonNull Call<TvShowResponse> call,@NonNull Response<TvShowResponse> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<TvShowResponse> call,@NonNull Throwable t) {
                data.setValue(null);
            }
        });

        return data;
    }
}
