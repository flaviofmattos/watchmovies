package com.watchmovies.model;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractItemList<T> {

    protected int mPage;

    protected int mTotal;

    protected int mTotalPages;

    protected List<T> mItemList = new ArrayList<>();

    protected AbstractItemList() {
    }

    public AbstractItemList(int page, int total, int totalPages, @NonNull List<T> items) {
        this.mPage = page;
        this.mTotal = total;
        this.mTotalPages = totalPages;
        this.mItemList = items;
    }

    public AbstractItemList(@NonNull final String json) throws JSONException {

        JSONObject jsonObject = new JSONObject(json);
        this.mPage = jsonObject.getInt("page");
        this.mTotal = jsonObject.getInt("total_results");
        this.mTotalPages = jsonObject.getInt("total_pages");

    }

    public int getPage() {
        return mPage;
    }

    public int getTotal() {
        return mTotal;
    }

    public int getTotalPages() {
        return mTotalPages;
    }

    public List<T> getList() {
        return mItemList;
    }

}
