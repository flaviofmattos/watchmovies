package com.watchmovies.moviedetail;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.watchmovies.R;
import com.watchmovies.TrailerListItemClickListener;
import com.watchmovies.model.Movie;
import com.watchmovies.model.Review;
import com.watchmovies.model.Trailer;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MovieDetailFragment extends Fragment implements TrailerListItemClickListener {

    @BindView(R.id.tv_movie_detail_title)
    TextView mMovieTitle;

    @BindView(R.id.tv_movie_detail_release_date)
    TextView mMovieReleaseDate;

    @BindView(R.id.tv_movie_detail_synopsis)
    TextView mMovieSynopsis;

    @BindView(R.id.tv_movie_detail_vote_average)
    TextView mMovieVoteAverage;

    @BindView(R.id.iv_movie_detail_image)
    ImageView mMoviePoster;

    @BindView(R.id.rv_trailers)
    RecyclerView mTrailers;

    @BindView(R.id.tv_reviews_label)
    TextView mReviewsLabel;

    @BindView(R.id.rv_reviews)
    RecyclerView mReviews;

    @BindView(R.id.movie_detail_container)
    ScrollView mScrollView;

    @BindView(R.id.iv_favourite)
    ImageView mFavoriteImage;

    @BindView(R.id.tv_empty_reviews)
    TextView mNoReviewsAvailable;

    @BindView(R.id.tv_empty_trailers)
    TextView mNoTrailersAvailable;

    private RecyclerView.LayoutManager mTrailerLayoutManager;
    private RecyclerView.LayoutManager mReviewsLayoutManager;
    private MovieReviewAdapter mReviewAdapter = new MovieReviewAdapter(new ArrayList<>());
    private MovieTrailerAdapter mTrailerAdapter = new MovieTrailerAdapter(new ArrayList<>(), this);
    private MovieDetailViewModel mMovieDetailViewModel;

    private Movie mMovie;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.movie_detail_frag, container, false);
        mScrollView = container.findViewById(R.id.movie_detail_container);
        ButterKnife.bind(this, view);

        setupTrailers();
        setupReviews();
        setupMovie();
        setupErrorMessage();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mReviewsLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mReviews.isShown()) {
                    mReviews.setVisibility(View.GONE);
                    mNoReviewsAvailable.setVisibility(View.GONE);
                    mReviewsLabel.setText(R.string.show_reviews);
                    mReviewsLabel.
                            setCompoundDrawablesRelativeWithIntrinsicBounds(null, null,
                                    getResources()
                                            .getDrawable(R.drawable.ic_baseline_arrow_drop_down_24px,
                                                    null) , null);
                } else {
                    mReviews.setVisibility(View.VISIBLE);
                    mReviewsLabel.setText(R.string.hide_reviews);
                    mReviewsLabel.
                            setCompoundDrawablesRelativeWithIntrinsicBounds(null, null,
                                    getResources()
                                            .getDrawable(R.drawable.ic_baseline_arrow_drop_up_24px,
                                                    null) , null);
                    if (mReviews.getAdapter() != null && mReviews.getAdapter().getItemCount() > 0) {
                        scrollReviews();
                    } else {

                        mNoReviewsAvailable.setVisibility(mMovieDetailViewModel.getContainsReviews().getValue() ? View.GONE : View.VISIBLE);

                    }
                }
            }
        });
        mFavoriteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMovieDetailViewModel.handleFavoriteSelection(mMovie);
            }
        });

    }



    public void bindMovieToView(@NonNull Movie movie) {


        handleFavoriteAction(mMovie, movie);

        mMovie = movie;

        String releaseDate = String.format(getString(R.string.date_format), mMovie.getReleaseDate());
        String averageRate = String.format(getString(R.string.movie_rate_format), String.valueOf(mMovie.getStars()));

        mMovieTitle.setText(mMovie.getTitle());
        mMovieReleaseDate.setText(releaseDate);
        mMovieSynopsis.setText(mMovie.getPlotSynopsis());
        mMovieVoteAverage.setText(averageRate);
        mFavoriteImage.setImageResource(mMovie.getIsFavorite() ? R.drawable.ic_baseline_star_24px :
                R.drawable.ic_baseline_star_border_24px);
        Picasso.with(getContext())
                .load(mMovie.getPosterImageUrl())
                .placeholder(android.R.drawable.sym_def_app_icon)
                .into(mMoviePoster);
    }



    public void setupTrailers() {

        mTrailerLayoutManager = new LinearLayoutManager(getContext());
        mTrailers.setLayoutManager(mTrailerLayoutManager);
        mTrailers.setAdapter(mTrailerAdapter);
        mMovieDetailViewModel.getTrailers().observe(this, new Observer<List<Trailer>>() {
            @Override
            public void onChanged(List<Trailer> trailers) {
                mTrailerAdapter.updateData(trailers);
            }
        });
        mMovieDetailViewModel.getContainsTrailers().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean containsTrailers) {
                mNoTrailersAvailable.setVisibility(containsTrailers ? View.GONE : View.VISIBLE);
            }
        });


    }


    public void setupReviews() {

        mReviewsLayoutManager = new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        mReviews.setLayoutManager(mReviewsLayoutManager);
        mReviews.setAdapter(mReviewAdapter);
        mMovieDetailViewModel.getReviews().observe(this, new Observer<List<Review>>() {
            @Override
            public void onChanged(List<Review> reviews) {
                mReviewAdapter.swapData(reviews);
            }
        });

    }


    private void displayAddedToFavorites() {
        Toast.makeText(getContext(), R.string.movie_saved, Toast.LENGTH_LONG).show();
    }


    private void displayRemovedFromFavorites() {
        Toast.makeText(getContext(), R.string.movie_removed, Toast.LENGTH_LONG).show();
    }



    @Override
    public void onClick(Trailer trailer) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.parse(trailer.getLink());
        intent.setData(uri);
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivity(intent);
        }
    }


    protected void setViewModel(@NonNull final MovieDetailViewModel movieDetailViewModel) {
        mMovieDetailViewModel = movieDetailViewModel;
    }


   private void setupMovie() {
        mMovieDetailViewModel.getMovie().observe(this, new Observer<Movie>() {
            @Override
            public void onChanged(Movie movie) {
                bindMovieToView(movie);
            }
        });
   }

   private void setupErrorMessage() {
        mMovieDetailViewModel.getContainsErrors().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean containsError) {
                if (containsError) {
                     Toast.makeText(getContext(), getString(R.string.default_error_message), Toast.LENGTH_LONG).show();
                }
            }
        });
   }


    private void handleFavoriteAction(Movie before, Movie after) {

        if (before != null && before.equals(after)) {
            if (!before.getIsFavorite() && after.getIsFavorite()) {
                displayAddedToFavorites();
            } else if (before.getIsFavorite() && !after.getIsFavorite()) {
                displayRemovedFromFavorites();
            }
        }

    }


    private void scrollReviews() {
        final int coords[] = new int[2];
        mReviews.getLocationOnScreen(coords);
        final int scroll = coords[1] != 0 ? coords[1] / 2 : 0;
        mScrollView.getHandler().post(new Runnable() {
            @Override
            public void run() {
                mScrollView.smoothScrollTo(0, scroll);
            }
        });
    }
}
