package com.dev.room1demo;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Word.class}, version = 1, exportSchema = false)
/*
    单例设计模式single,此类只允许实例化一个对象
 */
public abstract class WordDatabase extends RoomDatabase {
    private static WordDatabase INSTANCE;

    public static synchronized WordDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), WordDatabase.class, "db_word")
                    .build();
        }
        return INSTANCE;
    }

    public abstract WordDao getWordDao();
}
