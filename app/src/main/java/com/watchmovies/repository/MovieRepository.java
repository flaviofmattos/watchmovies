package com.watchmovies.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.watchmovies.FetcherCallback;
import com.watchmovies.http.MovieHttp;
import com.watchmovies.http.MovieRequest;
import com.watchmovies.http.ReviewRequest;
import com.watchmovies.http.TrailerRequest;
import com.watchmovies.model.Movie;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MovieRepository {

    private static final Object LOCK = new Object();

    private static MovieRepository mInstance;

    private ExecutorService executor = Executors.newSingleThreadExecutor();

    private MovieDatabase mMovieDatabase;


    private MovieRepository() {
    }

    private MovieRepository(@NonNull final MovieDatabase movieDatabase) {
        this();
        this.mMovieDatabase = movieDatabase;
    }

    public static MovieRepository getInstance(@NonNull final MovieDatabase movieDatabase) {
        if (mInstance == null) {
            synchronized (LOCK) {
                if (mInstance == null) {
                    mInstance = new MovieRepository(movieDatabase);
                }
            }
        }
        return mInstance;
    }

    public void fetchMostPopularMovies(@NonNull final FetcherCallback movieFetcherCallback,
                                       int pageNumber) {
        MovieRequest request = new MovieRequest.Builder()
                .mostPopular()
                .withCallback(movieFetcherCallback)
                .atPage(pageNumber)
                .build();
        MovieHttp.get(request);
    }

    public void fetchBestRatedMovies(@NonNull final FetcherCallback movieFetcherCallback,
                                     int pageNumber) {
        MovieRequest request = new MovieRequest.Builder()
                .topRated()
                .withCallback(movieFetcherCallback)
                .atPage(pageNumber)
                .build();
        MovieHttp.get(request);
    }


    public LiveData<List<Movie>> fetchFavoriteMovies() {
        return mMovieDatabase.movieDao().getAll();
    }


    public void fetchMovieById(@NonNull final LocalDatabaseOperationCallback callback, final int id) {

        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Movie movie = mMovieDatabase.movieDao().getById(id);
                    callback.onSuccess(movie);
                } catch (Exception e) {
                    callback.onFailure();
                }

            }
        });
    }


    public void fetchMovieTrailers(@NonNull final FetcherCallback movieFetcherCallback,
                                   @NonNull final Movie movie) {

        TrailerRequest request = new TrailerRequest(movie.getId(), movieFetcherCallback);
        MovieHttp.get(request);
    }


    public void fetchMovieReviews(@NonNull final FetcherCallback movieFetcherCallback,
                                  @NonNull final Movie movie) {
        ReviewRequest request = new ReviewRequest(movie.getId(), movieFetcherCallback);
        MovieHttp.get(request);
    }




    public synchronized void insert(@NonNull final Movie movie,
                                    @NonNull final LocalDatabaseOperationCallback callback) {

        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    mMovieDatabase.movieDao().insert(movie);
                    callback.onSuccess(movie);
                } catch (Exception e) {
                    callback.onFailure();
                }

            }
        });

    }


    public synchronized void delete(@NonNull final Movie movie,
                                    @NonNull final LocalDatabaseOperationCallback callback) {

        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    mMovieDatabase.movieDao().delete(movie);
                    callback.onSuccess(movie);
                } catch (Exception e) {
                    callback.onFailure();
                }
            }
        });
    }


}
