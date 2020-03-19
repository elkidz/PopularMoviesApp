package com.example.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.popularmovies.data.Movie;
import com.example.popularmovies.data.Trailer;
import com.example.popularmovies.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class MovieDetailActivity extends AppCompatActivity implements TrailerAdapter.TrailerAdapterOnClickHandler{

    private static final String TAG = MovieDetailActivity.class.getSimpleName();
    public static final String EXTRA_MOVIE = "com.example.popularmovies.Movie";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        TextView mTitle = findViewById(R.id.tv_detail_title);
        TextView mReleaseDate = findViewById(R.id.tv_detail_release_date);
        TextView mVoteAverage = findViewById(R.id.tv_detail_vote_average);
        TextView mOverview = findViewById(R.id.tv_detail_overview);
        ImageView mImagePoster = findViewById(R.id.iv_detail_movie_poster);

        // Remove back arrow from action bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);

        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra(EXTRA_MOVIE)) {
                Movie mMovie = (Movie) intentThatStartedThisActivity.getSerializableExtra(EXTRA_MOVIE);
                if (mMovie != null) {
                    mTitle.setText(mMovie.getTitle());
                    mReleaseDate.setText(mMovie.getReleaseDate());
                    mVoteAverage.setText(Double.toString(mMovie.getVoteAverage()));
                    mOverview.setText(mMovie.getOverview());

                    Picasso.get()
                            .load(mMovie.getPoster())
                            .into(mImagePoster);
                }
            }
        }
    }


    @Override
    public void onClick(Trailer trailer) {
        Uri youtubeUri = NetworkUtils.buildWatchUrl(trailer.getKey());

        Intent intentToWatchTrailer = new Intent(Intent.ACTION_VIEW, youtubeUri);
        startActivity(intentToWatchTrailer);
    }
}