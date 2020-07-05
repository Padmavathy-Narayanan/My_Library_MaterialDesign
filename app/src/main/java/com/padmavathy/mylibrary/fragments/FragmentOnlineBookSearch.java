package com.padmavathy.mylibrary.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.padmavathy.mylibrary.R;

public class FragmentOnlineBookSearch extends Fragment {
    RelativeLayout rel_no_internet,relOnlineLayout;
    private Button refreshBtn;
    private Button onLineSearch;

    public FragmentOnlineBookSearch() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View postView1 = inflater.inflate(R.layout.fragment_online_book_search, container, false);
        rel_no_internet = postView1.findViewById(R.id.interenet);
        refreshBtn = postView1.findViewById(R.id.refresh);
        relOnlineLayout = postView1.findViewById(R.id.onlineBookSearchRelativeLayout);
        onLineSearch = postView1.findViewById(R.id.online_buttonSearch);

        if(isNetworkAvailable()){
            rel_no_internet.setVisibility(View.GONE);
            relOnlineLayout.setVisibility(View.VISIBLE);
        }
        else {
            rel_no_internet.setVisibility(View.VISIBLE);
            relOnlineLayout.setVisibility(View.GONE);
            refreshBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isNetworkAvailable()){
                        relOnlineLayout.setVisibility(View.VISIBLE);
                        rel_no_internet.setVisibility(View.GONE);
                    }
                    else {
                        relOnlineLayout.setVisibility(View.GONE);
                        rel_no_internet.setVisibility(View.VISIBLE);
                    }
                }
            });
        }

        onLineSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"Searching...",Toast.LENGTH_LONG).show();
            }
        });
        return postView1;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
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
