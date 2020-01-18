package com.watchmovies.http;

import androidx.annotation.NonNull;
import android.util.Log;

import com.watchmovies.FetcherCallback;
import com.watchmovies.http.exception.HttpMovieException;
import com.watchmovies.http.exception.InvalidMovieApiKeyException;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.Map;


public final class MovieHttp {

    private static final String TAG = MovieHttp.class.getName();

    private static final OkHttpClient client = new OkHttpClient();
    private static final String API_HOST_URL = "https://api.themoviedb.org/3";
    private static final String API_IMAGE_URL = "http://image.tmdb.org/t/p/w342";
    private static final String API_KEY_PARAM = "api_key";
    private static final String API_KEY = "";



    public static void get(BaseRequest movieRequest) throws InvalidMovieApiKeyException {

        try {
            validateApiKey();
            HttpUrl.Builder urlBuilder = HttpUrl.parse(API_HOST_URL + movieRequest.getResource())
                    .newBuilder()
                    .addQueryParameter(API_KEY_PARAM, API_KEY);
            addRequestParameters(urlBuilder, movieRequest.getParameters());
            String url = urlBuilder.build().toString();
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            client.newCall(request).enqueue(new MovieHttpCallback(movieRequest.getCallback()));

        } catch (InvalidMovieApiKeyException ae) {
            throw ae;
        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage(), e);
            throw new HttpMovieException(e);
        }

    }


    public static String buildUrlFromPath(@NonNull String relativePath) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(API_IMAGE_URL + relativePath).newBuilder();
        return urlBuilder.build().toString();
    }


    private static void addRequestParameters(HttpUrl.Builder urlBuilder, Map<String, Object> params) {
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            urlBuilder.addQueryParameter(entry.getKey(), entry.toString());
        }
    }


    private static class MovieHttpCallback implements Callback {

        private final FetcherCallback mCallback;

        MovieHttpCallback(FetcherCallback callback) {
            this.mCallback = callback;
        }


        @Override
        public void onFailure(Request request, IOException e) {
            Log.w(TAG, e.getMessage(), e);
            if (mCallback.getHandler() != null) {
                mCallback.getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        mCallback.onFailure();
                    }
                });
            } else {
                mCallback.onFailure();
            }

        }

        @Override
        public void onResponse(final Response response) throws IOException {

            final Object result = mCallback.getInstance(response.body().string());
            if (mCallback.getHandler() != null) {
                mCallback.getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        mCallback.onSuccess(result);
                    }
                });
            } else {
                mCallback.onSuccess(result);
            }
        }
    }

    private static void validateApiKey() {
        if (API_KEY.equals("")) {
            throw new InvalidMovieApiKeyException();
        }
    }
}
