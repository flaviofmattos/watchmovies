package com.watchmovies.moviecatalog;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.watchmovies.MovieListItemClickListener;
import com.watchmovies.R;
import com.watchmovies.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;



public class MovieCatalogAdapter extends RecyclerView.Adapter<MovieCatalogAdapter.MovieCatalogViewHolder> {

    private List<Movie> mMovieList;
    private final MovieListItemClickListener mListItemClickListener;


    public MovieCatalogAdapter(@NonNull final List<Movie> movieList,
                               @NonNull final MovieListItemClickListener listItemClickListener) {
        this.mMovieList = movieList;
        this.mListItemClickListener = listItemClickListener;
    }

    @NonNull
    @Override
    public MovieCatalogViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View listItemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.movie_catalog_item, viewGroup, false);
        return new MovieCatalogViewHolder(listItemView, this.mListItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieCatalogViewHolder movieCatalogViewHolder, int i) {
        Picasso.with(movieCatalogViewHolder.mImageView.getContext())
                .load(mMovieList.get(i).getPosterImageUrl())
                .placeholder(android.R.drawable.sym_def_app_icon)
                .into(movieCatalogViewHolder.mImageView);
    }

    @Override
    public int getItemCount() {
        return mMovieList.size();
    }

    public void swapData(@NonNull List<Movie> movieList) {
        this.mMovieList = movieList;
        notifyDataSetChanged();
    }

    static class MovieCatalogViewHolder extends RecyclerView.ViewHolder {

        private final CardView mCardView;
        private final ImageView mImageView;

        MovieCatalogViewHolder(@NonNull final View itemView,
                                      @NonNull final MovieListItemClickListener listItemClickListener) {
            super(itemView);
            this.mCardView = itemView.findViewById(R.id.cv_movie_catalog_item);
            mImageView = itemView.findViewById(R.id.iv_movie_catalog_item_image);
            this.mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listItemClickListener.onItemClicked(getAdapterPosition());
                }
            });
        }
    }

}
