package com.watchmovies.moviedetail;

import com.watchmovies.FetcherCallback;
import com.watchmovies.http.exception.InvalidMovieApiKeyException;
import com.watchmovies.model.Movie;
import com.watchmovies.model.Review;
import com.watchmovies.model.ReviewList;
import com.watchmovies.model.Trailer;
import com.watchmovies.model.TrailerList;
import com.watchmovies.repository.LocalDatabaseOperationCallback;

import com.watchmovies.repository.MovieRepository;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;


public class MovieDetailViewModel extends ViewModel {

    private MovieRepository mMovieRepository;


    private MutableLiveData<Movie> mMovie = new MutableLiveData<>();
    private MutableLiveData<List<Trailer>> mTrailers = new MutableLiveData<>();
    private MutableLiveData<List<Review>> mReviews = new MutableLiveData<>();
    private MutableLiveData<Boolean> mContainsTrailers = new MutableLiveData<>();
    private MutableLiveData<Boolean> mContainsReviews = new MutableLiveData<>();
    private MutableLiveData<Boolean> containsErrors = new MutableLiveData<>();


    public MovieDetailViewModel(@NonNull MovieRepository movieRepository, @NonNull Movie movie) {

        try {
            mMovieRepository = movieRepository;
            mMovie.postValue(movie);
            mMovieRepository.fetchMovieById(new MovieDetailLocalCheckCallback(), movie.getId());
            mMovieRepository.fetchMovieTrailers(new MovieDetailTrailersCallback(), movie);
            mMovieRepository.fetchMovieReviews(new MovieDetailReviewsCallback(), movie);
        } catch (InvalidMovieApiKeyException e) {
            //do nothing
        } catch (Exception e) {
            containsErrors.setValue(true);
            containsErrors.postValue(true);
        }


    }

    public void handleFavoriteSelection(@NonNull final Movie movie) {

        if (movie.getIsFavorite()) {
            Movie moveToBeDeleted = new Movie(movie.getId(), movie.getTitle(), movie.getReleaseDate(),
                    movie.getPosterImageUrl(), movie.getStars(), movie.getPlotSynopsis(), false, movie.getPosterImage());
            mMovieRepository.delete(moveToBeDeleted, new MovieDetailOperationCallback());
        } else {
            Movie moveToBeSaved = new Movie(movie.getId(), movie.getTitle(), movie.getReleaseDate(),
                    movie.getPosterImageUrl(), movie.getStars(), movie.getPlotSynopsis(), true, movie.getPosterImage());
            mMovieRepository.insert(moveToBeSaved, new MovieDetailOperationCallback());
        }

    }

    public MutableLiveData<Movie> getMovie() {
        return mMovie;
    }

    public MutableLiveData<List<Trailer>> getTrailers() {
        return mTrailers;
    }

    public MutableLiveData<List<Review>> getReviews() {
        return mReviews;
    }

    public MutableLiveData<Boolean> getContainsTrailers() {
        return mContainsTrailers;
    }

    public MutableLiveData<Boolean> getContainsReviews() {
        return mContainsReviews;
    }


    public MutableLiveData<Boolean> getContainsErrors() {
        return containsErrors;
    }

    public static class Factory implements ViewModelProvider.Factory {

        private final Movie mMovie;

        private final MovieRepository mMovieRepository;

        public Factory(MovieRepository movieRepository, Movie movie) {
            mMovieRepository = movieRepository;
            mMovie = movie;
        }


        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> clazz) {

            if (clazz.isAssignableFrom(MovieDetailViewModel.class)) {
                return (T) new MovieDetailViewModel(mMovieRepository, mMovie);
            }
            throw new IllegalArgumentException("Class not supported");
        }
    }


    private class MovieDetailTrailersCallback implements FetcherCallback<TrailerList> {

        @Override
        public void onSuccess(@NonNull TrailerList trailerList) {
            containsErrors.postValue(false);
            mTrailers.setValue(trailerList.getList());
            mContainsTrailers.setValue(trailerList.getList() != null && !trailerList.getList().isEmpty());
        }

        @Override
        public void onFailure() {
            containsErrors.postValue(true);
        }

        @Override
        public TrailerList getInstance(@NonNull String json) {
            return new TrailerList(json);
        }
    }


    private class MovieDetailReviewsCallback implements FetcherCallback<ReviewList> {

        @Override
        public void onSuccess(@NonNull ReviewList reviewList) {
            containsErrors.postValue(false);
            mReviews.setValue(reviewList.getList());
            mContainsReviews.setValue(reviewList.getList() != null && !reviewList.getList().isEmpty());
        }

        @Override
        public void onFailure() {
            containsErrors.postValue(true);
        }

        @Override
        public ReviewList getInstance(@NonNull String json) {
            return new ReviewList(json);
        }
    }


    private class MovieDetailOperationCallback implements LocalDatabaseOperationCallback {

        @Override
        public void onSuccess(Movie movie) {
            mMovie.postValue(movie);
            containsErrors.postValue(false);
        }


        @Override
        public void onFailure() {
            containsErrors.postValue(true);
        }
    }


    private class MovieDetailLocalCheckCallback implements LocalDatabaseOperationCallback {

        @Override
        public void onSuccess(Movie movie) {
            if (movie != null) {
                mMovie.postValue(movie);
                containsErrors.postValue(false);
            }
        }

        @Override
        public void onFailure() {
            containsErrors.postValue(true);
        }

    }


}
