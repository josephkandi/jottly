package com.peruzal.jottly.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.support.annotation.NonNull;

import com.peruzal.jottly.entities.JournalEntry;
import com.peruzal.jottly.repository.LocalJournalRepository;
import com.peruzal.jottly.utils.JournalEntryAction;

/**
 * Created by joseph on 7/1/18.
 */

public class JournalViewModel extends AndroidViewModel {
    private MutableLiveData<JournalEntry> mEntry;
    private String mEmailAddress;

    public String getEmailAddress() {
        return mEmailAddress;
    }

    public void setEmailAddress(String mEmailAddress) {
        this.mEmailAddress = mEmailAddress;
    }

    private LocalJournalRepository mJournalRepository;
    private MutableLiveData<JournalEntryAction> mAction;

    public JournalViewModel(@NonNull Application application) {
        super(application);
        mEntry = new MutableLiveData<>();
        mAction = new MutableLiveData<>();
        mJournalRepository = new LocalJournalRepository(application, getEmailAddress());
    }

    public LiveData<JournalEntry> getJournalEntry() {
        return mEntry;
    }

    public LiveData<JournalEntryAction> getAction(){
        return mAction;
    }

    public void addJournalEntry(JournalEntry entry){
        mAction.postValue(JournalEntryAction.ADDED);
        mJournalRepository.insertJournalEntry(entry);
    }

    public void setJournalEntry(JournalEntry entry) {
        mEntry.setValue(entry);
        mAction.setValue(JournalEntryAction.SET);
    }

    public void updateJournalEntry(JournalEntry entry){
        mAction.setValue(JournalEntryAction.UPDATED);
        mJournalRepository.updateJournalEntry(entry);
    }
}
