package com.watchmovies.model;

import android.util.Log;

import com.watchmovies.model.exception.MovieParseException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ReviewList extends AbstractItemList<Review> {

    private static final String TAG = ReviewList.class.getName();

    private ReviewList() {
        super();
    }

    public ReviewList(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            mItemList = new ArrayList<>(jsonArray.length());
            for (int i=0; i < jsonArray.length(); i++) {
                JSONObject obj = (JSONObject) jsonArray.get(i);
                mItemList.add(new Review(obj.toString()));
            }

        } catch (JSONException e) {
            Log.e(TAG, e.getLocalizedMessage(), e);
            throw new MovieParseException();
        }
    }


}
