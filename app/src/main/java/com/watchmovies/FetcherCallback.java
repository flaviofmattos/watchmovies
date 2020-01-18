package com.watchmovies;

import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;


public interface FetcherCallback<T> {

    default Handler getHandler() {
        return new Handler(Looper.getMainLooper());
    }

    void onSuccess(@NonNull T t);

    void onFailure();

    T getInstance(@NonNull String json);

}
