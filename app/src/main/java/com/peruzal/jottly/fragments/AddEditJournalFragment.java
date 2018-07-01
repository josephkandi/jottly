package com.peruzal.jottly.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toolbar;

import com.google.android.gms.dynamic.IFragmentWrapper;
import com.peruzal.jottly.R;
import com.peruzal.jottly.entities.JournalEntry;
import com.peruzal.jottly.utils.JournalEntryAction;
import com.peruzal.jottly.utils.NavigationListener;
import com.peruzal.jottly.viewmodels.JournalViewModel;

import java.util.Date;

public class AddEditJournalFragment extends Fragment {
    private NavigationListener mNavigationListener;
    private JournalViewModel mJournalViewModel;
    private TextInputEditText tieTitle;
    private TextInputEditText tieText;
    private JournalEntryAction mEntryAction;
    private final String TAG = AddEditJournalFragment.class.getSimpleName();
    private long mJournalId;

    public AddEditJournalFragment(){}

    public static Fragment newInstance(NavigationListener listener, JournalEntryAction entryAction) {
        AddEditJournalFragment fragment = new AddEditJournalFragment();
        fragment.mNavigationListener = listener;
        fragment.mEntryAction = entryAction;
        fragment.setHasOptionsMenu(true);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        return inflater.inflate(R.layout.activity_add_or_edit_journal,container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tieTitle = view.findViewById(R.id.tie_title);
        tieText = view.findViewById(R.id.tie_description);

        if (mEntryAction == JournalEntryAction.UPDATE) {
            mJournalViewModel.getJournalEntry().observe(this, entry -> {
                if (!TextUtils.isEmpty(entry.getTitle())){
                    tieTitle.setText(entry.getTitle());
                }
                if (!TextUtils.isEmpty(entry.getEntry())){
                    tieText.setText(entry.getEntry());
                }

                mJournalId = entry.getId();
            });
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mJournalViewModel = ViewModelProviders.of(getActivity()).get(JournalViewModel.class);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mNavigationListener = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_journal_add, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_add:
                saveJournal();
                break;
            case R.id.action_cancel:
                Log.d(TAG, "Action completed");
                mNavigationListener.setAction(JournalEntryAction.ACTION_COMPLETED);
                break;
        }
        return true;
    }

    private void saveJournal() {
        String title = tieTitle.getText().toString();
        String text = tieText.getText().toString();

        if (TextUtils.isEmpty(title)){
            tieTitle.setError(getString(R.string.err_title_required));
            return;
        }

        if (TextUtils.isEmpty(text)){
            tieText.setError(getString(R.string.err_empty_note));
            return;
        }

        JournalEntry entry = new JournalEntry();
        entry.setEntry(text);
        entry.setTitle(title);
        entry.setEmailAddress(mJournalViewModel.getEmailAddress());

        switch (mEntryAction){
            case ADD:

                mJournalViewModel.addJournalEntry(entry);
                break;
            case UPDATE:
                entry.setId(mJournalId);
                entry.setUpdatedAt(new Date());
                mJournalViewModel.updateJournalEntry(entry);
                break;
        }

        mNavigationListener.setAction(JournalEntryAction.ACTION_COMPLETED);
    }
}
