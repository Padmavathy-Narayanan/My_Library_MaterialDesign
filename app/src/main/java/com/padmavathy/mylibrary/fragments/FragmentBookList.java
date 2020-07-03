package com.padmavathy.mylibrary.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.padmavathy.mylibrary.R;
import com.padmavathy.mylibrary.adapter.BookListAdapter;
import com.padmavathy.mylibrary.database.DatabaseHelper;
import com.padmavathy.mylibrary.model.Book;
import com.padmavathy.mylibrary.utils.MyDividerItemDecoration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class FragmentBookList extends Fragment {
    AlertDialog.Builder builder_1;
    AlertDialog alertDialog_1;
    private static final int PERMISSION_REQUEST_STORAGE = 1000;
    private static final int  READ_REQUEST_CODE = 1000;
    StringBuilder text;
    static final int READ_BLOCK_SIZE = 1000;

    private static final int PERMISSION_REQUEST_CODE = 200;
    boolean request_permission_granted = false;
    private RecyclerView recyclerView;
    private BookListAdapter mAdapter;
    private List<Book> notesList = new ArrayList<>();
    private DatabaseHelper db;
    Book n;

    public FragmentBookList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View postView1 = inflater.inflate(R.layout.fragment_book_list, container, false);

        db = new DatabaseHelper(getActivity());

        notesList.addAll(db.getAllNotes());

        // Button refresh1 = postView1.findViewById(R.id.refresh1);
        builder_1 = new AlertDialog.Builder(getActivity(),R.style.DialogTheme);
        recyclerView = postView1.findViewById(R.id.recycler_view);
        mAdapter = new BookListAdapter(getContext(), notesList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(mAdapter);
        //toggleEmptyNotes();

        return postView1;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_add:
                ViewGroup viewGroup = getView().findViewById(android.R.id.content);
                LayoutInflater layoutInflater = getLayoutInflater();
                //View dialogView_1 = layoutInflater.inflate(R.layout.loading,null);
                View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.loading, viewGroup, false);
                builder_1.setView(dialogView);
                alertDialog_1 = builder_1.create();

                // Change the alert dialog background color
                //alertDialog_1.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.roundd_rectangle));
                //alertDialog_1.show();

                if(Build.VERSION.SDK_INT>Build.VERSION_CODES.M && ActivityCompat.checkSelfPermission(getContext(),Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},PERMISSION_REQUEST_STORAGE);
                }
                if (checkPermission()) {
                    alertDialog_1.show();
                    alertDialog_1.setCancelable(false);
                    PerformFileSearch();
                    //Toast.makeText(MainActivity.this,"Permission already granted.",Toast.LENGTH_LONG).show();

                } else {

                    requestPermission();
                    if(request_permission_granted == true){
                        alertDialog_1.show();
                        alertDialog_1.setCancelable(false);
                        //alertDialog_1.setCancelable(false);
                        PerformFileSearch();
                    }

                    //Toast.makeText(MainActivity.this, "Please request permission.",Toast.LENGTH_LONG).show();
                }
                return false;
            case R.id.action_settings:

                // Do Fragment menu item stuff here
                return true;

            default:
                break;
        }

        return false;
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getActivity(), READ_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getActivity(), WRITE_EXTERNAL_STORAGE);

        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (locationAccepted && cameraAccepted){
                        Toast.makeText(getActivity(), "Permission Granted, Now you can access storage data", Toast.LENGTH_LONG).show();
                        request_permission_granted = true;
                        alertDialog_1.show();
                        alertDialog_1.setCancelable(false);
                        PerformFileSearch();
                    }
                    else {

                        Toast.makeText(getActivity(),  "Permission Denied, You cannot access storage data", Toast.LENGTH_LONG).show();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(READ_EXTERNAL_STORAGE)) {
                                showMessageOKCancel("You need to allow access to the permission",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE},
                                                            PERMISSION_REQUEST_CODE);

                                                }
                                            }
                                        });
                                return;
                            }
                        }

                    }
                }


                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(getActivity())
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    //select file from storage
    private void PerformFileSearch(){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        //Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/*");
        startActivityForResult(intent,READ_BLOCK_SIZE);
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {

            if (data != null) {
                Uri uri = data.getData();
                String path = uri.getPath();
                path = path.substring(path.indexOf(":") + 1);
                File dir = Environment.getExternalStorageDirectory();
                String filepath = dir.getAbsolutePath() + File.separatorChar + path;
                //String filename = dir.getAbsolutePath() + File.separatorChar + path;

                Toast.makeText(getContext(), path, Toast.LENGTH_LONG).show();
                // You can provide the dir location from SD card root
                String data1 = readData(path);
                String lines[] = data1.split("\n\n");

                for (String line : lines) {
                    System.out.println("line " + " : " + line);
                    String[] employee = line.split(",");    // use comma as separator
                    System.out.println("Book [Location=" + employee[0] + ", Book=" + employee[1] + ", Name=" + employee[2] + ", ISBN=" + employee[3] + ", Condiiton= " + employee[4] + ", Marking= " + employee[5] + ", Binding= " + employee[6] + ", Lent Price= " + employee[7] + ", Book Price= " + employee[8] + ", Paid Price= " + employee[9] + ", Quantity= " + employee[10] + "]"
                    );

                    String line_1 = "Book [Location=" + employee[0] + ", Book=" + employee[1] + ", Name=" + employee[2] + ", ISBN=" + employee[3] + ", Condiiton= " + employee[4] + ", Marking= " + employee[5] + ", Binding= " + employee[6] + ", Lent Price= " + employee[7] + ", Book Price= " + employee[8] + ", Paid Price= " + employee[9] + ", Quantity= " + employee[10] + "]";
                    Log.d("Book Details: ",line_1+"\n");
                    long id = db.insertNote(employee[1],employee[2],employee[3],employee[4],employee[5],employee[6],employee[0],employee[7],employee[8],employee[9],employee[10],"");

                    n = db.getNote(employee[3]);

                    if (n != null) {
                        // adding new note to array list at 0 position
                        notesList.add(0, n);

                        // refreshing the list
                        mAdapter.notifyDataSetChanged();
                        //pd.dismiss();
                        //toggleEmptyNotes();

                    }

                    }

                }
            }
            alertDialog_1.setCancelable(true);
            alertDialog_1.dismiss();
            super.onActivityResult(requestCode, resultCode, data);
        }

    private String readData(String path) {
        // Get the dir of SD Card
        File sdCardDir = Environment.getExternalStorageDirectory();

        // Get The Text file
        File txtFile = new File(sdCardDir, path);

        // Read the file Contents in a StringBuilder Object
        StringBuilder text = new StringBuilder();
        String strArray[] = new String[]{};
        try {

            BufferedReader reader = new BufferedReader(new FileReader(txtFile));

            String line;

            while ((line = reader.readLine()) != null) {
                text.append(line + "\n\n");

            }
            reader.close();
        } catch (IOException e) {
            Log.e("C2c", "Error occured while reading text file!!");

        }

        return text.toString();
    }
}
