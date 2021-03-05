package com.buildapp.tvshow.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.buildapp.tvshow.listener.TVShowListener;
import com.buildapp.tvshow.models.Tvshow;
import com.buildapp.tvshow.R;
import com.buildapp.tvshow.databinding.ItemTvShowBinding;

import java.util.List;

public class TVShowAdapter extends RecyclerView.Adapter<TVShowAdapter.TVShowViewHolder> {


    private List<Tvshow> tvshows;
    private LayoutInflater layoutInflater;
    private TVShowListener tvShowListener;

    public TVShowAdapter(List<Tvshow> tvshows , TVShowListener tvShowListener) {
        this.tvshows = tvshows;
        this.tvShowListener = tvShowListener;

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
            itemTvShowBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tvShowListener.OnTVShowClicked(tvshow);
                }
            });
        }
    }
}
