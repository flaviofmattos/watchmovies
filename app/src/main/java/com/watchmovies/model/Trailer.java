package com.watchmovies.model;

import androidx.annotation.NonNull;

import com.watchmovies.model.exception.MovieParseException;

import org.json.JSONException;
import org.json.JSONObject;

public class Trailer {

    private static final String YOUTUBE_URL = "https://www.youtube.com/watch?v=";

    private String mId;

    private String mName;

    private String mKey;

    private String mLink;

    public Trailer(@NonNull final String name, @NonNull final String key) {
        this.mName = name;
        this.mKey = key;
        this.mLink = YOUTUBE_URL + this.mKey;
    }


    public Trailer(@NonNull final String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            this.mId = jsonObject.getString("id");
            this.mName = jsonObject.optString("name");
            this.mKey = jsonObject.optString("key");
            this.mLink = YOUTUBE_URL + this.mKey;

        } catch (JSONException e) {
            throw new MovieParseException();
        }
    }


    public String getName() {
        return mName;
    }


    public String getLink() {
        return mLink;
    }

}
