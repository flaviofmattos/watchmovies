package com.watchmovies.moviedetail;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.watchmovies.R;
import com.watchmovies.model.Review;

import java.util.List;

public class MovieReviewAdapter extends RecyclerView.Adapter<MovieReviewAdapter.MovieReviewViewHolder> {

    private List<Review> mReviews;

    public MovieReviewAdapter(@NonNull final List<Review> reviews) {
        this.mReviews = reviews;
    }

    @NonNull
    @Override
    public MovieReviewViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View layout = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.review_item, viewGroup, false);
        return new MovieReviewViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieReviewViewHolder movieReviewViewHolder, int i) {
        movieReviewViewHolder.mAuthorLabel.setText(R.string.review_author);
        movieReviewViewHolder.mAuthor.setText(mReviews.get(i).getAuthor());
        movieReviewViewHolder.mReview.setText(mReviews.get(i).getContent());

    }

    @Override
    public int getItemCount() {
        return mReviews.size();
    }

    public void swapData(@NonNull List<Review> list) {
        mReviews = list;
        notifyDataSetChanged();
    }

    class MovieReviewViewHolder extends RecyclerView.ViewHolder {

        private TextView mAuthorLabel;
        private TextView mAuthor;
        private TextView mReview;

        public MovieReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            this.mAuthorLabel = itemView.findViewById(R.id.tv_review_author_label);
            this.mAuthor = itemView.findViewById(R.id.tv_review_author);
            this.mReview = itemView.findViewById(R.id.tv_review);
        }

    }


}
