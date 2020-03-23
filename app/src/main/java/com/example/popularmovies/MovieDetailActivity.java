package com.example.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.example.popularmovies.data.database.Movie;
import com.example.popularmovies.data.Trailer;
import com.example.popularmovies.databinding.ActivityMovieDetailBinding;
import com.example.popularmovies.utils.InjectorUtils;
import com.example.popularmovies.utils.NetworkUtils;
import com.example.popularmovies.viewmodel.MovieDetailActivityViewModel;
import com.example.popularmovies.viewmodel.MovieDetailViewModelFactory;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class MovieDetailActivity extends AppCompatActivity implements TrailerAdapter.TrailerAdapterOnClickHandler{

    private static final String LOG_TAG = MovieDetailActivity.class.getSimpleName();
    public static final String EXTRA_MOVIE = "com.example.popularmovies.Movie";

    private TrailerAdapter mTrailerAdapter;
    private ReviewAdapter mReviewAdapter;

    private MovieDetailActivityViewModel mViewModel;

    private Movie mMovie;

    private ActivityMovieDetailBinding mDetailBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        // Remove back arrow from action bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);

        mDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_movie_detail);

        mDetailBinding.rvTrailers.setHasFixedSize(true);
        mTrailerAdapter = new TrailerAdapter(this);
        mDetailBinding.rvTrailers.setAdapter(mTrailerAdapter);

        mDetailBinding.rvReviews.setHasFixedSize(true);
        mReviewAdapter = new ReviewAdapter();
        mDetailBinding.rvReviews.setAdapter(mReviewAdapter);


        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra(EXTRA_MOVIE)) {
                mMovie = (Movie) intentThatStartedThisActivity.getSerializableExtra(EXTRA_MOVIE);
                if (mMovie != null) {
                    MovieDetailViewModelFactory factory = InjectorUtils.provideMovieDetailViewModelFactory(this.getApplicationContext(), mMovie.getId());
                    mViewModel = new ViewModelProvider(this,factory).get(MovieDetailActivityViewModel.class);

                    bindToUI();

                    loadTrailerData();

                    loadReviewData();
                }
            }
        }
    }

    private void bindToUI() {
        Log.d(LOG_TAG, "bindToUI");
        mDetailBinding.tvDetailTitle.setText(mMovie.getTitle());
        mDetailBinding.tvDetailReleaseDate.setText(mMovie.getReleaseDate());
        mDetailBinding.tvDetailVoteAverage.setText(String.format(
                getApplicationContext().getString(R.string.vote_average_format),
                mMovie.getVoteAverage()
        ));
        mDetailBinding.tvDetailOverview.setText(mMovie.getOverview());
        mDetailBinding.ivDetailMoviePoster.setContentDescription(mMovie.getTitle());
        mDetailBinding.ivFavorite.setOnClickListener(view -> {
            Log.d(LOG_TAG, "Movie favorite clicked: " + mMovie.getId());
            mViewModel.setFavorite(mMovie);
        });

        Picasso.get()
                .load(mMovie.getPoster())
                .into(mDetailBinding.ivDetailMoviePoster);

        mViewModel.isFavorite().observe(this, isFavorite -> {
            Log.d(LOG_TAG, "Movie isFavorite: " + isFavorite);
            if (isFavorite) {
                mDetailBinding.ivFavorite.setImageResource(R.drawable.ic_star_black_48dp);
            } else {
                mDetailBinding.ivFavorite.setImageResource(R.drawable.ic_star_border_black_48dp);
            }
            mDetailBinding.ivFavorite.setVisibility(View.VISIBLE);
        });
    }

    @Override
    public void onClick(Trailer trailer) {
        Uri youtubeUri = NetworkUtils.buildWatchUrl(trailer.getKey());

        Intent intentToWatchTrailer = new Intent(Intent.ACTION_VIEW, youtubeUri);
        startActivity(intentToWatchTrailer);
    }

    private void loadTrailerData() {
        mViewModel.getTrailers().observe(this, trailers -> {
            mTrailerAdapter.setTrailerData(trailers);
            if (trailers != null && trailers.size() != 0) showTrailerDataView();
            else mDetailBinding.pbLoadingIndicator.setVisibility(View.VISIBLE);
        });
    }

    private void showTrailerDataView() {
        mDetailBinding.rvTrailers.setVisibility(View.VISIBLE);
    }

    private void loadReviewData() {
        mViewModel.getReviews().observe(this, reviews -> {
            mReviewAdapter.setReviewData(reviews);
            if (reviews != null && reviews.size() != 0) showReviewDataView();
            else mDetailBinding.pbLoadingIndicatorReview.setVisibility(View.VISIBLE);
        });
    }

    private void showReviewDataView() {
        mDetailBinding.rvReviews.setVisibility(View.VISIBLE);
    }
}