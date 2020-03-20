package com.example.popularmovies;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.example.popularmovies.data.database.Movie;
import com.example.popularmovies.data.Trailer;
import com.example.popularmovies.utils.InjectorUtils;
import com.example.popularmovies.utils.MovieJsonUtils;
import com.example.popularmovies.utils.NetworkUtils;
import com.example.popularmovies.viewmodel.MovieDetailActivityViewModel;
import com.example.popularmovies.viewmodel.MovieDetailViewModelFactory;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.Objects;

public class MovieDetailActivity extends AppCompatActivity implements TrailerAdapter.TrailerAdapterOnClickHandler{

    private static final String LOG_TAG = MovieDetailActivity.class.getSimpleName();
    public static final String EXTRA_MOVIE = "com.example.popularmovies.Movie";

    private TrailerAdapter mTrailerAdapter;
    private ReviewAdapter mReviewAdapter;

    private RecyclerView mRecyclerView;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;

    private RecyclerView mRecyclerViewReview;
    private TextView mErrorMessageDisplayReview;
    private ProgressBar mLoadingIndicatorReview;
    private MovieDetailActivityViewModel mViewModel;

    private Movie mMovie;
    private TextView mTitle;
    private TextView mReleaseDate;
    private TextView mVoteAverage;
    private TextView mOverview;
    private ImageView mImagePoster;
    private ImageView mImageFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        mTitle = findViewById(R.id.tv_detail_title);
        mReleaseDate = findViewById(R.id.tv_detail_release_date);
        mVoteAverage = findViewById(R.id.tv_detail_vote_average);
        mOverview = findViewById(R.id.tv_detail_overview);
        mImagePoster = findViewById(R.id.iv_detail_movie_poster);
        mImageFavorite = findViewById(R.id.iv_favorite);
        mImageFavorite.setOnClickListener(view -> {
            Log.d(LOG_TAG, "Movie favorite clicked: " + mMovie.getId());
            mViewModel.setFavorite(mMovie);
        });

        mRecyclerView = findViewById(R.id.rv_trailers);
        mRecyclerView.setHasFixedSize(true);
        mTrailerAdapter = new TrailerAdapter(this);
        mRecyclerView.setAdapter(mTrailerAdapter);

        mErrorMessageDisplay = findViewById(R.id.tv_error_message_display);

        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);

        mRecyclerViewReview = findViewById(R.id.rv_reviews);
        mRecyclerView.setHasFixedSize(true);
        mReviewAdapter = new ReviewAdapter();
        mRecyclerViewReview.setAdapter(mReviewAdapter);

        mErrorMessageDisplayReview = findViewById(R.id.tv_error_message_display_review);

        mLoadingIndicatorReview = findViewById(R.id.pb_loading_indicator_review);

        // Remove back arrow from action bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);

        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra(EXTRA_MOVIE)) {
                mMovie = (Movie) intentThatStartedThisActivity.getSerializableExtra(EXTRA_MOVIE);
                if (mMovie != null) {
                    MovieDetailViewModelFactory factory = InjectorUtils.provideMovieDetailViewModelFactory(this.getApplicationContext(), mMovie.getId());
                    mViewModel = ViewModelProviders.of(this, factory).get(MovieDetailActivityViewModel.class);

                    bindToUI();

                    loadTrailerData();

                    loadReviewData();
                }
            }
        }
    }

    private void bindToUI() {
        Log.d(LOG_TAG, "bindToUI");
        mTitle.setText(mMovie.getTitle());
        mReleaseDate.setText(mMovie.getReleaseDate());
        mVoteAverage.setText(String.format(
                getApplicationContext().getString(R.string.vote_average_format),
                mMovie.getVoteAverage()
        ));
        mOverview.setText(mMovie.getOverview());
        mImagePoster.setContentDescription(mMovie.getTitle());

        Picasso.get()
                .load(mMovie.getPoster())
                .into(mImagePoster);

        mViewModel.isFavorite().observe(this, isFavorite -> {
            Log.d(LOG_TAG, "Movie isFavorite: " + isFavorite);
            if (isFavorite) {
                mImageFavorite.setImageResource(R.drawable.ic_star_black_48dp);
            } else {
                mImageFavorite.setImageResource(R.drawable.ic_star_border_black_48dp);
            }
            mImageFavorite.setVisibility(View.VISIBLE);
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
            else mLoadingIndicator.setVisibility(View.VISIBLE);
        });
    }

    private void showTrailerDataView() {
        /* First, make sure the error is invisible */
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        /* Then, make sure the trailer data is visible */
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void loadReviewData() {
        mViewModel.getReviews().observe(this, reviews -> {
            mReviewAdapter.setReviewData(reviews);
            if (reviews != null && reviews.size() != 0) showReviewDataView();
            else mLoadingIndicatorReview.setVisibility(View.VISIBLE);
        });
    }

    private void showReviewDataView() {
        /* First, make sure the error is invisible */
        mErrorMessageDisplayReview.setVisibility(View.INVISIBLE);
        /* Then, make sure the trailer data is visible */
        mRecyclerViewReview.setVisibility(View.VISIBLE);
    }
}