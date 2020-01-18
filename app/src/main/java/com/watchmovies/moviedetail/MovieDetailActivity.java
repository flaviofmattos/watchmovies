package com.watchmovies.moviedetail;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.app.NavUtils;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.view.MenuItem;

import com.watchmovies.R;
import com.watchmovies.model.Movie;
import com.watchmovies.repository.MovieDatabase;
import com.watchmovies.repository.MovieRepository;
import com.watchmovies.util.ViewModelHolder;


public class MovieDetailActivity extends AppCompatActivity {

    private static final String MOVIE_DETAIL_VIEW_MODEL_TAG = "MOVIE_DETAIL_VIEW_MODEL_TAG";

    private MovieDatabase mMovieDatabase;
    private MovieRepository mMovieRepository;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail_activity);

        mMovieDatabase = MovieDatabase.getInstance(this);
        mMovieRepository = MovieRepository.getInstance(mMovieDatabase);

        if (getIntent().getParcelableExtra("movie") != null) {

            Movie movie = getIntent().getParcelableExtra("movie");
            MovieDetailFragment movieDetailFragment = getFragmentUIInstance();
            MovieDetailViewModel movieDetailViewModel = getViewModelInstance(movie);
            movieDetailFragment.setViewModel(movieDetailViewModel);
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }

    }



    private MovieDetailFragment getFragmentUIInstance() {

        MovieDetailFragment fragment = (MovieDetailFragment) getSupportFragmentManager()
                .findFragmentById(R.id.content_detail_frame);
        if (fragment == null) {
            fragment = new MovieDetailFragment();
        }


        if (!fragment.isAdded()) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.content_detail_frame, fragment);
            transaction.commit();
        }

        return fragment;

    }


    private MovieDetailViewModel getViewModelInstance(@NonNull final Movie movie) {

        ViewModelHolder viewModelHolder = (ViewModelHolder)
                getSupportFragmentManager().findFragmentByTag(MOVIE_DETAIL_VIEW_MODEL_TAG);
        MovieDetailViewModel movieDetailViewModel;
        if (viewModelHolder == null || viewModelHolder.getViewModel() == null) {

            movieDetailViewModel = ViewModelProviders.of(this, new MovieDetailViewModel.Factory(mMovieRepository, movie)).get(MovieDetailViewModel.class);
            viewModelHolder = ViewModelHolder.createViewModelHolder(movieDetailViewModel);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(viewModelHolder, MOVIE_DETAIL_VIEW_MODEL_TAG);
            transaction.commit();

        } else {
            movieDetailViewModel = (MovieDetailViewModel) viewModelHolder.getViewModel();
        }
        return movieDetailViewModel;

    }
}
