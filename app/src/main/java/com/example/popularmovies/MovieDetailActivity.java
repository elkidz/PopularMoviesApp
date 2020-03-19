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
import androidx.recyclerview.widget.RecyclerView;

import com.example.popularmovies.data.Movie;
import com.example.popularmovies.data.Trailer;
import com.example.popularmovies.utils.MovieJsonUtils;
import com.example.popularmovies.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.Objects;

public class MovieDetailActivity extends AppCompatActivity implements TrailerAdapter.TrailerAdapterOnClickHandler{

    private static final String TAG = MovieDetailActivity.class.getSimpleName();
    public static final String EXTRA_MOVIE = "com.example.popularmovies.Movie";

    private TrailerAdapter mTrailerAdapter;
    private ReviewAdapter mReviewAdapter;

    private RecyclerView mRecyclerView;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;

    private RecyclerView mRecyclerViewReview;
    private TextView mErrorMessageDisplayReview;
    private ProgressBar mLoadingIndicatorReview;

    private Movie mMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        /* MOVIE */
        TextView mTitle = findViewById(R.id.tv_detail_title);
        TextView mReleaseDate = findViewById(R.id.tv_detail_release_date);
        TextView mVoteAverage = findViewById(R.id.tv_detail_vote_average);
        TextView mOverview = findViewById(R.id.tv_detail_overview);
        ImageView mImagePoster = findViewById(R.id.iv_detail_movie_poster);

        /* TRAILER */
        mRecyclerView = findViewById(R.id.rv_trailers);
        mRecyclerView.setHasFixedSize(true);
        mTrailerAdapter = new TrailerAdapter(this);
        mRecyclerView.setAdapter(mTrailerAdapter);

        mErrorMessageDisplay = findViewById(R.id.tv_error_message_display);
        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);

        /* REVIEW */
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
                    mTitle.setText(mMovie.getTitle());
                    mReleaseDate.setText(mMovie.getReleaseDate());
                    mVoteAverage.setText(Double.toString(mMovie.getVoteAverage()));
                    mOverview.setText(mMovie.getOverview());

                    Picasso.get()
                            .load(mMovie.getPoster())
                            .into(mImagePoster);

                    loadTrailerData();

                    loadReviewData();
                }
            }
        }
    }

    /* TRAILER STUFF */

    @Override
    public void onClick(Trailer trailer) {
        Uri youtubeUri = NetworkUtils.buildWatchUrl(trailer.getKey());

        Intent intentToWatchTrailer = new Intent(Intent.ACTION_VIEW, youtubeUri);
        startActivity(intentToWatchTrailer);
    }

    private void loadTrailerData() {
        showTrailerDataView();

        new FetchTrailerTask().execute(String.valueOf(mMovie.getId()));
    }

    private void showErrorMessageTrailer(String message) {
        /* First, hide the currently visible data */
        mRecyclerView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        if (message != null) {
            mErrorMessageDisplay.setText(message);
        }
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    private void showTrailerDataView() {
        /* First, make sure the error is invisible */
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        /* Then, make sure the trailer data is visible */
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    /**
     * Fetch trailers with AsyncTask
     */
    @SuppressLint("StaticFieldLeak")
    private class FetchTrailerTask extends AsyncTask<String, Void, String[]> {
        private String errorMessage;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String[] doInBackground(String... strings) {

            if (strings.length == 0) {
                return null;
            }

            String movieId = strings[0];
            URL url = NetworkUtils.buildTrailerUrl(movieId);

            try {
                String jsonResponse = NetworkUtils.getResponseFromHttpUrl(url);

                return MovieJsonUtils.getListFromJson(jsonResponse);

            } catch (Exception e) {
                errorMessage = "Error: " + e.getMessage();
                Log.d(TAG, errorMessage);
                return null;
            }
        }

        @Override
        protected void onPostExecute(String[] trailerData) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (trailerData != null) {
                showTrailerDataView();
                mTrailerAdapter.setTrailerData(trailerData);
            } else {
                showErrorMessageTrailer(errorMessage);
            }
        }
    }

    /* REVIEW STUFF */

    private void loadReviewData() {
        showReviewDataView();

        new FetchReviewTask().execute(String.valueOf(mMovie.getId()));
    }

    private void showErrorMessageReview(String message) {
        /* First, hide the currently visible data */
        mRecyclerViewReview.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        if (message != null) {
            mErrorMessageDisplayReview.setText(message);
        }
        mErrorMessageDisplayReview.setVisibility(View.VISIBLE);
    }

    private void showReviewDataView() {
        /* First, make sure the error is invisible */
        mErrorMessageDisplayReview.setVisibility(View.INVISIBLE);
        /* Then, make sure the trailer data is visible */
        mRecyclerViewReview.setVisibility(View.VISIBLE);
    }

    @SuppressLint("StaticFieldLeak")
    private class FetchReviewTask extends AsyncTask<String, Void, String[]> {
        private String errorMessage;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicatorReview.setVisibility(View.VISIBLE);
        }

        @Override
        protected String[] doInBackground(String... strings) {

            if (strings.length == 0) {
                return null;
            }

            String movieId = strings[0];
            URL requestUrl = NetworkUtils.buildReviewUrl(movieId);

            try {
                String jsonResponse = NetworkUtils.getResponseFromHttpUrl(requestUrl);

                return MovieJsonUtils.getListFromJson(jsonResponse);

            } catch (Exception e) {
                errorMessage = "Error: " + e.getMessage();
                Log.d(TAG, errorMessage);
                return null;
            }
        }

        @Override
        protected void onPostExecute(String[] reviewData) {
            super.onPostExecute(reviewData);
            mLoadingIndicatorReview.setVisibility(View.INVISIBLE);
            if (reviewData != null) {
                showReviewDataView();
                mReviewAdapter.setReviewData(reviewData);
            } else {
                showErrorMessageReview(errorMessage);
            }
        }
    }
}