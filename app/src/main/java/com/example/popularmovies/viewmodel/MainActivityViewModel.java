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

    MainActivityViewModel(MovieRepository repository) {
        mRepository = repository;
        fetchMovies(NetworkUtils.Sort.POPULAR.name());
        mMovies = mRepository.getMovies();
    }

    public LiveData<List<Movie>> getMovies() {
        return mMovies;
    }

    public void fetchMovies(String sortName) {
        mRepository.fetchMovies(sortName);
    }
}
