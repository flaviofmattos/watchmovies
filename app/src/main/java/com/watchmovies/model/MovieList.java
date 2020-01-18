package com.watchmovies.model;

import androidx.annotation.NonNull;
import android.util.Log;

import com.watchmovies.model.exception.MovieParseException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MovieList extends AbstractItemList<Movie> {

    private static final String TAG = MovieList.class.getName();


    private MovieList() {
        super();
    }


    public MovieList(@NonNull final List<Movie> movies) {
        this();
        mItemList.addAll(movies);
    }

    public MovieList(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            mItemList = new ArrayList<>(jsonArray.length());
            for (int i=0; i < jsonArray.length(); i++) {
                JSONObject obj = (JSONObject) jsonArray.get(i);
                mItemList.add(new Movie(obj.toString()));
            }

        } catch (JSONException e) {
            Log.e(TAG, e.getLocalizedMessage(), e);
            throw new MovieParseException();
        }
    }


}
