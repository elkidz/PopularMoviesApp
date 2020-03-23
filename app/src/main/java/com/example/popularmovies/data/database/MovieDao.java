package com.example.popularmovies.data.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM movie")
    LiveData<List<Movie>> getMoviesFavorite();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Movie movie);

    @Query("DELETE FROM movie WHERE id = :movieId")
    void delete(int movieId);

    @Query("SELECT EXISTS(SELECT 1 FROM movie WHERE id = :movieId LIMIT 1)")
    LiveData<Boolean> isFavorite(int movieId);
}
