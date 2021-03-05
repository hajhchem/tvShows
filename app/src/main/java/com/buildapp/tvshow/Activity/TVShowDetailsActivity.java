package com.buildapp.tvshow.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.buildapp.tvshow.Adapters.EpisodesAdapter;
import com.buildapp.tvshow.Adapters.ImageSliderAdapter;
import com.buildapp.tvshow.R;
import com.buildapp.tvshow.Utilities.TempDataHolder;
import com.buildapp.tvshow.ViewModels.TVShowDetailsViewModel;
import com.buildapp.tvshow.databinding.ActivityTVShowDetailsBinding;
import com.buildapp.tvshow.databinding.LayoutEpisodesBottomSheetBinding;
import com.buildapp.tvshow.models.Tvshow;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Locale;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class TVShowDetailsActivity extends AppCompatActivity {

    private ActivityTVShowDetailsBinding activityTVShowDetailsBinding;
    private TVShowDetailsViewModel tvShowDetailsViewModel;
    private BottomSheetDialog eppisodesBottomSheetDialog;
    private LayoutEpisodesBottomSheetBinding layoutEpisodesBottomSheetBinding;
    private Tvshow tvshow;
    private Boolean isTVShowAvilbleInWatch = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityTVShowDetailsBinding = DataBindingUtil.setContentView(this, R.layout.activity_t_v_show_details);
        doInitilization();
    }

    private void doInitilization() {
        tvShowDetailsViewModel = new ViewModelProvider(this).get(TVShowDetailsViewModel.class);
        activityTVShowDetailsBinding.imageback.setOnClickListener(view -> onBackPressed());
        tvshow = (Tvshow) getIntent().getSerializableExtra("tvShow");
        checkTVShowInWatchlist();
        getTVSowDetails();
    }

    private void checkTVShowInWatchlist (){
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(tvShowDetailsViewModel.getTVShowFromWatchlist(String.valueOf(tvshow.getId()))
        .subscribeOn(Schedulers.computation())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(tvshow ->{
                    isTVShowAvilbleInWatch = true;
                    activityTVShowDetailsBinding.imagewatchlist.setImageResource(R.drawable.ic_check_24);
                    compositeDisposable.dispose();
                }

                )) ;  }

    private void getTVSowDetails() {

        activityTVShowDetailsBinding.setIsLoading(true);
        String tvshowId = String.valueOf(tvshow.getId());
        tvShowDetailsViewModel.getTVShowDetails(tvshowId).observe(
                this, tvShowDetailsResponse -> {
                    activityTVShowDetailsBinding.setIsLoading(false);
                    if (tvShowDetailsResponse.getTvShowsDetals() != null) {
                        if (tvShowDetailsResponse.getTvShowsDetals().getPictures() != null) {
                            loadImageSlider(tvShowDetailsResponse.getTvShowsDetals().getPictures());
                        }
                        activityTVShowDetailsBinding.setTvShowImagrURL(
                                tvShowDetailsResponse.getTvShowsDetals().getImage_path()
                        );
                        activityTVShowDetailsBinding.imageTvshow.setVisibility(View.VISIBLE);
                        activityTVShowDetailsBinding.setDescription(
                                String.valueOf(HtmlCompat.fromHtml(tvShowDetailsResponse.getTvShowsDetals().getDescription(),
                                        HtmlCompat.FROM_HTML_MODE_LEGACY))
                        );
                        activityTVShowDetailsBinding.textdescription.setVisibility(View.VISIBLE);
                        activityTVShowDetailsBinding.textReadMore.setVisibility(View.VISIBLE);
                        activityTVShowDetailsBinding.textReadMore.setOnClickListener(view -> {
                            if (activityTVShowDetailsBinding.textReadMore.getText().toString().equals("Read More")) {
                                activityTVShowDetailsBinding.textdescription.setMaxLines(Integer.MAX_VALUE);
                                activityTVShowDetailsBinding.textdescription.setEllipsize(null);
                                activityTVShowDetailsBinding.textReadMore.setText("Read Less");
                            } else {
                                activityTVShowDetailsBinding.textdescription.setMaxLines(4);
                                activityTVShowDetailsBinding.textdescription.setEllipsize(TextUtils.TruncateAt.END);
                                activityTVShowDetailsBinding.textReadMore.setText("Read More");
                            }
                        });


                        activityTVShowDetailsBinding.setRating(
                                String.format(Locale.getDefault(),
                                        "%.2f",
                                        Double.parseDouble(tvShowDetailsResponse.getTvShowsDetals().getRating()))
                        );

                        if (tvShowDetailsResponse.getTvShowsDetals().getGenres() != null) {
                            activityTVShowDetailsBinding.setGenre(tvShowDetailsResponse.getTvShowsDetals().getGenres()[0]);

                        } else {
                            activityTVShowDetailsBinding.setGenre("N/A");
                        }
                        activityTVShowDetailsBinding.setRuntime(tvShowDetailsResponse.getTvShowsDetals().getRuntime() + "Min");
                        activityTVShowDetailsBinding.viewDiver1.setVisibility(View.VISIBLE);
                        activityTVShowDetailsBinding.layoutMisc.setVisibility(View.VISIBLE);
                        activityTVShowDetailsBinding.viewdiver2.setVisibility(View.VISIBLE);
                        activityTVShowDetailsBinding.buttonWebsite.setOnClickListener(view -> {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(tvShowDetailsResponse.getTvShowsDetals().getUrl()));
                            startActivity(intent);
                        });
                        activityTVShowDetailsBinding.buttonWebsite.setVisibility(View.VISIBLE);
                        activityTVShowDetailsBinding.buttonEpisode.setVisibility(View.VISIBLE);
                        activityTVShowDetailsBinding.buttonEpisode.setOnClickListener(view -> {
                            if (eppisodesBottomSheetDialog == null) {
                                eppisodesBottomSheetDialog = new BottomSheetDialog(TVShowDetailsActivity.this);
                                layoutEpisodesBottomSheetBinding = DataBindingUtil.inflate(
                                        LayoutInflater.from(TVShowDetailsActivity.this),
                                        R.layout.layout_episodes_bottom_sheet,
                                        findViewById(R.id.episodesContnanier), false
                                );
                                eppisodesBottomSheetDialog.setContentView(layoutEpisodesBottomSheetBinding.getRoot());
                                layoutEpisodesBottomSheetBinding.episodeRecyclerView.setAdapter(
                                        new EpisodesAdapter(tvShowDetailsResponse.getTvShowsDetals().getEpisodes())
                                );
                                layoutEpisodesBottomSheetBinding.textTitle.setText(
                                        String.format("Episodes | %s", tvshow.getName())
                                );
                                layoutEpisodesBottomSheetBinding.imageClose.setOnClickListener(view1 -> eppisodesBottomSheetDialog.dismiss());

                            }
                            // ----- Optional section start ----
                            FrameLayout frameLayout = eppisodesBottomSheetDialog.findViewById(
                                    com.google.android.material.R.id.design_bottom_sheet
                            );

                            if (frameLayout != null) {
                                BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from(frameLayout);
                                bottomSheetBehavior.setPeekHeight(Resources.getSystem().getDisplayMetrics().heightPixels);
                                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                            }

                            // ----- Optinal  section End ------

                            eppisodesBottomSheetDialog.show();
                        });

                        activityTVShowDetailsBinding.imagewatchlist.setOnClickListener(view -> {
                            CompositeDisposable compositeDisposable = new CompositeDisposable();
                            if(isTVShowAvilbleInWatch){
                                compositeDisposable.add(tvShowDetailsViewModel.remouveTVShowFromWatchlist(tvshow)
                                .subscribeOn(Schedulers.computation())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(() ->{
                                    isTVShowAvilbleInWatch = false;
                                    TempDataHolder.IS_WATCHLIST_UPDATED= true;
                                    activityTVShowDetailsBinding.imagewatchlist.setImageResource(R.drawable.ic_eye_24);
                                    Toast.makeText(getApplicationContext(), "Remouved from watchlist", Toast.LENGTH_SHORT).show();

                                }));

                            }else
                            {
                               compositeDisposable.add(tvShowDetailsViewModel.addToWatchlist(tvshow)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(() -> {
                                            TempDataHolder.IS_WATCHLIST_UPDATED= true;
                                            activityTVShowDetailsBinding.imagewatchlist.setImageResource(R.drawable.ic_check_24);
                                            Toast.makeText(getApplicationContext(), "Added to watchlist", Toast.LENGTH_SHORT).show();
                                            compositeDisposable.dispose();
                                        }));
                            }
                        });
                        activityTVShowDetailsBinding.imagewatchlist.setVisibility(View.VISIBLE);
                        loadBasicTVShowDetails();
                    }
                }
        );
    }

    private void loadImageSlider(String[] sliderimage) {
        activityTVShowDetailsBinding.sliderViewPager.setOffscreenPageLimit(1);
        activityTVShowDetailsBinding.sliderViewPager.setAdapter(new ImageSliderAdapter(sliderimage));
        activityTVShowDetailsBinding.sliderViewPager.setVisibility(View.VISIBLE);
        activityTVShowDetailsBinding.viewfadeedge.setVisibility(View.VISIBLE);
        setupSliderIndicator(sliderimage.length);
        activityTVShowDetailsBinding.sliderViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                setCurrentSliderIndicator(position);
            }
        });
    }

    private void setupSliderIndicator(int count) {
        ImageView[] indicators = new ImageView[count];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(8, 0, 8, 0);
        for (int i = 0; i < indicators.length; i++) {
            indicators[i] = new ImageView(getApplicationContext());
            indicators[i].setImageDrawable(ContextCompat.getDrawable(
                    getApplicationContext(), R.drawable.background_slider_indicator_inactive
            ));
            indicators[i].setLayoutParams(layoutParams);
            activityTVShowDetailsBinding.layoutsliderIndicateur.addView(indicators[i]);
            activityTVShowDetailsBinding.layoutsliderIndicateur.setVisibility(View.VISIBLE);
        }
        setCurrentSliderIndicator(0);
    }

    private void setCurrentSliderIndicator(int position) {
        int chlidcount = activityTVShowDetailsBinding.layoutsliderIndicateur.getChildCount();
        for (int i = 0; i < chlidcount; i++) {
            ImageView imageView = (ImageView) activityTVShowDetailsBinding.layoutsliderIndicateur.getChildAt(i);
            if (i == position) {
                imageView.setImageDrawable(
                        ContextCompat.getDrawable(getApplicationContext(), R.drawable.backgroud_slider_indicator_active)
                );
            } else {
                imageView.setImageDrawable(
                        ContextCompat.getDrawable(getApplicationContext(), R.drawable.background_slider_indicator_inactive)
                );
            }
        }
    }

    private void loadBasicTVShowDetails() {
        activityTVShowDetailsBinding.setTvShowName(tvshow.getName());
        activityTVShowDetailsBinding.setNetworkCountry(tvshow.getNetwork() + "(" +
                tvshow.getCountry() + ")");
        activityTVShowDetailsBinding.setStatus(tvshow.getStatus());
        activityTVShowDetailsBinding.setSatartedDate(tvshow.getStart_date());

        activityTVShowDetailsBinding.textName.setVisibility(View.VISIBLE);
        activityTVShowDetailsBinding.textNetworkCountry.setVisibility(View.VISIBLE);
        activityTVShowDetailsBinding.textStatus.setVisibility(View.VISIBLE);
        activityTVShowDetailsBinding.textStarted.setVisibility(View.VISIBLE);


    }
}