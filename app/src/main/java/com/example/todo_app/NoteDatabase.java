package com.example.todo_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


import android.content.Context;
import android.os.Bundle;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {notesEn.class}, version = 1, exportSchema = false)
abstract class NoteMeDatabase extends RoomDatabase {

    public abstract notesDao notesdao();

    private static volatile NoteMeDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;

    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static NoteMeDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (NoteMeDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            NoteMeDatabase.class, "NoteMeDatabase")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}