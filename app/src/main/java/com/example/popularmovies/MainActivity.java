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
import android.widget.Toast;

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
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;

    private ProgressBar mLoadingIndicator;
    private MainActivityViewModel mViewModel;

    private List<Movie> mMovies;
    private List<Movie> mMoviesFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);

        mRecyclerView = findViewById(R.id.rv_movies);

        mMovieAdapter = new MovieAdapter(this);
        mRecyclerView.setAdapter(mMovieAdapter);

        MainViewModelFactory factory = InjectorUtils.provideMainActivityViewModelFactory(this.getApplicationContext());
        mViewModel = ViewModelProviders.of(this, factory).get(MainActivityViewModel.class);

        mViewModel.getMovies().observe(this, movies -> {
            mMovies = movies;
            showData();
        });

        Log.d(LOG_TAG, "Main activity created");
    }

    private void showData() {
        List<Movie> movies;
        if (mViewModel.isShowFavorite()) {
            movies = mMoviesFavorite;
        } else {
            movies = mMovies;
        }
        mMovieAdapter.setMovieData(movies);
        if (movies != null && movies.size() != 0) showMovieDataView();
        else Toast.makeText(getApplicationContext(),
                "No favorites",
                Toast.LENGTH_LONG).show();
    }

    private void showMovieDataView() {
        Log.d(LOG_TAG, "showMovieDataView");
        mLoadingIndicator.setVisibility(View.INVISIBLE);
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
                mViewModel.setShowFavorite(false);
                // Force going top when changing
                mRecyclerView.smoothScrollToPosition(0);
                return true;
            case R.id.action_sort_by_top_rated:
                mViewModel.fetchMovies(NetworkUtils.Sort.TOP_RATED.name());
                mViewModel.setShowFavorite(false);
                mRecyclerView.smoothScrollToPosition(0);
                return true;
            case R.id.action_sort_favorite:
                mViewModel.setShowFavorite(true);
                showData();
                mRecyclerView.smoothScrollToPosition(0);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}