package com.example.popularmovies;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.popularmovies.data.database.Movie;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment implements MovieAdapter.MovieAdapterOnClickHandler {

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
        navController
    }

    @Override
    public void onClick(Movie movie) {

    }
}
