package com.padmavathy.mylibrary.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.padmavathy.mylibrary.R;
import com.padmavathy.mylibrary.activity.BottomNavActivity;
import com.padmavathy.mylibrary.adapter.HistoryAdapter;
import com.padmavathy.mylibrary.adapter.WishlistAdapter;
import com.padmavathy.mylibrary.database.HistoryDatabaseHelper;
import com.padmavathy.mylibrary.database.WishlistDatabaseHelper;
import com.padmavathy.mylibrary.model.History_Borrow;
import com.padmavathy.mylibrary.model.Wishlist;
import com.padmavathy.mylibrary.utils.MyDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class FragmentWishlist extends Fragment {
    Toolbar toolbar_borrow;
    private BottomNavActivity mainActivity;
    TextView history;
    RecyclerView recyclerView;
    private List<Wishlist> historyBorrowList = new ArrayList<Wishlist>();
    private WishlistDatabaseHelper db1;
    private WishlistAdapter wishlistAdapter;
    private TextView noNotesView;
    ImageView img;

    public FragmentWishlist(){

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        View postView1 = inflater.inflate(R.layout.fragment_wish_list, container, false);
        toolbar_borrow = getActivity().findViewById(R.id.toolbarwishlist);
        history = postView1.findViewById(R.id.tv_wishlist);
        img = postView1.findViewById(R.id.img_backOnlineWishlist);
        history.setText("Wishlist");

   /*     mTabLayout = postView1.findViewById(R.id.tabs5);
        mViewPager = postView1.findViewById(R.id.container5);*/

        recyclerView = (RecyclerView)postView1.findViewById(R.id.recycler_view_3);
        noNotesView = postView1.findViewById(R.id.empty_notes_view_wishlist);


      /*  mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        mSectionsPagerAdapter = new SectionPagerAdapterRoot(getFragmentManager(), mFragmentOne, mFragmentTwo,2);
        mViewPager.setAdapter(mSectionsPagerAdapter);*/

      img.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              FragmentManager fragmentManager = getFragmentManager();
              FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
              FragmentBookList NAME = new FragmentBookList();
              fragmentTransaction.replace(R.id.frame1, NAME);
              fragmentTransaction.commit();
          }
      });

        wishlistAdapter =  new WishlistAdapter(getContext(),historyBorrowList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(wishlistAdapter);
        toggleEmptyNotes();


            db1 = new WishlistDatabaseHelper(getContext());
        if(historyBorrowList!=null && historyBorrowList.size()>0){
            historyBorrowList.clear();
        }
        historyBorrowList.addAll(db1.getAllWishlists());

        return postView1;
    }
    private void handleFragments(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame1, fragment);
        fragmentTransaction.commit();
    }
    private void toggleEmptyNotes() {
        // you can check notesList.size() > 0
        WishlistDatabaseHelper db2 =  new WishlistDatabaseHelper((getContext()));
        if (db2.getWishlistsCount() > 0) {
            noNotesView.setText("Wishlist is Empty");
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
