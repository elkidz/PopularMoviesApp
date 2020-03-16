package com.example.popularmovies;

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
import androidx.recyclerview.widget.RecyclerView;

import com.example.popularmovies.utils.MovieUtils;
import com.example.popularmovies.utils.NetworkUtils;

import java.net.URL;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {

    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;

    private TextView mErrorMessageDisplay;

    private ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.rv_movies);
        mErrorMessageDisplay = findViewById(R.id.tv_error_message_display);
        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);

        mRecyclerView.setHasFixedSize(true);

        mMovieAdapter = new MovieAdapter(this);
        mRecyclerView.setAdapter(mMovieAdapter);

        loadMovieData(NetworkUtils.Sort.POPULAR.name());
    }

    private void loadMovieData(String sortName) {
        showMovieDataView();

        new FetchMovieTask().execute(sortName);
    }

    private void showMovieDataView() {
        /* First, make sure the error is invisible */
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        /* Then, make sure the weather data is visible */
        mRecyclerView.setVisibility(View.VISIBLE);
    }


    private void showErrorMessage(String message) {
        /* First, hide the currently visible data */
        mRecyclerView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        if (message != null) {
            mErrorMessageDisplay.setText(message);
        }
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(String movie) {

    }

    public class FetchMovieTask extends AsyncTask<String, Void, String[]> {
        private String errorMessage;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String[] doInBackground(String... params) {

            if (params.length == 0) {
                return null;
            }

            String sortName = params[0];
            URL weatherRequestUrl = NetworkUtils.buildUrl(NetworkUtils.Sort.valueOf(sortName));

            try {
                String jsonResponse = NetworkUtils
                        .getResponseFromHttpUrl(weatherRequestUrl);

                String[] simpleData = MovieUtils.getListFromJson(jsonResponse);

                return simpleData;

            } catch (Exception e) {
                errorMessage = "Error: " + e.getMessage();
                Log.d(TAG, errorMessage);
                return null;
            }
        }

        @Override
        protected void onPostExecute(String[] movieData) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (movieData != null) {
                showMovieDataView();
                mMovieAdapter.setMovieData(movieData);
            } else {
                showErrorMessage(errorMessage);
            }
        }
    }


    /**
     * Menu
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
                loadMovieData(NetworkUtils.Sort.POPULAR.name());
                return true;
            case R.id.action_sort_by_top_rated:
                loadMovieData(NetworkUtils.Sort.TOP_RATED.name());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
