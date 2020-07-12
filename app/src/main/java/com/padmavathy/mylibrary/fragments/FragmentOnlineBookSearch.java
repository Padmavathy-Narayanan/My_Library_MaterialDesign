package com.padmavathy.mylibrary.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.padmavathy.mylibrary.R;
import com.padmavathy.mylibrary.adapter.OnlineBookAdapter;
import com.padmavathy.mylibrary.model.BookOnlineSearch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class FragmentOnlineBookSearch extends Fragment {
    RelativeLayout rel_no_internet,relOnlineLayout;
    private Button refreshBtn;
    private Button onLineSearch;


    //Online Book List
    private RecyclerView mRecyclerView;
    private ArrayList<BookOnlineSearch> mBooks;
    private OnlineBookAdapter mAdapter;
    private RequestQueue mRequestQueue;

    private static  final  String BASE_URL="https://www.googleapis.com/books/v1/volumes?q=";

    private EditText search_edit_text_book,search_edit_text_isbn,search_edit_text_author;
    private Button search_button;
    private ProgressBar loading_indicator;
    private TextView error_message;

    LinearLayout lin_booklistView;
    ProgressBar progressBar;

    public FragmentOnlineBookSearch() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View postView1 = inflater.inflate(R.layout.fragment_online_book_search, container, false);
        rel_no_internet = postView1.findViewById(R.id.interenet);
        refreshBtn = postView1.findViewById(R.id.refresh);
        relOnlineLayout = postView1.findViewById(R.id.onlineBookSearchRelativeLayout);
        onLineSearch = postView1.findViewById(R.id.online_buttonSearch);

        search_edit_text_isbn=postView1.findViewById(R.id.online_isbn);
        search_edit_text_book = postView1.findViewById(R.id.online_book_name);
        search_edit_text_author = postView1.findViewById(R.id.online_authorname);

        loading_indicator=postView1.findViewById(R.id.loading_indicator);
        error_message= postView1.findViewById(R.id.message_display);
        progressBar = postView1.findViewById(R.id.progressCircular);

        lin_booklistView = postView1.findViewById(R.id.onlineBookListView);

        mRecyclerView = postView1.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mBooks = new ArrayList<>();
        mRequestQueue = Volley.newRequestQueue(getContext());

        if(isNetworkAvailable()){
            rel_no_internet.setVisibility(View.GONE);
            relOnlineLayout.setVisibility(View.VISIBLE);
        }
        else {
            rel_no_internet.setVisibility(View.VISIBLE);
            relOnlineLayout.setVisibility(View.GONE);
            refreshBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isNetworkAvailable()){
                        relOnlineLayout.setVisibility(View.VISIBLE);
                        rel_no_internet.setVisibility(View.GONE);
                    }
                    else {
                        relOnlineLayout.setVisibility(View.GONE);
                        rel_no_internet.setVisibility(View.VISIBLE);
                    }
                }
            });
        }

        onLineSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBooks.clear();
                search();
                progressBar.setVisibility(View.VISIBLE);
                //Toast.makeText(getContext(),"Searching...",Toast.LENGTH_LONG).show();
            }
        });
        return postView1;
    }

    private void search()
    {
        String search_query = "";
        String bookISBN = search_edit_text_isbn.getText().toString();
        String bookBook = search_edit_text_book.getText().toString();
        String bookAuthor = search_edit_text_author.getText().toString();

        if (!bookISBN.matches("")){
            search_query =bookISBN;
        }
        if (!bookBook.matches("")){
            search_query =bookBook;
        }
        if (!bookAuthor.matches("")){
            search_query =bookAuthor;
        }

        boolean is_connected = Read_network_state(getActivity());
        if(!is_connected)
        {
            error_message.setText(R.string.Failed_to_Load_data);
            mRecyclerView.setVisibility(View.GONE);
            error_message.setVisibility(View.VISIBLE);
            return;
        }

        //  Log.d("QUERY",search_query);


        if(search_edit_text_isbn.equals("") && search_edit_text_author.equals("") && search_edit_text_book.equals(""))
        {
            Toast.makeText(getContext(),"Please enter any one of the fields!",Toast.LENGTH_SHORT).show();
            return;
        }

        String final_query=search_query.replace(" ","+");
        Uri uri=Uri.parse(BASE_URL+final_query);
        Uri.Builder buider = uri.buildUpon();

        parseJson(buider.toString());

    }

    private void parseJson(final String key) {
        relOnlineLayout.setVisibility(View.GONE);
        lin_booklistView.setVisibility(View.VISIBLE);

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


                                mBooks.add(new BookOnlineSearch(title , author , publishedDate , description ,categories
                                        ,thumbnail,buy,previewLink,price,pageCount,url,averageRating,isbn));

                                String book_info = "Title: "+title +"\n"+"Author : "+author+"\n"+"Publish Date: "+publishedDate+"\n"+"Description: "+description+"\n"+
                                        "Categories: "+categories+"\n"+"Thumbnail: "+thumbnail+"\n"+"Buy: "+buy+"\n"+"Preview :"+previewLink+"\n"+
                                        "Price : "+price+"\n"+"Page Count: "+pageCount+"\n"+"URI:"+url+"\n"+"AVERAGE RATING"+averageRating;

                                Log.d("Book_Info",book_info+"Key: "+key);


                                mAdapter = new OnlineBookAdapter(getContext() , mBooks);
                                mRecyclerView.setAdapter(mAdapter);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("TAG" , e.toString());

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
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
}
