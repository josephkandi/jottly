package com.peruzal.jottly.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.peruzal.jottly.R;
import com.peruzal.jottly.entities.JournalEntry;
import com.peruzal.jottly.utils.JournalEntryAction;
import com.peruzal.jottly.utils.NavigationListener;
import com.peruzal.jottly.viewmodels.JournalViewModel;

import java.text.SimpleDateFormat;
import java.util.Locale;


public class ViewJournalEntryFragment extends Fragment {
    private JournalViewModel mJournalViewModel;
    private NavigationListener mNavigationListener;
    private TextView tvTitle;
    private TextView tvText;
    private TextView tvUpdateAt;
    private static SimpleDateFormat mSimpleDateFormat;
    private JournalEntryAction mJournalAction;

    public ViewJournalEntryFragment() {
    }

    public static ViewJournalEntryFragment newInstance(NavigationListener listener) {
        ViewJournalEntryFragment fragment = new ViewJournalEntryFragment();
        fragment.mNavigationListener = listener;
        fragment.setHasOptionsMenu(true);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mSimpleDateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm", Locale.ENGLISH);
        return inflater.inflate(R.layout.fragment_view_journal_entry, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mJournalViewModel = ViewModelProviders.of(getActivity()).get(JournalViewModel.class);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvTitle = view.findViewById(R.id.tv_title);
        tvText = view.findViewById(R.id.tv_text);
        tvUpdateAt = view.findViewById(R.id.tv_updatedAt);

        mJournalViewModel.getJournalEntry().observe(this, entry -> updateUI(entry));
    }

    private void updateUI(JournalEntry entry) {
        tvTitle.setText(entry.getTitle());
        tvText.setText(entry.getEntry());
        tvUpdateAt.setText(mSimpleDateFormat.format(entry.getUpdatedAt()));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_view_journal, menu);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mNavigationListener = null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_done:
                mNavigationListener.setAction(JournalEntryAction.ACTION_COMPLETED);
                break;
            case R.id.action_edit:
                mNavigationListener.setAction(JournalEntryAction.UPDATE);
        }
        return true;
    }
}
