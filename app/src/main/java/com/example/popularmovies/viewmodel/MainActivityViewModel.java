package com.example.popularmovies.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.popularmovies.data.database.Movie;
import com.example.popularmovies.data.MovieRepository;
import com.example.popularmovies.utils.NetworkUtils;

import java.util.List;

public class MainActivityViewModel extends ViewModel {

    private final MovieRepository mRepository;
    private final LiveData<List<Movie>> mMovies;
    private final LiveData<List<Movie>> mMoviesFavorite;
    private boolean showFavorite = false;

    MainActivityViewModel(MovieRepository repository) {
        mRepository = repository;
        fetchMovies(NetworkUtils.Sort.POPULAR.name());
        mMovies = mRepository.getMovies();
        mMoviesFavorite = mRepository.getMoviesFavorite();
    }

    public LiveData<List<Movie>> getMovies() {
        return mMovies;
    }

    public LiveData<List<Movie>> getMoviesFavorite() {
        return mMoviesFavorite;
    }

    public void fetchMovies(String sortName) {
        mRepository.fetchMovies(sortName);
    }

    public boolean isShowFavorite() {
        return showFavorite;
    }

    public void setShowFavorite(boolean showFavorite) {
        this.showFavorite = showFavorite;
    }
}
