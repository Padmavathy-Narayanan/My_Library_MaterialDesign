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
import com.padmavathy.mylibrary.adapter.LentOutAdapter;
import com.padmavathy.mylibrary.database.LentInDatabaseHelper;
import com.padmavathy.mylibrary.database.LentOutDatabaseHelper;
import com.padmavathy.mylibrary.model.Book;
import com.padmavathy.mylibrary.model.LentOut;
import com.padmavathy.mylibrary.utils.MyDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class FragmentB extends androidx.fragment.app.Fragment {
    private RecyclerView recyclerView;
    private LentOutAdapter mAdapter;
    private List<LentOut> notesList = new ArrayList<>();
    private LentOutDatabaseHelper db;
    Book n;
    TextView noNotesView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View postView2 = inflater.inflate(R.layout.fragment_b, container, false);

        recyclerView = (RecyclerView)postView2.findViewById(R.id.recycler_view_lentoutdetails);
        noNotesView = postView2.findViewById(R.id.empty_notes_view_lent_outdetails);

        mAdapter =  new LentOutAdapter(getContext(),notesList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(mAdapter);
        toggleEmptyNotes();


        db = new LentOutDatabaseHelper(getContext());
        if(notesList!=null && notesList.size()>0){
            notesList.clear();
        }
        notesList.addAll(db.getAllLentOuts());

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
