package com.buildapp.tvshow.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.buildapp.tvshow.R;
import com.buildapp.tvshow.databinding.ItemTvShowBinding;

import com.buildapp.tvshow.listener.WatchlistListener;
import com.buildapp.tvshow.models.Tvshow;

import java.util.List;

public class WatchlistAdapter extends RecyclerView.Adapter<WatchlistAdapter.TVShowViewHolder> {


    private List<Tvshow> tvshows;
    private LayoutInflater layoutInflater;
    private WatchlistListener watchlistListener;

    public WatchlistAdapter(List<Tvshow> tvshows , WatchlistListener watchlistListener) {
        this.tvshows = tvshows;
        this.watchlistListener = watchlistListener;

    }

    @NonNull
    @Override
    public TVShowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(layoutInflater == null){

            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        ItemTvShowBinding tvShowBinding = DataBindingUtil.inflate(
                layoutInflater, R.layout.item_tv_show,parent,false
        );

        return new TVShowViewHolder(tvShowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull TVShowViewHolder holder, int position) {
        holder.bindingTVShow(tvshows.get(position));

    }

    @Override
    public int getItemCount() {
        return tvshows.size();
    }

    class TVShowViewHolder extends RecyclerView.ViewHolder{

        private ItemTvShowBinding itemTvShowBinding;

        public TVShowViewHolder(ItemTvShowBinding itemTvShowBinding) {
            super(itemTvShowBinding.getRoot());
            this.itemTvShowBinding = itemTvShowBinding;
        }

        public void bindingTVShow(Tvshow tvshow){
            itemTvShowBinding.setTvshow(tvshow);
            itemTvShowBinding.executePendingBindings();
            itemTvShowBinding.getRoot().setOnClickListener(view -> watchlistListener.onTVShowClicked(tvshow));
            itemTvShowBinding.imageDelete.setOnClickListener(view -> watchlistListener.remouvTVShowFromWatchlist(tvshow,getAdapterPosition()));
            itemTvShowBinding.imageDelete.setVisibility(View.VISIBLE);
        }
    }
}
