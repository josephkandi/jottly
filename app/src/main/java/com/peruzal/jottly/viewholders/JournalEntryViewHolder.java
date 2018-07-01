package com.peruzal.jottly.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.peruzal.jottly.R;

/**
 * Created by joseph on 6/30/18.
 */

public class JournalEntryViewHolder extends RecyclerView.ViewHolder {
    public TextView titleTextView;
    public TextView textTextView;
    public TextView updatedAtTextView;

    public JournalEntryViewHolder(View itemView, ItemClickListener itemClickListener) {
        super(itemView);
        titleTextView = itemView.findViewById(R.id.tv_title);
        textTextView = itemView.findViewById(R.id.tv_text);
        updatedAtTextView = itemView.findViewById(R.id.tv_updatedAt);
        itemView.setOnClickListener( view -> {
            itemClickListener.onItemClick(getAdapterPosition());
        });
    }

    public interface ItemClickListener {
        void onItemClick(int position);
    }
}
