package com.watchmovies.model;

import androidx.annotation.NonNull;

import com.watchmovies.model.exception.MovieParseException;

import org.json.JSONException;
import org.json.JSONObject;

public class Review {

    private String mId;

    private String mAuthor;

    private String mContent;

    private Review() {}

    public Review(@NonNull final String author, @NonNull final String content) {
        this.mAuthor = author;
        this.mContent = content;
    }

    public Review(@NonNull String json) {

        try {
            JSONObject jsonObject = new JSONObject(json);
            this.mId = jsonObject.getString("id");
            this.mAuthor = jsonObject.getString("author");
            this.mContent = jsonObject.optString("content");
        } catch (JSONException e) {
            throw new MovieParseException();
        }

    }


    public String getId() {
        return mId;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getContent() {
        return mContent;
    }
}
