package com.padmavathy.mylibrary.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.padmavathy.mylibrary.R;
import com.padmavathy.mylibrary.activity.BottomNavActivity;
import com.padmavathy.mylibrary.adapter.BorrowAdapter;
import com.padmavathy.mylibrary.adapter.HistoryAdapter;
import com.padmavathy.mylibrary.database.BorrowDatabaseHelper;
import com.padmavathy.mylibrary.database.HistoryDatabaseHelper;
import com.padmavathy.mylibrary.model.BorrowBook;
import com.padmavathy.mylibrary.model.History_Borrow;
import com.padmavathy.mylibrary.utils.MyDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class FragmenrHistoryBorrowedBook extends Fragment {
    Toolbar toolbar_borrow;
    private BottomNavActivity mainActivity;
    TextView history;
    RecyclerView recyclerView;
    private List<History_Borrow> historyBorrowList = new ArrayList<History_Borrow>();
    private HistoryDatabaseHelper db1;
    private HistoryAdapter historyAdapter;
    private TextView noNotesView;


    public FragmenrHistoryBorrowedBook() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        View postView1 = inflater.inflate(R.layout.fragment_history_borrowed_book, container, false);
        toolbar_borrow = getActivity().findViewById(R.id.toolbar5);
        history = postView1.findViewById(R.id.tv_history);
        history.setText("History");
   /*     mTabLayout = postView1.findViewById(R.id.tabs5);
        mViewPager = postView1.findViewById(R.id.container5);*/

        recyclerView = (RecyclerView)postView1.findViewById(R.id.recycler_view_3);
        noNotesView = postView1.findViewById(R.id.empty_notes_view_2);


      /*  mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        mSectionsPagerAdapter = new SectionPagerAdapterRoot(getFragmentManager(), mFragmentOne, mFragmentTwo,2);
        mViewPager.setAdapter(mSectionsPagerAdapter);*/

        historyAdapter =  new HistoryAdapter(getContext(),historyBorrowList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(historyAdapter);
        toggleEmptyNotes();
       /* mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        recyclerView.setAdapter(mAdapter);
                        fab.show();
                        break;
                    case 1:
                        recyclerView.setAdapter(historyAdapter);
                        fab.hide();
                        //recyclerView.setVisibility(View.GONE);
                        break;
                    default:
                        recyclerView.setAdapter(mAdapter);
                        fab.show();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });*/


        db1 = new HistoryDatabaseHelper(getContext());
        if(historyBorrowList!=null && historyBorrowList.size()>0){
            historyBorrowList.clear();
        }
        historyBorrowList.addAll(db1.getAllHistory_Borrows());

        return postView1;
    }
    private void handleFragments(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame1, fragment);
        fragmentTransaction.commit();
    }
    private void toggleEmptyNotes() {
        // you can check notesList.size() > 0
        HistoryDatabaseHelper db2 =  new HistoryDatabaseHelper((getContext()));
        if (db2.getHistory_BorrowsCount() > 0) {
            noNotesView.setText("No History");
            noNotesView.setVisibility(View.GONE);
        } else {
            noNotesView.setVisibility(View.VISIBLE);
        }
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
