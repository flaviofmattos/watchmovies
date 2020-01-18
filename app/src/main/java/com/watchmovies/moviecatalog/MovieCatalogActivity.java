package com.watchmovies.moviecatalog;


import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.view.Menu;
import android.view.MenuItem;

import com.watchmovies.R;
import com.watchmovies.moviepreference.PreferenceActivity;
import com.watchmovies.util.ViewModelHolder;



public class MovieCatalogActivity extends AppCompatActivity {

    private static final String MOVIE_CATALOG_VIEW_MODEL_TAG = "MOVIE_CATALOG_VIEW_MODEL_TAG";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        MovieCatalogFragment movieCatalogFragment = getFragmentUIInstance();
        MovieCatalogViewModel movieCatalogViewModel = getViewModelInstance();
        movieCatalogFragment.setViewModel(movieCatalogViewModel);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.preferences, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.mi_preferences) {
            Intent intent = new Intent(this, PreferenceActivity.class);
            startActivity(intent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }


    private MovieCatalogFragment getFragmentUIInstance() {

        MovieCatalogFragment fragment = (MovieCatalogFragment) getSupportFragmentManager()
                .findFragmentById(R.id.content_frame);
        if (fragment == null) {
            fragment = new MovieCatalogFragment();
        }


        if (!fragment.isAdded()) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.content_frame, fragment);
            transaction.commit();
        }

        return fragment;

    }


    private MovieCatalogViewModel getViewModelInstance() {

        ViewModelHolder viewModelHolder = (ViewModelHolder)
                getSupportFragmentManager().findFragmentByTag(MOVIE_CATALOG_VIEW_MODEL_TAG);
        MovieCatalogViewModel movieCatalogViewModel;
        if (viewModelHolder == null || viewModelHolder.getViewModel() == null) {

            movieCatalogViewModel = ViewModelProviders.of(this).get(MovieCatalogViewModel.class);
            viewModelHolder = ViewModelHolder.createViewModelHolder(movieCatalogViewModel);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(viewModelHolder, MOVIE_CATALOG_VIEW_MODEL_TAG);
            transaction.commit();

        } else {
            movieCatalogViewModel = (MovieCatalogViewModel) viewModelHolder.getViewModel();
        }
        return movieCatalogViewModel;

    }

}
