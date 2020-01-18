package com.watchmovies.http;

import androidx.annotation.NonNull;

import com.watchmovies.FetcherCallback;

public class ReviewRequest extends BaseRequest {

    private final String REVIEW_RESOURCE = "/movie/%s/reviews";

    private final FetcherCallback mCallback;
    private final int mMovieId;
    private final String mResource;


    public ReviewRequest(final int movieId, @NonNull final FetcherCallback callback) {
        this.mCallback = callback;
        this.mMovieId = movieId;
        this.mResource = String.format(REVIEW_RESOURCE, mMovieId);
    }


    @Override
    public String getResource() {
        return mResource;
    }

    @Override
    public FetcherCallback getCallback() {
        return mCallback;
    }


}
