package com.peruzal.jottly.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.peruzal.jottly.conveters.DateConverter;
import com.peruzal.jottly.dao.JournalEntryDao;
import com.peruzal.jottly.entities.JournalEntry;

/**
 * Created by joseph on 6/30/18.
 */

@Database(entities = {JournalEntry.class}, version = 1)
@TypeConverters(value = {DateConverter.class})
public abstract class AppDb extends RoomDatabase {
    private static final String DATABASE_NAME = "journal.db";

    public abstract JournalEntryDao journalEntryDao();

    private static AppDb INSTANCE;
    public static AppDb getDatabase(Context ctx) {
        if (INSTANCE == null){
            INSTANCE = Room.databaseBuilder(ctx,AppDb.class, DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }

    public void destroyInstace() {
        INSTANCE = null;
    }
}
