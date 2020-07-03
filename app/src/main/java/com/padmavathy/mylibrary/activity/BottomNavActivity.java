package com.padmavathy.mylibrary.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.padmavathy.mylibrary.R;
import com.padmavathy.mylibrary.fragments.FragmenrHistoryBorrowedBook;
import com.padmavathy.mylibrary.fragments.FragmentBookList;
import com.padmavathy.mylibrary.fragments.FragmentBorrowedBook;
import com.padmavathy.mylibrary.fragments.FragmentOnlineBookSearch;

public class BottomNavActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private FragmentBookList fragment1;
    private FragmentOnlineBookSearch fragment2;
    private FragmentBorrowedBook fragment3;
    private FragmenrHistoryBorrowedBook fragment4;
    private BottomNavigationView mBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_nav);

        toolbar = findViewById(R.id.bottom_tool1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My Library");
        toolbar.setTitleTextAppearance(this, R.style.RobotoBoldTextAppearance);
        mBottom = findViewById(R.id.basic_bottom);

        fragment1 =  new FragmentBookList();
        fragment2 = new FragmentOnlineBookSearch();
        fragment3 = new FragmentBorrowedBook();
        fragment4 =  new FragmenrHistoryBorrowedBook();

        //default fragment that should be visible on open
        handleFragments(fragment1);

        //pass fragments that should be visible in following switch case
        mBottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.basic_home:
                        handleFragments(fragment1);
                        break;

                    case R.id.basic_onlinebook:
                        handleFragments(fragment2);
                        break;

                    case R.id.basic_borrowedbook:
                        handleFragments(fragment3);
                        break;

                    case R.id.basic_historyborrow:
                        handleFragments(fragment4);
                        break;
                }
                return true;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bottom_nav_toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_add:
                Toast.makeText(BottomNavActivity.this,"IMPORT",Toast.LENGTH_LONG).show();
                return false;
            case R.id.action_settings:
                //startSettings();
                Toast.makeText(BottomNavActivity.this,"EXPORT",Toast.LENGTH_LONG).show();
                return false;
            default:
                break;
        }
        return false;
    }

    private void handleFragments(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame1, fragment);
        fragmentTransaction.commit();
    }


}