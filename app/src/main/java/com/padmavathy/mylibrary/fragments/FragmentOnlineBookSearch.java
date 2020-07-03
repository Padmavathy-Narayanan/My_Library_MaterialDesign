package com.padmavathy.mylibrary.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.padmavathy.mylibrary.R;

public class FragmentOnlineBookSearch extends Fragment {
    public FragmentOnlineBookSearch() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View postView1 = inflater.inflate(R.layout.fragment_online_book_search, container, false);
        //Button refresh1 = postView1.findViewById(R.id.refresh1);
        return postView1;
    }
}
