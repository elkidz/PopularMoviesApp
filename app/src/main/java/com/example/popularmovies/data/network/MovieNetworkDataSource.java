package com.example.popularmovies.data.network;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.popularmovies.data.Review;
import com.example.popularmovies.data.Trailer;
import com.example.popularmovies.data.database.Movie;
import com.example.popularmovies.utils.AppExecutors;
import com.example.popularmovies.utils.MovieJsonUtils;
import com.example.popularmovies.utils.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * Provides an API for doing all operations with the server data
 */
public class MovieNetworkDataSource {
    private static final String LOG_TAG = MovieNetworkDataSource.class.getSimpleName();

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static MovieNetworkDataSource sInstance;

    private final MutableLiveData<List<Movie>> mMovieList;
    private final MutableLiveData<List<Trailer>> mTrailerList;
    private final MutableLiveData<List<Review>> mReviewList;
    private final AppExecutors mExecutors;

    private MovieNetworkDataSource(AppExecutors executors) {
        mExecutors = executors;
        mMovieList = new MutableLiveData<>();
        mTrailerList = new MutableLiveData<>();
        mReviewList = new MutableLiveData<>();
    }

    /**
     * Get the singleton for this class
     */
    public static MovieNetworkDataSource getInstance(AppExecutors executors) {
        Log.d(LOG_TAG, "Getting the network data source");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new MovieNetworkDataSource(executors);
                Log.d(LOG_TAG, "Made new network data source");
            }
        }
        return sInstance;
    }

    public LiveData<List<Movie>> getMovies() {
        return mMovieList;
    }

    public LiveData<List<Trailer>> getTrailers() {
        return mTrailerList;
    }

    public LiveData<List<Review>> getReviews() {
        return mReviewList;
    }

    public void fetchMovies(String sortName) {
        Log.d(LOG_TAG, "Fetch movies started: " + sortName);
        mExecutors.networkIO().execute(() -> {
            URL requestUrl = NetworkUtils.buildUrl(NetworkUtils.Sort.valueOf(sortName));

            try {
                String jsonResponse = NetworkUtils.getResponseFromHttpUrl(requestUrl);

                mMovieList.postValue(MovieJsonUtils.getMoviesFromJson(jsonResponse));

            } catch (IOException e) {
                String errorMessage = "IOException: " + e.getMessage();
                Log.d(LOG_TAG, errorMessage);
            } catch (JSONException e) {
                String errorMessage = "JSONException: " + e.getMessage();
                Log.d(LOG_TAG, errorMessage);
            }
        });
    }

    public void fetchTrailers(String movieId) {
        Log.d(LOG_TAG, "Fetch trailers started: " + movieId);
        mExecutors.networkIO().execute(() -> {
            URL requestUrl = NetworkUtils.buildTrailerUrl(movieId);

            try {
                String jsonResponse = NetworkUtils.getResponseFromHttpUrl(requestUrl);

                mTrailerList.postValue(MovieJsonUtils.getTrailersFromJson(jsonResponse));

            } catch (IOException e) {
                String errorMessage = "IOException: " + e.getMessage();
                Log.d(LOG_TAG, errorMessage);
            } catch (JSONException e) {
                String errorMessage = "JSONException: " + e.getMessage();
                Log.d(LOG_TAG, errorMessage);
            }
        });
    }

    public void fetchReviews(String movieId) {
        Log.d(LOG_TAG, "Fetch reviews started: " + movieId);
        mExecutors.networkIO().execute(() -> {
            URL requestUrl = NetworkUtils.buildReviewUrl(movieId);

            try {
                String jsonResponse = NetworkUtils.getResponseFromHttpUrl(requestUrl);

                mReviewList.postValue(MovieJsonUtils.getReviewsFromJson(jsonResponse));

            } catch (IOException e) {
                String errorMessage = "IOException: " + e.getMessage();
                Log.d(LOG_TAG, errorMessage);
            } catch (JSONException e) {
                String errorMessage = "JSONException: " + e.getMessage();
                Log.d(LOG_TAG, errorMessage);
            }
        });
    }

}
