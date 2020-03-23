package com.example.popularmovies.utils;

import android.content.Context;

import com.example.popularmovies.data.MovieRepository;
import com.example.popularmovies.data.database.MovieDatabase;
import com.example.popularmovies.data.network.MovieNetworkDataSource;
import com.example.popularmovies.viewmodel.MainViewModelFactory;
import com.example.popularmovies.viewmodel.MovieDetailViewModelFactory;

/**
 * Provides static methods to inject the various classes needed for Movie
 */
public class InjectorUtils {

    private static MovieRepository provideRepository(Context context) {
        MovieDatabase database = MovieDatabase.getInstance(context.getApplicationContext());
        AppExecutors executors = AppExecutors.getInstance();
        MovieNetworkDataSource networkDataSource = MovieNetworkDataSource.getInstance(executors);
        return MovieRepository.getInstance(database.movieDao(), networkDataSource, executors);
    }

    public static MainViewModelFactory provideMainActivityViewModelFactory(Context context) {
        MovieRepository repository = provideRepository(context.getApplicationContext());
        return new MainViewModelFactory(repository);
    }

    public static MovieDetailViewModelFactory provideMovieDetailViewModelFactory(Context context, int movieId) {
        MovieRepository repository = provideRepository(context.getApplicationContext());
        return new MovieDetailViewModelFactory(repository, movieId);
    }
}
