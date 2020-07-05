package com.padmavathy.mylibrary.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.padmavathy.mylibrary.R;

public class FragmentBorrowTab extends Fragment {
    private FloatingActionButton fab;
    private FragmentTabAddBorrowBook fragmentAddBook;

    public FragmentBorrowTab(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View postView1 = inflater.inflate(R.layout.fragment_borrowed_tab, container, false);
      //  fab = postView1.findViewById(R.id.fab_borrow_add);
        fragmentAddBook = new FragmentTabAddBorrowBook();

       /* fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleFragments(fragmentAddBook);
                //Toast.makeText(getContext(), "Replace with your own action", Toast.LENGTH_LONG).show();
            }
        });*/

        return postView1;
    }
    private void handleFragments(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame1, fragment);
        fragmentTransaction.commit();
    }
    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }
    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }
}
