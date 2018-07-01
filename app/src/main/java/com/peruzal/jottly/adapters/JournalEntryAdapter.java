package com.peruzal.jottly.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.peruzal.jottly.R;
import com.peruzal.jottly.entities.JournalEntry;
import com.peruzal.jottly.viewholders.EmptyJournalViewHolder;
import com.peruzal.jottly.viewholders.JournalEntryViewHolder;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by joseph on 6/30/18.
 */

public class JournalEntryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_EMPTY_LIST_PLACEHOLDER = 0;
    private static final int VIEW_TYPE_ENTRY_VIEW = 1;
    private static final int VIEW_TYPE_HEADER_VIEW = 1;
    private List<JournalEntry> mEntries;
    static SimpleDateFormat mSimpleDateFormat;
    private static final String TAG = JournalEntryAdapter.class.getSimpleName();
    private ItemClickListener mItemClickListener;

    public JournalEntryAdapter(ItemClickListener listener) {
        mItemClickListener = listener;
        mEntries = new ArrayList<>();

        mSimpleDateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm");
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType){
            case VIEW_TYPE_EMPTY_LIST_PLACEHOLDER:
             itemView = inflater.inflate(R.layout.item_journal_empty_view,parent, false);
            return new EmptyJournalViewHolder(itemView);
            case VIEW_TYPE_ENTRY_VIEW:
                itemView = inflater.inflate(R.layout.item_journal_entry,parent, false);
                return new JournalEntryViewHolder(itemView, (position -> {
                    mItemClickListener.onItemClick(position);
                }));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof JournalEntryViewHolder){
            JournalEntry journalEntry = mEntries.get(position);
            if (journalEntry == null){
                return;
            }

            JournalEntryViewHolder jvh = (JournalEntryViewHolder) holder;

            jvh.titleTextView.setText(journalEntry.getTitle());
            jvh.textTextView.setText(journalEntry.getEntry());

            String updateAt = mSimpleDateFormat.format(journalEntry.getUpdatedAt());
            jvh.updatedAtTextView.setText(updateAt);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mEntries.size() == 0){
            return VIEW_TYPE_EMPTY_LIST_PLACEHOLDER;
        }
        return VIEW_TYPE_ENTRY_VIEW;
    }


    @Override
    public int getItemCount() {
        return mEntries.size() > 0 ? mEntries.size() : 1;
    }

    public void setData(List<JournalEntry> entries){
        mEntries.clear();
        mEntries.addAll(entries);
        notifyDataSetChanged();
    }

    public void addEntry(JournalEntry entry){
        mEntries.add(entry);
        notifyItemInserted(mEntries.size() - 1);
    }

    public JournalEntry getJournalAt(int position){
        return mEntries.get(position);
    }

    public void  removeJournalEntry(int position) {
        Log.d(TAG,"Removed? " + mEntries.remove(position));
        notifyItemRemoved(position);
    }

    public List<JournalEntry> getItems(){
        return mEntries;
    }


    public interface ItemClickListener {
        void onItemClick(int position);
    }
}
