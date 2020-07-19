package com.padmavathy.mylibrary.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.padmavathy.mylibrary.R;
import com.padmavathy.mylibrary.database.DatabaseHelper;
import com.padmavathy.mylibrary.database.LentInDatabaseHelper;
import com.padmavathy.mylibrary.model.Book;
import com.padmavathy.mylibrary.model.LentIn;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class FragmentAddLentIn extends Fragment {
    Toolbar toolbar;
    EditText et_personName,et_Date;
    ImageView imgBackPress;
    Button btnSave;
    Calendar myCalendar;
    String _path = "";
    LentInDatabaseHelper lentInDatabaseHelper;
    LentIn lentIn;
    String value;
    Book book;
    DatabaseHelper db;
    RequestQueue requestQueue;
    FragmentLentView NAME;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View postView1 = inflater.inflate(R.layout.fragment_book_add_lent_view, container, false);
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar_add_lent_in);

        et_personName = postView1.findViewById(R.id.etName);
        et_Date = postView1.findViewById(R.id.lentDate);

        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

        et_Date.setText(currentDate);
        myCalendar = Calendar.getInstance();

        btnSave = postView1.findViewById(R.id.saveLentIn);

        // RequestQueue For Handle Network Request
        requestQueue = Volley.newRequestQueue(getContext());

        lentInDatabaseHelper = new LentInDatabaseHelper(getContext());
        lentIn = new LentIn();

        db = new DatabaseHelper((getContext()));
        book = new Book();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        et_Date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        value = getArguments().getString("KEY");

        NAME = new FragmentLentView();

        /*book = db.getNote(Long.parseLong(value));
        Log.d("KEY",book.getAuthor());

        final String isbn;

        isbn = book.getIsbn();
*/
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String personName = et_personName.getText().toString();
                String dateLent = et_Date.getText().toString();

                if (!TextUtils.isEmpty(personName) && !TextUtils.isEmpty(dateLent)){
                    ConnectivityManager connMgr = (ConnectivityManager) getActivity()
                            .getSystemService(Context.CONNECTIVITY_SERVICE);

                    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

                    if (networkInfo != null && networkInfo.isConnected()) {
                        // fetch data
                        postData(value,personName,dateLent);
                    } else {
                        // display error
                        Toast.makeText(getContext(),"No internet Connection,Please try again!",Toast.LENGTH_LONG).show();
                    }

                }
                else {
                    Toast.makeText(getContext(),"Please enter details",Toast.LENGTH_LONG).show();
                }

            }
        });
        return postView1;
    }


    // Post Request For JSONObject
    public void postData(final String isbn, final String Name, String lentDate ) {
        Date date1= null;
        try {
            date1 = new SimpleDateFormat("dd-MM-yyyy").parse(lentDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
        final String dateValue= targetFormat.format(date1);

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JSONObject object = new JSONObject();
        try {
            //input your API parameters
            object.put("ISBN",isbn);
            object.put("LentTo",Name);
            object.put("LentDate",dateValue);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Enter the correct url for your api service site
        String url = "http://104.37.186.201/MyLibraryService/MyLibService.svc/UpdateBooksLentToStatus";
        String updated_url = url+"?"+"ISBN="+isbn+"&LentTo="+Name+"&LentDate="+dateValue;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, updated_url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        lentInDatabaseHelper.insertLentIn(isbn,Name,dateValue,"Lent_In");
                        new AlertDialog.Builder(getContext())
                                .setMessage("Information Saved Successfully...")
                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                // The dialog is automatically dismissed when a dialog button is clicked.
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Continue with delete operation
                                        Bundle args_1 = new Bundle();
                                        args_1.putString("ValueKey", value);
                                        NAME.setArguments(args_1);
                                        handleFragments(NAME);
                                        dialog.dismiss();
                                    }
                                })
                                // A null listener allows the button to dismiss the dialog and take no further action.
                                .setNegativeButton(android.R.string.no, null)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                        //Toast.makeText(getContext(),"Saved Successfully!",Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"Error getting response",Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
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

    private void updateLabel() {
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        et_Date.setText(sdf.format(myCalendar.getTime()));
    }

}
