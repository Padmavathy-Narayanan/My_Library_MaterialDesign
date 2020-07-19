package com.padmavathy.mylibrary.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.padmavathy.mylibrary.R;
import com.padmavathy.mylibrary.adapter.BookListAdapter;
import com.padmavathy.mylibrary.adapter.LentInAdapter;
import com.padmavathy.mylibrary.adapter.WishlistAdapter;
import com.padmavathy.mylibrary.database.DatabaseHelper;
import com.padmavathy.mylibrary.database.LentInDatabaseHelper;
import com.padmavathy.mylibrary.database.WishlistDatabaseHelper;
import com.padmavathy.mylibrary.model.Book;
import com.padmavathy.mylibrary.model.LentIn;
import com.padmavathy.mylibrary.utils.MyDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class FragmentA extends androidx.fragment.app.Fragment {
    private RecyclerView recyclerView;
    private LentInAdapter mAdapter;
    private List<LentIn> notesList = new ArrayList<>();
    private LentInDatabaseHelper db;
    Book n;
    TextView noNotesView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View postView2 = inflater.inflate(R.layout.fragment_a, container, false);

        recyclerView = (RecyclerView)postView2.findViewById(R.id.recycler_view_lentdetails);
        noNotesView = postView2.findViewById(R.id.empty_notes_view_lent_details);

        mAdapter =  new LentInAdapter(getContext(),notesList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(mAdapter);
        toggleEmptyNotes();


        db = new LentInDatabaseHelper(getContext());
        if(notesList!=null && notesList.size()>0){
            notesList.clear();
        }
        notesList.addAll(db.getAllLentIns());

        return postView2;
    }

    private void toggleEmptyNotes() {
        // you can check notesList.size() > 0
        LentInDatabaseHelper db2 =  new LentInDatabaseHelper((getContext()));
        if (db2.getLentInsCount() > 0) {
            noNotesView.setText("No records");
            noNotesView.setVisibility(View.GONE);
        } else {
            noNotesView.setVisibility(View.VISIBLE);
        }
    }
}
