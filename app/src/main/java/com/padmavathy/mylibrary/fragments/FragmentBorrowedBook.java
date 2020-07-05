package com.padmavathy.mylibrary.fragments;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.padmavathy.mylibrary.R;
import com.padmavathy.mylibrary.activity.BottomNavActivity;
import com.padmavathy.mylibrary.adapter.BorrowAdapter;
import com.padmavathy.mylibrary.adapter.HistoryAdapter;
import com.padmavathy.mylibrary.adapter.SectionPagerAdapterRoot;
import com.padmavathy.mylibrary.database.BorrowDatabaseHelper;
import com.padmavathy.mylibrary.database.HistoryDatabaseHelper;
import com.padmavathy.mylibrary.model.Book;
import com.padmavathy.mylibrary.model.BorrowBook;
import com.padmavathy.mylibrary.model.History_Borrow;
import com.padmavathy.mylibrary.utils.MyDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class FragmentBorrowedBook extends Fragment {
    Toolbar toolbar_borrow;
    private BottomNavActivity mainActivity;
  /*  private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private SectionPagerAdapterRoot mSectionsPagerAdapter;*/

    RecyclerView recyclerView;
    private List<BorrowBook> notesList = new ArrayList<BorrowBook>();
    private List<History_Borrow> historyBorrowList = new ArrayList<History_Borrow>();
    private BorrowDatabaseHelper db;
    private HistoryDatabaseHelper db1;
    private BorrowAdapter mAdapter;
    private HistoryAdapter historyAdapter;

    private FragmentBorrowTab mFragmentOne;
    private FragmenrHistoryBorrowedBook mFragmentTwo;

    private FloatingActionButton fab;
    private FragmentTabAddBorrowBook fragmentAddBook;
    private FragmenrHistoryBorrowedBook fragmenrHistoryBorrowedBook;
    private TextView noNotesView;

    public FragmentBorrowedBook() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mainActivity = (BottomNavActivity)activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View postView1 = inflater.inflate(R.layout.fragment_borrowed_book, container, false);
        toolbar_borrow = getActivity().findViewById(R.id.toolbar5);
        noNotesView = postView1.findViewById(R.id.empty_notes_view_1);

   /*     mTabLayout = postView1.findViewById(R.id.tabs5);
        mViewPager = postView1.findViewById(R.id.container5);*/
        fab = postView1.findViewById(R.id.fab_borrow_add);
        recyclerView = (RecyclerView)postView1.findViewById(R.id.recycler_view_2);

        fragmentAddBook = new FragmentTabAddBorrowBook();
        //fragmenrHistoryBorrowedBook = new FragmenrHistoryBorrowedBook();

        //mFragmentOne = new FragmentBorrowTab();
        //mFragmentTwo = new FragmenrHistoryBorrowedBook();

      /*  mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        mSectionsPagerAdapter = new SectionPagerAdapterRoot(getFragmentManager(), mFragmentOne, mFragmentTwo,2);
        mViewPager.setAdapter(mSectionsPagerAdapter);*/

        recyclerView.setVisibility(View.VISIBLE);
        mAdapter = new BorrowAdapter(getContext(), notesList);
        historyAdapter =  new HistoryAdapter(getContext(),historyBorrowList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(mAdapter);
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

        fab = postView1.findViewById(R.id.fab_borrow_add);
        fragmentAddBook = new FragmentTabAddBorrowBook();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleFragments(fragmentAddBook);
                //Toast.makeText(getContext(), "Replace with your own action", Toast.LENGTH_LONG).show();
            }
        });

        db = new BorrowDatabaseHelper(getContext());
        if(notesList!=null && notesList.size()>0){
            notesList.clear();
        }
        notesList.addAll(db.getAllBorrows());
        for (BorrowBook element : notesList) {
            Log.d("BORROW",element.getTitle());
        }

        return postView1;
    }
    private void handleFragments(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame1, fragment);
        fragmentTransaction.commit();
    }
    private void toggleEmptyNotes() {
        // you can check notesList.size() > 0
        BorrowDatabaseHelper db1 =  new BorrowDatabaseHelper(getContext());
        if (db1.getBorrowsCount() > 0) {
            noNotesView.setText("No Borrowed Books");
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
