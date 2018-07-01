package com.peruzal.jottly.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.peruzal.jottly.R;
import com.peruzal.jottly.adapters.JournalEntryAdapter;
import com.peruzal.jottly.entities.JournalEntry;
import com.peruzal.jottly.repository.LocalJournalRepository;
import com.peruzal.jottly.utils.JournalEntryAction;
import com.peruzal.jottly.utils.NavigationListener;
import com.peruzal.jottly.viewmodels.JournalViewModel;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainFragment extends Fragment implements JournalEntryAdapter.ItemClickListener {
    private static final String TAG = MainFragment.class.getSimpleName();
    private LocalJournalRepository mJournalRepository;
    private RecyclerView mRecyclerView;
    private NavigationListener mNavigationListener;
    JournalEntryAdapter mAdapter;
    JournalViewModel mJournalViewModel;


    public static Fragment newInstance(NavigationListener navigationListener) {
        MainFragment fragment = new MainFragment();
        fragment.mNavigationListener = navigationListener;
        return fragment;
    }

    public MainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mJournalViewModel = ViewModelProviders.of(getActivity()).get(JournalViewModel.class);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mJournalRepository = new LocalJournalRepository(getActivity().getApplicationContext(), mJournalViewModel.getEmailAddress());

        mRecyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);

        view.findViewById(R.id.fab_add).setOnClickListener((v) -> mNavigationListener.setAction(JournalEntryAction.ADD));

        mAdapter = new JournalEntryAdapter(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);

        mJournalRepository.getAllJournals().observe(this, journalEntries -> mAdapter.setData(journalEntries));

        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));

        Snackbar snackbar = Snackbar.make(mRecyclerView, R.string.journal_deleted, Snackbar.LENGTH_LONG);


        ItemTouchHelper.SimpleCallback swipeToDeleteCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                JournalEntry entry = mAdapter.getJournalAt(position);
                snackbar.setAction(R.string.undo, (view -> {
                    mAdapter.addEntry(entry);
                    mJournalRepository.insertJournalEntry(entry);
                }));
                snackbar.show();
                mAdapter.removeJournalEntry(position);
                mJournalRepository.deleteJournalEntry(entry);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mNavigationListener = null;
    }

    @Override
    public void onItemClick(int position) {
        JournalEntry entry = mAdapter.getJournalAt(position);
        mJournalViewModel.setJournalEntry(entry);
        mNavigationListener.setAction(JournalEntryAction.VIEW);
    }

}
