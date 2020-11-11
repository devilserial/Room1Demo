package com.dev.words;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Word.class}, version = 4, exportSchema = false)
/*
    单例设计模式single,此类只允许实例化一个对象
 */
public abstract class WordDatabase extends RoomDatabase {
    private static WordDatabase INSTANCE;

    public static synchronized WordDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), WordDatabase.class, "db_word")
//                    .fallbackToDestructiveMigration()
                    .addMigrations(MIGRATION_3_4)
                    .build();
        }
        return INSTANCE;
    }

    public abstract WordDao getWordDao();
    static final Migration MIGRATION_2_3 = new Migration(2,3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("alter table word add column bar_data integer not null default 0");
        }
    };
    static final Migration MIGRATION_3_4 = new Migration(3,4) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("create table word_temp (id integer primary key not null,englishWord TEXT,chineseMeaning text,bar_data integer not null default 0)");
            database.execSQL("insert into word_temp (id,englishWord,chineseMeaning,bar_data) select id,englishWord,chineseMeaning,bar_data from word;");
            database.execSQL("drop table word");
            database.execSQL("alter table word_temp rename to word");
        }
    };
}
