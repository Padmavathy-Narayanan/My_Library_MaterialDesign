package com.padmavathy.mylibrary.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.padmavathy.mylibrary.R;
import com.padmavathy.mylibrary.activity.BottomNavActivity;
import com.padmavathy.mylibrary.adapter.CustomGrid;
import com.padmavathy.mylibrary.adapter.OnlineBookAdapter;
import com.padmavathy.mylibrary.database.DatabaseHelper;
import com.padmavathy.mylibrary.model.Book;
import com.padmavathy.mylibrary.model.BookOnlineSearch;
import com.padmavathy.mylibrary.model.Wishlist;
import com.padmavathy.mylibrary.utils.RecyclerTouchListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.CONNECTIVITY_SERVICE;

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
    Button button_view,buttonSearchImage;

    RelativeLayout relativeLayoutBookDetails;

    FragmentLentView fragmentLentView;
    private RequestQueue mRequestQueue;

    ProgressBar progressBar;
    RecyclerView grid;
    CustomGrid adapter;
    private ArrayList<BookOnlineSearch> imageList;

    BookOnlineSearch n;

    private static  final  String BASE_URL="https://www.googleapis.com/books/v1/volumes?q=";
    LinearLayout onlineBookImageListView;
    private RecyclerView.LayoutManager mLayoutManager;

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

        grid = (RecyclerView) postView2.findViewById(R.id.gridView);
        onlineBookImageListView = (LinearLayout)postView2.findViewById(R.id.onlineBookImageListView);
        mRequestQueue = Volley.newRequestQueue(getContext());

        imageList = new ArrayList<>();

        adapter = new CustomGrid(getContext(), imageList);

        buttonSearchImage =(Button)postView2.findViewById(R.id.searchImage);

        img_backpress = (ImageView)postView2.findViewById(R.id.img_back);
        editDta = (ImageView)postView2.findViewById(R.id.img_editdata);
        deleteData = (ImageView)postView2.findViewById(R.id.deleteData);
        relativeLayoutBookDetails = (RelativeLayout)postView2.findViewById(R.id.details_relative_layout);

        toolbar_tit = (TextView)postView2.findViewById(R.id.toolbar_title_view_isbn);
        button_view = (Button) postView2.findViewById(R.id.view);
        progressBar = (ProgressBar) postView2.findViewById(R.id.progressCircular);
        fragmentEditBook =  new FragmentEditBook();
        fragmentLentView = new FragmentLentView();

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getContext());
        grid.setLayoutManager(mLayoutManager);

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

        Log.d("LENT_ISBN_V",isbn);

            button_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ConnectivityManager connMgr = (ConnectivityManager) getActivity()
                            .getSystemService(Context.CONNECTIVITY_SERVICE);

                    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

                    if (networkInfo != null && networkInfo.isConnected()) {
                        // fetch data
                        Bundle args_1 = new Bundle();
                        Log.d("LENT_ISBN_V", isbn);
                        args_1.putString("ValueKey", isbn);
                        fragmentLentView.setArguments(args_1);
                        handleFragments(fragmentLentView);
                    } else {
                        // display error
                        Toast.makeText(getContext(),"No internet Connection,Please try again!",Toast.LENGTH_LONG).show();
                    }
                }

            });

            buttonSearchImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imageList.clear();
                    search(bookname,author,isbn);
                    progressBar.setVisibility(View.VISIBLE);
                }
            });



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

        grid.addOnItemTouchListener(new RecyclerTouchListener(getActivity(),
                grid, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                n = imageList.get(position);

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                alertDialog.setTitle("Update Image");
                alertDialog.setMessage("Do you want to update this image?");
                alertDialog.setPositiveButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.setNegativeButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // DO SOMETHING HERE
                        book.setIsbn(isbn);
                        book.setBook(bookname);
                        book.setAuthor(author);
                        book.setMarking(marking);
                        book.setBinding(binding);
                        book.setLocation(location);
                        book.setCondition(condition);
                        book.setLent_price(lentto);
                        book.setBook_price(bookprice);
                        book.setPaid_price(pricepaid);
                        book.setQuantity(quantity);
                        book.setImagePath(n.getThumbnail());


                        db.updateNote(book);
                        Toast.makeText(getContext(),"Updated Successfully",Toast.LENGTH_LONG).show();
                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        FragmentBookList NAME = new FragmentBookList();
                        fragmentTransaction.replace(R.id.frame1, NAME);
                        fragmentTransaction.commit();

                        dialog.dismiss();

                    }
                });

                AlertDialog dialog = alertDialog.create();
                dialog.show();

                /*Bundle args = new Bundle();
                args.putString("Key", String.valueOf(n.getId()));
                fragment.setArguments(args);
                handleFragments(fragment);*/
                //Toast.makeText(getActivity(),""+n.getThumbnail(),Toast.LENGTH_LONG).show();
                /*Intent i = new Intent(getActivity(), BookViewActivity.class);
                i.putExtra("Note",notesList.get(position));
                startActivity(i);
*/
            }

            @Override
            public void onLongClick(View view, int position) {
                //showActionsDialog(position);
            }

        }));


       // return super.onCreateView(inflater, container, savedInstanceState);
        return postView2;

    }

    private void search(String BookNme,String ISBN,String Author)
    {
        String search_query = "";
        String bookISBN = BookNme;
        String bookBook = ISBN;
        String bookAuthor = Author;

        if (!bookISBN.matches("")){
            search_query =bookISBN;
        }
        else if (!bookBook.matches("")){
            search_query =bookBook;
        }
        else if (!bookAuthor.matches("")){
            search_query =bookAuthor;
        }

        boolean is_connected = Read_network_state(getActivity());
        if(!is_connected)
        {
            Toast.makeText(getContext(),"Cannot load Image",Toast.LENGTH_LONG).show();
            /*error_message.setText(R.string.Failed_to_Load_data);
            mRecyclerView.setVisibility(View.GONE);
            error_message.setVisibility(View.VISIBLE);*/
            return;
        }

        //  Log.d("QUERY",search_query);


        if(bookISBN.isEmpty() && bookBook.isEmpty() && bookAuthor.isEmpty())
        {
            Toast.makeText(getContext(),"ISBN or Author Name or Book Name is needed to search!",Toast.LENGTH_SHORT).show();
            return;
        }

        String final_query=search_query.replace(" ","+");
        Uri uri=Uri.parse(BASE_URL+final_query);
        Uri.Builder buider = uri.buildUpon();

        parseJson(buider.toString());

    }

    private void parseJson(final String key) {
        //relativeLayoutBookDetails.setVisibility(View.GONE);
        onlineBookImageListView.setVisibility(View.VISIBLE);

        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, key.toString(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.GONE);
                        String title ="";
                        String author ="";
                        String publishedDate = "NoT Available";
                        String description = "No Description";
                        int pageCount = 1000;
                        String categories = "No categories Available ";
                        String buy ="";
                        String averageRating = "";

                        String price = "NOT_FOR_SALE";
                        try {
                            JSONArray items = response.getJSONArray("items");

                            for (int i = 0 ; i< items.length() ;i++){
                                JSONObject item = items.getJSONObject(i);
                                JSONObject volumeInfo = item.getJSONObject("volumeInfo");



                                try{
                                    title = volumeInfo.getString("title");

                                    JSONArray authors = volumeInfo.getJSONArray("authors");
                                    if(authors.length() == 1){
                                        author = authors.getString(0);
                                    }else {
                                        author = authors.getString(0) + "|" +authors.getString(1);
                                    }


                                    publishedDate = volumeInfo.getString("publishedDate");
                                    pageCount = volumeInfo.getInt("pageCount");



                                    JSONObject saleInfo = item.getJSONObject("saleInfo");
                                    JSONObject listPrice = saleInfo.getJSONObject("listPrice");
                                    price = listPrice.getString("amount") + " " +listPrice.getString("currencyCode");
                                    description = volumeInfo.getString("description");
                                    averageRating = volumeInfo.getString("averageRating");
                                    buy = saleInfo.getString("buyLink");
                                    categories = volumeInfo.getJSONArray("categories").getString(0);

                                }catch (Exception e){

                                }
                                String thumbnail ="";
                                JSONObject thumbnailUrlObject = volumeInfo.optJSONObject("imageLinks");
                                if (thumbnailUrlObject != null && thumbnailUrlObject.has("thumbnail")) {
                                    thumbnail = thumbnailUrlObject.getString("thumbnail");
                                }
                                //String thumbnail = volumeInfo.getJSONObject("imageLinks").getString("thumbnail");

                                String isbn = volumeInfo.getJSONArray("industryIdentifiers").getJSONObject(0).getString("identifier");

                                String previewLink = volumeInfo.getString("previewLink");
                                String url = volumeInfo.getString("infoLink");
                                //String buylink = volumeInfo.getString("webReaderLink");


                                imageList.add(new BookOnlineSearch(title , author , publishedDate , description ,categories
                                        ,thumbnail,buy,previewLink,price,pageCount,url,averageRating,isbn));

                                String book_info = "Title: "+title +"\n"+"Author : "+author+"\n"+"Publish Date: "+publishedDate+"\n"+"Description: "+description+"\n"+
                                        "Categories: "+categories+"\n"+"Thumbnail: "+thumbnail+"\n"+"Buy: "+buy+"\n"+"Preview :"+previewLink+"\n"+
                                        "Price : "+price+"\n"+"Page Count: "+pageCount+"\n"+"URI:"+url+"\n"+"AVERAGE RATING"+averageRating;

                                Log.d("Book_Info",book_info+"Key: "+thumbnail);


                                adapter = new CustomGrid(getContext() , imageList);

                                if(thumbnail!=null && !thumbnail.isEmpty()){
                                    grid.setAdapter(adapter);
                                }
                                else {
                                    Toast.makeText(getContext(),"No images found",Toast.LENGTH_LONG).show();
                                }
                            }


                        } catch (JSONException e) {
                            Toast.makeText(getContext(),"No images found",Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                            Log.e("TAG" , e.toString());

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"No images found",Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        });
        mRequestQueue.add(request);
    }

    private boolean Read_network_state(Context context)
    {    boolean is_connected;
        ConnectivityManager cm =(ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        is_connected=info!=null&&info.isConnectedOrConnecting();
        return is_connected;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
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
