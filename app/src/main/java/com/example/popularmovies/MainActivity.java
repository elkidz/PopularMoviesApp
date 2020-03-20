package com.example.popularmovies;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.example.popularmovies.data.database.Movie;
import com.example.popularmovies.utils.InjectorUtils;
import com.example.popularmovies.utils.MovieJsonUtils;
import com.example.popularmovies.utils.NetworkUtils;
import com.example.popularmovies.viewmodel.MainActivityViewModel;
import com.example.popularmovies.viewmodel.MainViewModelFactory;

import java.net.URL;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;

    private TextView mErrorMessageDisplay;

    private ProgressBar mLoadingIndicator;
    private MainActivityViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.rv_movies);
        mErrorMessageDisplay = findViewById(R.id.tv_error_message_display);
        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);

        mMovieAdapter = new MovieAdapter(this);
        mRecyclerView.setAdapter(mMovieAdapter);

        MainViewModelFactory factory = InjectorUtils.provideMainActivityViewModelFactory(this.getApplicationContext());
        mViewModel = ViewModelProviders.of(this, factory).get(MainActivityViewModel.class);

        mViewModel.getMovies().observe(this, movies -> {
            mMovieAdapter.setMovieData(movies);
            if (movies != null && movies.size() != 0) showMovieDataView();
            else showLoading();
        });

        Log.d(LOG_TAG, "Main activity created");
    }

    private void showMovieDataView() {
        /* First, make sure the error is invisible */
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        /* Then, make sure the movie data is visible */
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(Movie movie) {
        Log.d(LOG_TAG, "onClick: " + movie.getId());
        Class destinationClass = MovieDetailActivity.class;
        Intent intentToStartDetailActivity = new Intent(this, destinationClass);
        intentToStartDetailActivity.putExtra(MovieDetailActivity.EXTRA_MOVIE, movie);
        startActivity(intentToStartDetailActivity);
    }

    private void showLoading() {
        Log.d(LOG_TAG, "Loading");
        mRecyclerView.setVisibility(View.INVISIBLE);
        // Show the loading indicator
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    /**
     * MENU
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sort, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_sort_by_popular:
                mViewModel.fetchMovies(NetworkUtils.Sort.POPULAR.name());
                // Force going top when changing
                mRecyclerView.smoothScrollToPosition(0);
                return true;
            case R.id.action_sort_by_top_rated:
                mViewModel.fetchMovies(NetworkUtils.Sort.TOP_RATED.name());
                // Force going top when changing
                mRecyclerView.smoothScrollToPosition(0);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}