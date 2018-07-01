package com.peruzal.jottly.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

/**
 * Created by joseph on 6/30/18.
 */

@Entity(tableName = "entry")
public class JournalEntry {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String title;
    private String entry;
    private Date updatedAt;
    private String emailAddress;

    public JournalEntry() {
        updatedAt = new Date();
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEntry() {
        return entry;
    }

    public void setEntry(String entry) {
        this.entry = entry;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return String.format("%d %s %s %s", id, title, entry, updatedAt);
    }

    @Override
    public int hashCode() {
        return 7;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof JournalEntry){
            JournalEntry entry = (JournalEntry)obj;
            return entry.getId() == id;
        }
        return false;
    }
}
