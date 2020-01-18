package com.watchmovies.moviecatalog;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.watchmovies.MovieListItemClickListener;
import com.watchmovies.R;
import com.watchmovies.model.Movie;
import com.watchmovies.moviedetail.MovieDetailActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MovieCatalogFragment extends Fragment implements
        SharedPreferences.OnSharedPreferenceChangeListener, MovieListItemClickListener {

    @BindView(R.id.rv_movie_catalog)
    RecyclerView mMovieCatalogRecyclerView;

    @BindView(R.id.pb_loading_movies)
    View mProgressBar;

    @BindView(R.id.tv_invalid_api_key)
    TextView mErrorMessage;

    @BindView(R.id.tv_no_movies_available)
    TextView mNoMoviesLabel;

    @BindView(R.id.iv_icon_no_movies_available)
    ImageView mNoMoviesIcon;

    private RecyclerView.LayoutManager mLayoutManager;
    private MovieCatalogAdapter mAdapter = new MovieCatalogAdapter(new ArrayList<>(), this);
    private MovieCatalogViewModel mMovieCatalogViewModel;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.movie_catalog_frag, container, false);
        ButterKnife.bind(this, view);

        setupRecyclerView();
        setupMoviesObserver();
        setupLoadingObserver();
        setupErrorMessages();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        PreferenceManager.getDefaultSharedPreferences(getContext())
                .registerOnSharedPreferenceChangeListener(this);

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        PreferenceManager.getDefaultSharedPreferences(getContext())
                .unregisterOnSharedPreferenceChangeListener(this);
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        mMovieCatalogViewModel.loadMovies();
    }

    @Override
    public void onItemClicked(int position) {
        Movie movie = null;
        if (shouldDisplayFavorites()) {
            movie = mMovieCatalogViewModel.getLocalMovies().getValue().get(position);
        } else {
            movie = mMovieCatalogViewModel.getMovies().getValue().get(position);
        }

        showMovieDetail(movie);
    }


    protected void setViewModel(@NonNull final MovieCatalogViewModel movieCatalogViewModel) {
        this.mMovieCatalogViewModel = movieCatalogViewModel;
    }


    private void setupRecyclerView() {
        mLayoutManager = new GridLayoutManager(getActivity(), getSpanCount());
        mMovieCatalogRecyclerView.setLayoutManager(mLayoutManager);
        mMovieCatalogRecyclerView.setAdapter(mAdapter);
        mMovieCatalogRecyclerView.setHasFixedSize(false);

    }


    private void setupMoviesObserver() {
        mMovieCatalogViewModel.getMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                if (movies == null || movies.isEmpty()) {
                    mAdapter.swapData(new ArrayList<>());
                    showNoMoviesAvailable();
                } else {
                    hideNoMoviesAvailable();
                    mAdapter.swapData(movies);
                }
            }
        });

        mMovieCatalogViewModel.getLocalMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {

                if (shouldDisplayFavorites()) {
                    if (movies == null || movies.isEmpty()) {
                        mAdapter.swapData(new ArrayList<>());
                        showNoMoviesAvailable();
                    } else {
                        hideNoMoviesAvailable();
                        mAdapter.swapData(movies);
                    }
                }
            }
        });
    }


    private void setupLoadingObserver() {
        mMovieCatalogViewModel.isLoading().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                if (isLoading) {
                    showProgressBar();
                } else {
                    hideProgressBar();
                }
            }
        });
    }

    private void setupErrorMessages() {
        mMovieCatalogViewModel.isMissingApiKey().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isMissingApiKey) {
                if (isMissingApiKey) {
                    displayConfigurationError();
                } else {
                    dismissConfigurationError();
                }
            }
        });
        mMovieCatalogViewModel.containsErrors().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean containsErrors) {
                if (containsErrors) {
                    showErrorMessage();
                }
            }
        });

    }


    private void showMovieDetail(Parcelable movie) {
        Intent intent = new Intent(getContext(), MovieDetailActivity.class);
        intent.putExtra("movie", movie);
        startActivity(intent);
    }


    private void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
    }


    private void hideProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }


    public void showErrorMessage() {
        Toast.makeText(getContext(), getString(R.string.default_error_message), Toast.LENGTH_LONG)
                .show();
    }


    private void displayConfigurationError() {
        mErrorMessage.setVisibility(View.VISIBLE);
    }


    private void dismissConfigurationError() {
        mErrorMessage.setVisibility(View.GONE);
    }



    private void showNoMoviesAvailable() {
        mNoMoviesLabel.setVisibility(View.VISIBLE);
        mNoMoviesIcon.setVisibility(View.VISIBLE);
    }



    private void hideNoMoviesAvailable() {
        mNoMoviesLabel.setVisibility(View.GONE);
        mNoMoviesIcon.setVisibility(View.GONE);
    }



    private int getSpanCount() {
        int spanCount = 3;
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            spanCount = 5;
        }
        return spanCount;
    }



    private boolean shouldDisplayFavorites() {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(getContext());
        String key = getString(R.string.key_sort_mode);
        String defaultValue = getString(R.string.value_sort_mode_popular);
        return sharedPreferences.getString(key, defaultValue).equals("favourite");
    }






}
