package com.example.popularmovies;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.popularmovies.data.database.Movie;
import com.example.popularmovies.utils.InjectorUtils;
import com.example.popularmovies.viewmodel.MainActivityViewModel;
import com.example.popularmovies.viewmodel.MainViewModelFactory;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment implements MovieAdapter.MovieAdapterOnClickHandler {

    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;

    private ProgressBar mLoadingIndicator;
    private MainActivityViewModel mViewModel;

    private List<Movie> mMovies;
    private List<Movie> mMoviesFavorite;

    public MainFragment() {
        // Required empty public constructor
    }

    private NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mLoadingIndicator = view.findViewById(R.id.pb_loading_indicator);

        mRecyclerView = view.findViewById(R.id.rv_movies);

        mMovieAdapter = new MovieAdapter(this);
        mRecyclerView.setAdapter(mMovieAdapter);

        MainViewModelFactory factory = InjectorUtils.provideMainActivityViewModelFactory(this.getApplicationContext());
        mViewModel = new ViewModelProvider(this, factory).get(MainActivityViewModel.class);

        mViewModel.getMovies().observe(this, movies -> {
            mMovies = movies;
            showData();
        });

        mViewModel.getMoviesFavorite().observe(this, movies -> {
            mMoviesFavorite = movies;
            showData();
        });

        navController = Navigation.findNavController(view);

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
        else showLoading();
    }

    private void showLoading() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    private void showMovieDataView() {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(Movie movie) {

    }
}
