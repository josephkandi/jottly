package com.peruzal.jottly.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.peruzal.jottly.entities.JournalEntry;

import java.util.Date;
import java.util.List;

/**
 * Created by joseph on 6/30/18.
 */

@Dao
public interface JournalEntryDao {

    @Query("SELECT * FROM entry WHERE emailAddress = :emailAddress  ORDER BY updatedAt DESC")
    LiveData<List<JournalEntry>> getAllJournals(String emailAddress);

    @Query("SELECT * FROM entry WHERE updatedAt = :date AND emailAddress = :emailAddress")
    LiveData<List<JournalEntry>> getJournalEntriesByDate(String emailAddress, Date date);

    @Query("SELECT * FROM entry WHERE emailAddress = :emailAddress LIMIT :limit")
    LiveData<List<JournalEntry>> getJournalEntries(String emailAddress, int limit);

    @Query("SELECT * FROM entry WHERE emailAddress = :emailAddress AND  (title LIKE :searchTerm OR  entry LIKE :searchTerm)")
    LiveData<List<JournalEntry>> getJournalsByText(String emailAddress, String searchTerm);

    @Update
    void updateJournalEntry(JournalEntry entry);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertJournalEntry(JournalEntry journalEntry);

    @Delete
    void deleteJournalEntry(JournalEntry journalEntry);

    @Query("DELETE FROM  entry")
    void deleteAllJournalEntries();
}
