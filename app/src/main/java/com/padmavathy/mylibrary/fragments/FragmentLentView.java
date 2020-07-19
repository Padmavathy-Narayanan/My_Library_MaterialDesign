package com.padmavathy.mylibrary.fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.padmavathy.mylibrary.R;
import com.padmavathy.mylibrary.adapter.BookListAdapter;
import com.padmavathy.mylibrary.adapter.LentInAdapter;
import com.padmavathy.mylibrary.database.DatabaseHelper;
import com.padmavathy.mylibrary.database.LentInDatabaseHelper;
import com.padmavathy.mylibrary.model.Book;
import com.padmavathy.mylibrary.model.LentIn;
import com.padmavathy.mylibrary.utils.MyDividerItemDecoration;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FragmentLentView extends Fragment {
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    FragmentAddLentIn fragmentAddLentIn;
    FragmentAddLentOut fragmentAddLentOut;
    String value;
    Book book;
    DatabaseHelper db;

    private RecyclerView recyclerView;
    private LentInAdapter mAdapter;
    private List<LentIn> notesList = new ArrayList<>();
    private LentInDatabaseHelper lentInDatabaseHelper;
    Book n;
    TextView noNotesView;
    ImageView img_back;
    Toolbar toolbar;

    public FragmentLentView(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View postView1 = inflater.inflate(R.layout.fragment_a, container, false);
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbarLentInView);

       /* toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                FragmentBookList NAME = new FragmentBookList();
                fragmentTransaction.replace(R.id.frame1, NAME);
                fragmentTransaction.commit();
            }
        });*/
        img_back = (ImageView)postView1.findViewById(R.id.img_back_lent_in);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                FragmentBookList NAME = new FragmentBookList();
                fragmentTransaction.replace(R.id.frame1, NAME);
                fragmentTransaction.commit();
            }
        });

        fragmentAddLentIn = new FragmentAddLentIn();
        fragmentAddLentOut = new FragmentAddLentOut();

        value = getArguments().getString("ValueKey");

        //book = db.getNote(Long.parseLong(value));
        System.out.println("LENT_KEY"+value);

        final String isbn;

        //isbn = book.getIsbn();

//        Log.d("LENT_ISBN",isbn);

        FloatingActionButton fab = (FloatingActionButton) postView1.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                ViewGroup viewGroup = view.findViewById(android.R.id.content);
                final View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.customview_dialog, viewGroup, false);

                Button btn_lent = dialogView.findViewById(R.id.lent_in);
                Button btn_lent_out = dialogView.findViewById(R.id.lent_out);

                builder.setView(dialogView);
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();

                btn_lent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle args_1 = new Bundle();
                        args_1.putString("KEY", value);

                        if(value.isEmpty()){
                            Toast.makeText(getContext(),"ISBN Value is empty",Toast.LENGTH_LONG).show();
                        }
                        else {
                            ConnectivityManager connMgr = (ConnectivityManager) getActivity()
                                    .getSystemService(Context.CONNECTIVITY_SERVICE);

                            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

                            if (networkInfo != null && networkInfo.isConnected()) {
                                // fetch data
                                fragmentAddLentIn.setArguments(args_1);
                                handleFragments(fragmentAddLentIn);
                            } else {
                                // display error
                                Toast.makeText(getContext(),"No internet Connection,Please try again!",Toast.LENGTH_LONG).show();
                            }




                        }
                        alertDialog.dismiss();
                        //Snackbar.make(v,"LENT IN", BaseTransientBottomBar.LENGTH_LONG).show();
                    }
                });

                btn_lent_out.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle args_1 = new Bundle();
                        args_1.putString("Key", value);
                        if(value.isEmpty()){
                            Toast.makeText(getContext(),"ISBN Value is empty",Toast.LENGTH_LONG).show();
                        }
                        else {
                            ConnectivityManager connMgr = (ConnectivityManager) getActivity()
                                    .getSystemService(Context.CONNECTIVITY_SERVICE);

                            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

                            if (networkInfo != null && networkInfo.isConnected()) {
                                // fetch data
                                fragmentAddLentOut.setArguments(args_1);
                                handleFragments(fragmentAddLentOut);
                            } else {
                                // display error
                                Toast.makeText(getContext(),"No internet Connection,Please try again!",Toast.LENGTH_LONG).show();
                            }
                        }

                        alertDialog.dismiss();
                    }
                });

            }
        });

        /*viewPager = (ViewPager) postView1.findViewById(R.id.viewPager);
        viewPagerAdapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout =  postView1.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);*/

        recyclerView = (RecyclerView)postView1.findViewById(R.id.recycler_view_lentdetails);
        noNotesView = postView1.findViewById(R.id.empty_notes_view_lent_details);
        
        LentIn lentIn = new LentIn();

        String lent_isbn = lentIn.getIsbn();
        
        int id_val = 0;

        //Log.d("FRAG",value);

        if(value.isEmpty()){
            noNotesView.setText("No records");
            noNotesView.setVisibility(View.VISIBLE);
            //toggleEmptyNotes();
        }
        else {
            noNotesView.setVisibility(View.GONE);
            mAdapter =  new LentInAdapter(getContext(),notesList);

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.addItemDecoration(new MyDividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL, 16));
            recyclerView.setAdapter(mAdapter);
            toggleEmptyNotes();


            lentInDatabaseHelper = new LentInDatabaseHelper(getContext());
            if(notesList!=null && notesList.size()>0){
                notesList.clear();
            }

            notesList.addAll(lentInDatabaseHelper.getAllISBNLentIns(value));
            //notesList.addAll(lentInDatabaseHelper.getAllLentIns());
        }
        //setHasOptionsMenu(true);
        return postView1;
    }

    /*Enable options menu in this fragment*/
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //inflate menu
        inflater.inflate(R.menu.menu_main_2, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //handle menu item clicks
        int id = item.getItemId();

        if (id == R.id.action_lentin) {
            //do your function here
            //Toast.makeText(getActivity(), "Lemt In", Toast.LENGTH_SHORT).show();
        }
        if (id == R.id.action_lentout) {
            //do your function here
            //Toast.makeText(getActivity(), "Lent Out", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
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

    private void toggleEmptyNotes() {
        // you can check notesList.size() > 0
        LentInDatabaseHelper db2 =  new LentInDatabaseHelper((getContext()));
        if (db2.getLentInsCount() > 0) {
            noNotesView.setVisibility(View.GONE);
        } else {
            noNotesView.setText("No records");
            noNotesView.setVisibility(View.VISIBLE);
        }
    }
}
