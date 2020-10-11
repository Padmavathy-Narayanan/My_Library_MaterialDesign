package com.padmavathy.mylibrary.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.padmavathy.mylibrary.R;
import com.padmavathy.mylibrary.fragments.FragmenrHistoryBorrowedBook;
import com.padmavathy.mylibrary.fragments.FragmentBookList;
import com.padmavathy.mylibrary.fragments.FragmentBorrowedBook;
import com.padmavathy.mylibrary.fragments.FragmentOnlineBookSearch;

//import net.skoumal.fragmentback.BackFragmentHelper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class BottomNavActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private FragmentBookList fragment1;
    private FragmentOnlineBookSearch fragment2;
    private FragmentBorrowedBook fragment3;
    private FragmenrHistoryBorrowedBook fragment4;
    private BottomNavigationView mBottom;
    TextView tv;
    String task;
    boolean navigationOnlineSearch = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_nav);

        String currentDateTimeString = java.text.DateFormat.getDateTimeInstance().format(new Date());
        Log.d("Current DateTime",currentDateTimeString);
        DateFormat df = new SimpleDateFormat("dd MMM yyyy hh:mm:ss aa");
        try {
            Date date = df.parse(currentDateTimeString);
            SimpleDateFormat print = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss aa");
            System.out.println(print.format(date));

        } catch (ParseException e) {
            e.printStackTrace();
        }

        toolbar = findViewById(R.id.bottom_tool1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My Library");
        toolbar.setTitleTextAppearance(this, R.style.RobotoBoldTextAppearance);
        mBottom = findViewById(R.id.basic_bottom);
        tv = (TextView)findViewById(R.id.tv_view_toolbar);

        fragment1 =  new FragmentBookList();
        fragment2 = new FragmentOnlineBookSearch();
        fragment3 = new FragmentBorrowedBook();
        fragment4 =  new FragmenrHistoryBorrowedBook();

        //default fragment that should be visible on open
        handleFragments1(fragment1);
        SharedPreferences prefs = getSharedPreferences("MY_PREFS_NAME", MODE_PRIVATE);
        String name = prefs.getString("name", "My Library");//"No name defined" is the default value.
        if(!name.trim().isEmpty()){
            tv.setText(name);
        }
        else {
            tv.setText("My Library");
        }

        String sessionId = getIntent().getStringExtra("Fragment");
        if(sessionId == null){
            handleFragments1(fragment1);
        }
        else{
            if (sessionId.equals("Online_Book")) {
                sessionId = "";
                handleFragments(fragment2);
                navigationOnlineSearch = true;
            }
        }

        //pass fragments that should be visible in following switch case
        mBottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.basic_home:
                        handleFragments1(fragment1);
                        break;

                    case R.id.basic_onlinebook:
                   /*     Bundle bundle = new Bundle();
                        bundle.putString("BottomNavActivity","true");
                        // set Fragmentclass Arguments
                        fragment2.setArguments(bundle);*/
                        handleFragments1(fragment2);

                        break;

                    case R.id.basic_borrowedbook:
                        handleFragments1(fragment3);
                        break;

                    case R.id.basic_historyborrow:
                        handleFragments1(fragment4);
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
            /*case R.id.action_add:
                //Toast.makeText(BottomNavActivity.this,"IMPORT",Toast.LENGTH_LONG).show();
                return false;*/
            case R.id.change_title:
                final EditText taskEditText = new EditText(BottomNavActivity.this);
                AlertDialog dialog = new AlertDialog.Builder(BottomNavActivity.this)
                        .setTitle("Change Title")
                        .setView(taskEditText)
                        .setPositiveButton("Change", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                task  = String.valueOf(taskEditText.getText());
                                // MY_PREFS_NAME - a static String variable like:
                                //public static final String MY_PREFS_NAME = "MyPrefsFile";
                                SharedPreferences.Editor editor = getSharedPreferences("MY_PREFS_NAME", MODE_PRIVATE).edit();
                                editor.putString("name", task);
                                editor.apply();
                                tv.setText(task);
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();
                return true;
            default:
                break;
        }
        return false;
    }

    private void handleFragments(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame1, fragment);
        fragmentTransaction.commit();
       /* finish();
        startActivity(getIntent());*/
    }

    private void handleFragments1(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame1, fragment);
        fragmentTransaction.commit();
       /* finish();
        startActivity(getIntent());*/
    }



  /*  @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
        }
    }*/
  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
      if (keyCode == KeyEvent.KEYCODE_BACK) {
          //handleFragments(fragment1);
          AlertDialog.Builder builder = new AlertDialog.Builder(this);
          builder.setMessage("Are you sure you want to exit?")
                  .setCancelable(false)
                  .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                      public void onClick(DialogInterface dialog, int id) {
                          BottomNavActivity.this.finish();
                      }
                  })
                  .setNegativeButton("No", new DialogInterface.OnClickListener() {
                      public void onClick(DialogInterface dialog, int id) {
                          dialog.cancel();
                      }
                  });
          AlertDialog alert = builder.create();
          alert.show();
            //Toast.makeText(this,"exit",Toast.LENGTH_LONG).show();
      }
      return super.onKeyDown(keyCode, event);
  }


}