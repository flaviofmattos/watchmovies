package com.watchmovies.repository;

import android.content.Context;


import com.watchmovies.model.Movie;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;


@Database(entities = {Movie.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class MovieDatabase extends RoomDatabase {

    private static final String DB_NAME = "movies_3_db";

    private static final Object LOCK = new Object();

    private static MovieDatabase sInstance;

    public static MovieDatabase getInstance(@NonNull final Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                if (sInstance == null) {
                    sInstance = Room.databaseBuilder(context.getApplicationContext(),
                            MovieDatabase.class, DB_NAME)
                            .build();
                }
            }
        }
        return sInstance;
    }

    public abstract MovieDao movieDao();

}
