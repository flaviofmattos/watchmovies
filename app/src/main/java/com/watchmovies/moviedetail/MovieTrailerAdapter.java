package com.watchmovies.moviedetail;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.watchmovies.R;
import com.watchmovies.TrailerListItemClickListener;
import com.watchmovies.model.Trailer;

import java.util.List;

public class MovieTrailerAdapter extends RecyclerView.Adapter<MovieTrailerAdapter.MovieTrailerViewHolder> {


    private List<Trailer> mTrailers;
    private TrailerListItemClickListener mTrailerListItemClickListener;

    public MovieTrailerAdapter(@NonNull final List<Trailer> trailers,
                               @NonNull final TrailerListItemClickListener listener) {
        this.mTrailers = trailers;
        this.mTrailerListItemClickListener = listener;
    }

    @NonNull
    @Override
    public MovieTrailerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.trailer_item, viewGroup, false);
        return new MovieTrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MovieTrailerViewHolder movieTrailerViewHolder,
                                 final int i) {
        final Trailer trailer = mTrailers.get(i);
        movieTrailerViewHolder.mName.setText("Trailer "+ (i+1));
        movieTrailerViewHolder.mPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTrailerListItemClickListener.onClick(trailer);
            }
        });
        movieTrailerViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTrailerListItemClickListener.onClick(trailer);
            }
        });

    }


    @Override
    public int getItemCount() {
        return mTrailers.size();
    }


    public void updateData(@NonNull final List<Trailer> trailers) {
        this.mTrailers = trailers;
        notifyDataSetChanged();
    }


    class MovieTrailerViewHolder extends RecyclerView.ViewHolder {

        private ImageButton mPlay;
        private TextView mName;
        private View mView;

        public MovieTrailerViewHolder(@NonNull View itemView) {
            super(itemView);
            mPlay = itemView.findViewById(R.id.btn_play);
            mName = itemView.findViewById(R.id.tv_trailer_name);
            mView = itemView;
        }
    }


}
