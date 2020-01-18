package com.watchmovies.http;

import com.watchmovies.FetcherCallback;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseRequest {

    public abstract String getResource();

    public Map<String, Object> getParameters() {
        return new HashMap<>();
    }

    public abstract FetcherCallback getCallback();

}
