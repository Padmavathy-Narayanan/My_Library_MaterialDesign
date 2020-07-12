package com.padmavathy.mylibrary.fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

import com.padmavathy.mylibrary.R;
import com.padmavathy.mylibrary.activity.BottomNavActivity;
import com.padmavathy.mylibrary.database.DatabaseHelper;
import com.padmavathy.mylibrary.model.Book;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.File;

public class FragmentViewBook extends Fragment {
    private BottomNavActivity mainActivity;
    private Toolbar toolbar;
    TextView tv_bookname,tv_author,tv_isbn,tv_marking,tv_condition,tv_location,tv_binding,tv_lentto,tv_bookprice,tv_pricepaid,tv_quantity,toolbar_tit;
    ImageView img_book,img_backpress,editDta,deleteData;
    String value;
    AlertDialog.Builder builder;
    FragmentEditBook fragmentEditBook;
    Book book;
    DatabaseHelper db;

    public FragmentViewBook(){

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mainActivity = (BottomNavActivity)activity;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View postView2 = inflater.inflate(R.layout.fragment_view_book, container, false);
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar_fragment);

        db = new DatabaseHelper(getContext());
        book = new Book();
        builder = new AlertDialog.Builder(getContext());
        tv_bookname = (TextView)postView2.findViewById(R.id.timestamp_book);
        tv_author = (TextView)postView2.findViewById(R.id.authorname);
        tv_isbn = (TextView)postView2.findViewById(R.id.isbn_no);
        tv_marking = (TextView)postView2.findViewById(R.id.marking_det);
        tv_binding = (TextView)postView2.findViewById(R.id.binding_det);
        tv_location = (TextView)postView2.findViewById(R.id.location_det);
        tv_condition = (TextView)postView2.findViewById(R.id.condition_det);
        tv_lentto = (TextView)postView2.findViewById(R.id.lentto_no);
        tv_bookprice = (TextView)postView2.findViewById(R.id.book_price_det);
        tv_pricepaid = (TextView)postView2.findViewById(R.id.paidprice_det);
        tv_quantity = (TextView)postView2.findViewById(R.id.quantity_det);
        img_book = (ImageView) postView2.findViewById(R.id.dot_book);

        img_backpress = (ImageView)postView2.findViewById(R.id.img_back);
        editDta = (ImageView)postView2.findViewById(R.id.img_editdata);
        deleteData = (ImageView)postView2.findViewById(R.id.deleteData);

        toolbar_tit = (TextView)postView2.findViewById(R.id.toolbar_title_view_isbn);
        fragmentEditBook =  new FragmentEditBook();

        //setupToolbar();

        value = getArguments().getString("Key");



        book = db.getNote(Long.parseLong(value));
        Log.d("KEY",book.getAuthor());
        toolbar_tit.setText("ISBN: "+book.getIsbn());

        final String bookname,author,isbn,marking,binding,location,condition,lentto,bookprice,pricepaid,quantity,img_path;

        bookname = book.getBook();
        author = book.getAuthor();
        isbn = book.getIsbn();
        marking = book.getMarking();
        binding = book.getBinding();
        location = book.getLocation();
        condition = book.getCondition();
        lentto = book.getLent_price();
        bookprice = book.getBook_price();
        pricepaid = book.getPaid_price();
        quantity = book.getQuantity();
        img_path = book.getImagePath();

        if(!img_path.trim().isEmpty() && img_path!=null){
            if(img_path.startsWith("http")){
                Picasso.get().load(img_path).into(img_book);
            }
            else {
                showImage(img_path);
            }
        }

        tv_bookname.setText(bookname);
        tv_author.setText(author);
        tv_isbn.setText(isbn);
        tv_marking.setText(marking);
        tv_binding.setText(binding);
        tv_location.setText(location);
        tv_condition.setText(condition);
        tv_lentto.setText(lentto);
        tv_bookprice.setText(bookprice);
        tv_pricepaid.setText(pricepaid);
        tv_quantity.setText(quantity);

        img_backpress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                FragmentBookList NAME = new FragmentBookList();
                fragmentTransaction.replace(R.id.frame1, NAME);
                fragmentTransaction.commit();
            }
        });


        editDta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args_1 = new Bundle();
                args_1.putString("Key", value);
                fragmentEditBook.setArguments(args_1);
                handleFragments(fragmentEditBook);
            }
        });

        deleteData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Setting message manually and performing action on button click
                builder.setMessage("Do you want to delete this book?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                db.deleteNote(book);
                                Toast.makeText(getActivity(),"Deleted Successfully",Toast.LENGTH_LONG).show();
                                FragmentManager fragmentManager = getFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                FragmentBookList NAME = new FragmentBookList();
                                fragmentTransaction.replace(R.id.frame1, NAME);
                                fragmentTransaction.commit();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //  Action for 'NO' Button
                                dialog.cancel();
                            }
                        });
                //Creating dialog box
                AlertDialog alert = builder.create();
                //Setting the title manually
                alert.setTitle("Delete Book");
                alert.show();



            }
        });



       // return super.onCreateView(inflater, container, savedInstanceState);
        return postView2;

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

    private void handleFragments(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame1, fragment);
        fragmentTransaction.commit();
    }
    private void showImage(String path) {
        File imgFile = new  File(path);

        if(imgFile.exists()){

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            img_book.setImageBitmap(myBitmap);

        }
    }
    /*private void setupToolbar(){
        ((AppCompatActivity)mainActivity).getSupportActionBar().hide();
        //((AppCompatActivity)mainActivity).getActionBar().hide();
        mainActivity.setSupportActionBar(toolbar);
    }*/
}
