package com.watchmovies.moviecatalog;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.watchmovies.FetcherCallback;
import com.watchmovies.R;
import com.watchmovies.http.exception.InvalidMovieApiKeyException;
import com.watchmovies.model.Movie;
import com.watchmovies.model.MovieList;
import com.watchmovies.repository.MovieDatabase;
import com.watchmovies.repository.MovieRepository;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class MovieCatalogViewModel extends AndroidViewModel {

    private MovieRepository mMovieRepository;

    private LiveData<List<Movie>> mLocalMovies;
    private MutableLiveData<List<Movie>> mMovies = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private MutableLiveData<Boolean> isMissingApiKey = new MutableLiveData<>();
    private MutableLiveData<Boolean> containsErrors = new MutableLiveData<>();


    public MovieCatalogViewModel(@NonNull Application application) {
        super(application);
        mMovieRepository = MovieRepository.getInstance(MovieDatabase.getInstance(application));
        mLocalMovies = mMovieRepository.fetchFavoriteMovies();
        loadMovies();
    }


    public MutableLiveData<List<Movie>> getMovies() {
        return mMovies;
    }

    public LiveData<List<Movie>> getLocalMovies() {
        return mLocalMovies;
    }

    public MutableLiveData<Boolean> isLoading() {
        return isLoading;
    }

    public MutableLiveData<Boolean> containsErrors() {
        return containsErrors;
    }

    public MutableLiveData<Boolean> isMissingApiKey() {
        return isMissingApiKey;
    }


    private String getSortMode() {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(getApplication());
        String key = getApplication().getString(R.string.key_sort_mode);
        String defaultValue = getApplication().getString(R.string.value_sort_mode_popular);
        return sharedPreferences.getString(key, defaultValue);
    }

    public void loadMovies() {
        try {
            String sortMode = getSortMode();
            if (sortMode.equals("favourite")) {
                mMovies.postValue(mLocalMovies.getValue());
            } else if (sortMode.equals("rate")) {
                isLoading.postValue(true);
                mMovieRepository.fetchBestRatedMovies(new DefaultFetcherCallback(), 0);
            } else {
                isLoading.postValue(true);
                mMovieRepository.fetchMostPopularMovies(new DefaultFetcherCallback(), 0);
            }
            containsErrors.postValue(false);
            isMissingApiKey.postValue(false);
        } catch (InvalidMovieApiKeyException e) {
            isMissingApiKey.setValue(true);
            isMissingApiKey.postValue(true);
            isLoading.setValue(false);
            isLoading.postValue(false);
        } catch (Exception e) {
            isLoading.setValue(false);
            isLoading.postValue(false);
            containsErrors.setValue(true);
            containsErrors.postValue(true);
        }

    }


    private class DefaultFetcherCallback implements FetcherCallback<MovieList> {

        @Override
        public void onSuccess(@NonNull MovieList movieList) {
            mMovies.postValue(movieList.getList());
            isLoading.postValue(false);
            containsErrors.postValue(false);
        }

        @Override
        public void onFailure() {
            isLoading.postValue(false);
            containsErrors.postValue(true);
        }

        @Override
        public MovieList getInstance(@NonNull String json) {
            return new MovieList(json);
        }
    }


}
