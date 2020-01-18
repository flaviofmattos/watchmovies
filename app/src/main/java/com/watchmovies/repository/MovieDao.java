package com.watchmovies.repository;

import com.watchmovies.model.Movie;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface MovieDao {


    @Query("SELECT * FROM movies")
    LiveData<List<Movie>> getAll();

    @Query("SELECT * FROM movies WHERE id = :id")
    Movie getById(int id);

    @Delete
    void delete(Movie movie);

    @Insert(onConflict = OnConflictStrategy.ROLLBACK)
    void insert(Movie movie);


}
