package com.watchmovies.http;

import androidx.annotation.NonNull;

import com.watchmovies.FetcherCallback;

public class TrailerRequest extends BaseRequest {

    private final String TRAILER_RESOURCE = "/movie/%s/videos";

    private final FetcherCallback mCallback;
    private final int mMovieId;
    private final String mResource;

    public TrailerRequest(final int movieId, @NonNull final FetcherCallback callback) {
        this.mCallback = callback;
        this.mMovieId = movieId;
        this.mResource = String.format(TRAILER_RESOURCE, mMovieId);
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
