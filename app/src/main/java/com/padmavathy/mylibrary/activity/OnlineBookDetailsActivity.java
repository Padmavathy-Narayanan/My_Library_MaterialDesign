package com.padmavathy.mylibrary.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.padmavathy.mylibrary.R;
import com.padmavathy.mylibrary.database.DatabaseHelper;
import com.padmavathy.mylibrary.database.WishlistDatabaseHelper;
import com.padmavathy.mylibrary.model.Book;
import com.padmavathy.mylibrary.model.Wishlist;

public class OnlineBookDetailsActivity extends AppCompatActivity {
    Book book;
    DatabaseHelper db;

    Wishlist wishlist;
    WishlistDatabaseHelper wishlistDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_book_details);

        db = new DatabaseHelper(this);
        book = new Book();

        wishlistDatabaseHelper = new WishlistDatabaseHelper(this);
        wishlist = new Wishlist();

        //hide the default actionBar
//        getSupportActionBar().hide();

        //Receive
        Bundle extras = getIntent().getExtras();
        String title =""
                , authors ="", description="" , categories ="", publishDate=""
                ,info ="", buy ="",preview ="" ,thumbnail ="",pageCount = "",rating ="",isbn="";
        if(extras != null){
            title = extras.getString("book_title");
            authors = extras.getString("book_author");
            description = extras.getString("book_desc");
            categories = extras.getString("book_categories");
            publishDate = extras.getString("book_publish_date");
            info = extras.getString("book_info");
            buy = extras.getString("book_buy");
            preview = extras.getString("book_preview");
            thumbnail = extras.getString("book_thumbnail");
            pageCount =extras.getString("book_pageCount");
            rating =extras.getString("book_rating");
            isbn = extras.getString("book_isbn");
        }

        Log.d("BUY_LINK",buy+": "+info);

        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_id);
        collapsingToolbarLayout.setTitleEnabled(true);

        TextView tvTitle = findViewById(R.id.aa_book_name);
        TextView tvAuthors = findViewById(R.id.aa_author);
        TextView tvDesc = findViewById(R.id.aa_description);
/*        TextView tvCatag = findViewById(R.id.aa_categorie);
        TextView tvPublishDate = findViewById(R.id.aa_publish_date);*/
        TextView tvInfo = findViewById(R.id.aa_info);
        TextView tvPageCount  = findViewById(R.id.aa_page_count);
        TextView tvRating = findViewById(R.id.aa_Rating);
        TextView tvAddToWishlist = findViewById(R.id.aa_wishlist);
        TextView tvAddoList = findViewById(R.id.aa_add_to_list);

        final String finalTitle = title;
        final String finalAuthors = authors;
        final String finalThumbnail = thumbnail;
        final String finalIsbn = isbn;
        final String finalBuy = buy;
        final String finalInfo = info;

        tvAddoList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(OnlineBookDetailsActivity.this);
                alertDialogBuilder.setMessage("Are you sure, You wanted to add this book into Home list");
                        alertDialogBuilder.setPositiveButton("yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        book.setBook(finalTitle);
                                        book.setAuthor(finalAuthors);
                                        book.setIsbn(finalIsbn);
                                        book.setImagePath(finalThumbnail);
                                        db.insertNote(finalTitle,finalAuthors, finalIsbn,"","","","","","","","", finalThumbnail,"","","");

                                        Toast.makeText(OnlineBookDetailsActivity.this,"Book Added Successfully",Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(OnlineBookDetailsActivity.this,BottomNavActivity.class));
                                    }
                                });

                alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });


        tvAddToWishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(OnlineBookDetailsActivity.this);
                alertDialogBuilder.setMessage("You want to add this book into wish list");
                alertDialogBuilder.setPositiveButton("yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                wishlist.setTitle(finalTitle);
                                wishlist.setPersonNmae(finalAuthors);
                                wishlist.setImgPath(finalThumbnail);
                                wishlist.setIsbn(finalIsbn);
                                String buyLink = "";
                                if(finalBuy!=null && !finalBuy.isEmpty()){
                                    buyLink = finalBuy;
                                }
                                else if(finalInfo!=null && !finalInfo.isEmpty()){
                                    buyLink = finalInfo;
                                }
                                else{
                                    buyLink = "";
                                }

                                wishlistDatabaseHelper.insertWishlist(finalTitle,finalAuthors,finalThumbnail,finalIsbn,buyLink);
                                Toast.makeText(OnlineBookDetailsActivity.this,"Book Added to Wishlist",Toast.LENGTH_LONG).show();
                                startActivity(new Intent(OnlineBookDetailsActivity.this,BottomNavActivity.class));
                            }
                        });

                alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }
        });

        ImageView ivThumbnail = findViewById(R.id.aa_thumbnail);

        tvTitle.setText(title);
        tvAuthors.setText("by "+authors);
        tvDesc.setText(description);
    /*    tvCatag.setText("Category : "+categories);
        tvPublishDate.setText("Published Date: "+publishDate);*/

        tvPageCount.setText("Page Count: "+String.valueOf(pageCount));
        tvRating.setText("Rating: "+String.valueOf(rating));
        if(rating != null && rating.length()>0){

        }
        else {
            //bookRatingBar.setRating(rating);
        }

        Log.d("PAGE",pageCount+":"+rating);

        final String finalBuy_1 = buy;
        final String finalInfo_2 = info;
        tvInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (finalBuy != null && !finalBuy.isEmpty()) {
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(finalBuy_1));
                    startActivity(i);
                }
                else if(finalInfo != null && !finalInfo.isEmpty()){
                    Intent i = new Intent(Intent.ACTION_VIEW,Uri.parse(finalInfo_2));
                    startActivity(i);
                }
                else {
                    Toast.makeText(OnlineBookDetailsActivity.this,"This book is not for sale",Toast.LENGTH_LONG).show();
                }
            }
        });


        final String finalPreview = preview;


        collapsingToolbarLayout.setTitle(title);

        RequestOptions requestOptions = new RequestOptions().centerCrop().placeholder(R.drawable.loading_shape).error(R.drawable.loading_shape);

        Glide.with(this).load(thumbnail).apply(requestOptions).into(ivThumbnail);

    }
}