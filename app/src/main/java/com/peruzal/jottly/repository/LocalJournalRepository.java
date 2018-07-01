package com.peruzal.jottly.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;

import com.peruzal.jottly.db.AppDb;
import com.peruzal.jottly.entities.JournalEntry;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by joseph on 6/30/18.
 */

public class LocalJournalRepository {
    private AppDb appDb;
    private ExecutorService mExecutor = Executors.newSingleThreadExecutor();
    private MutableLiveData<String> mError = new MutableLiveData<>();
    private String mEmailAddress;


    public LocalJournalRepository(Context ctx, String emailAddress){
        appDb = AppDb.getDatabase(ctx);
        this.mEmailAddress = emailAddress;
    }

    public LiveData<List<JournalEntry>> getAllJournals() {
        try {
            return mExecutor.submit(() -> appDb.journalEntryDao().getAllJournals(mEmailAddress)).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            mError.setValue(e.getMessage());
        } catch (ExecutionException e) {
            e.printStackTrace();
            mError.setValue(e.getMessage());
        }
        return null;
    }

    public LiveData<List<JournalEntry>> getJournalEntriesByDate(Date date) {
        try {
            return mExecutor.submit(() -> appDb.journalEntryDao().getJournalEntriesByDate(mEmailAddress, date)).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            mError.setValue(e.getMessage());
        } catch (ExecutionException e) {
            e.printStackTrace();
            mError.setValue(e.getMessage());
        }
        return null;
    }

    public LiveData<List<JournalEntry>> getJournalEntries(int limit) {
        try {
            return mExecutor.submit(() -> appDb.journalEntryDao().getJournalEntries(mEmailAddress, limit)).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            mError.setValue(e.getMessage());
        } catch (ExecutionException e) {
            e.printStackTrace();
            mError.setValue(e.getMessage());
        }
        return null;
    }

    public void updateJournalEntry(JournalEntry entry){
        mExecutor.execute(() -> appDb.journalEntryDao().updateJournalEntry(entry));
    }

    public LiveData<List<JournalEntry>> getJournalsBySearchTerm(String searchTerm) {

        try {
            return mExecutor.submit(() -> appDb.journalEntryDao().getJournalsByText(mEmailAddress, searchTerm)).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            mError.setValue(e.getMessage());
        } catch (ExecutionException e) {
            e.printStackTrace();
            mError.setValue(e.getMessage());
        }
        return null;
    }


    public void insertJournalEntry(JournalEntry entry) {
        mExecutor.execute(() -> appDb.journalEntryDao().insertJournalEntry(entry));
    }

    public void  deleteJournalEntry(JournalEntry entry) {
        mExecutor.execute(() -> appDb.journalEntryDao().deleteJournalEntry(entry));
    }

    public void deleteAllJournalEntries() {
        mExecutor.execute(() -> appDb.journalEntryDao().deleteAllJournalEntries());
    }
}
