package com.watchmovies.repository;

import com.watchmovies.model.Movie;

public interface LocalDatabaseOperationCallback {

    void onSuccess(Movie movie);

    void onFailure();

}
