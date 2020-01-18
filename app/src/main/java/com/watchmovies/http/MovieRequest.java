package com.watchmovies.http;

import androidx.annotation.NonNull;


import com.watchmovies.FetcherCallback;

import java.util.HashMap;
import java.util.Map;


public class MovieRequest extends BaseRequest {

    private final String mResource;
    private final Map<String, Object> mParameters;
    private final FetcherCallback mCallback;


    private MovieRequest(@NonNull Builder builder) {
        this.mResource = builder.resource;
        this.mParameters = builder.parameters;
        this.mCallback = builder.callback;
    }



    public Map<String, Object> getParameters() {
        return mParameters;
    }

    public String getResource() {
        return mResource;
    }

    public FetcherCallback getCallback() {
        return mCallback;
    }


    public static class Builder {

        private final String TOP_RATED_RESOURCE = "/movie/top_rated";
        private final String MOST_POPULAR_RESOURCE = "/movie/popular";

        private final Map<String, Object> parameters;
        private String resource;
        private FetcherCallback callback;

        public Builder() {
            this.parameters = new HashMap<>();
        }


        public MovieRequest.Builder topRated() {
            this.resource = TOP_RATED_RESOURCE;
            return this;
        }

        public MovieRequest.Builder mostPopular() {
            this.resource = MOST_POPULAR_RESOURCE;
            return this;
        }


        public MovieRequest.Builder usingLanguage(@NonNull String language) {
            parameters.put("language", language);
            return this;
        }

        public MovieRequest.Builder atPage(@NonNull Integer number) {
            parameters.put("page", number);
            return this;
        }

        public MovieRequest.Builder withCallback(FetcherCallback callback) {
            this.callback = callback;
            return this;
        }

        public MovieRequest build() {
            return new MovieRequest(this);
        }
    }
}
