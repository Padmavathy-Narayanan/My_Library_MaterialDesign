package com.padmavathy.mylibrary.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.padmavathy.mylibrary.R;
import com.padmavathy.mylibrary.activity.OnlineBookDetailsActivity;
import com.padmavathy.mylibrary.model.BookOnlineSearch;

import java.util.List;

public class OnlineBookAdapter extends RecyclerView.Adapter<OnlineBookAdapter.MyViewHolder> {

    private Context mContext;
    private List<BookOnlineSearch> mData;
    private RequestOptions options;

    public OnlineBookAdapter(Context mContext, List<BookOnlineSearch> mData) {
        this.mContext = mContext;
        this.mData = mData;

        //Request option for Glide
        options = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE).centerCrop().placeholder(R.drawable.loading_shape).error(R.drawable.loading_shape);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.book_raw_item, parent, false);
        final MyViewHolder viewHolder = new MyViewHolder(view);
        viewHolder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, OnlineBookDetailsActivity.class);
                int pos = viewHolder.getAdapterPosition();
                i.putExtra("book_title", mData.get(pos).getTitle());
                i.putExtra("book_author", mData.get(pos).getAuthors());
                i.putExtra("book_desc", mData.get(pos).getDescription());
                i.putExtra("book_categories", mData.get(pos).getCategories());
                i.putExtra("book_publish_date", mData.get(pos).getPublishedDate());
                i.putExtra("book_info", mData.get(pos).getmUrl());
                i.putExtra("book_buy", mData.get(pos).getBuy());
                i.putExtra("book_preview", mData.get(pos).getPerview());
                i.putExtra("book_thumbnail", mData.get(pos).getThumbnail());
                i.putExtra("book_rating",mData.get(pos).getRating());
                i.putExtra("book_pageCount",String.valueOf(mData.get(pos).getPageCount()));
                i.putExtra("book_isbn",mData.get(pos).getIsbn());

                Log.d("PG",mData.get(pos).getRating()+":"+mData.get(pos).getPageCount());


                mContext.startActivity(i);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {

        BookOnlineSearch book = mData.get(i);
        holder.tvTitle.setText(book.getTitle());
        holder.tvAuthor.setText("by " +book.getAuthors());

        if(book.getRating() == ""){

        }
        else {
            holder.tvPrice.setText("Rating: " + String.valueOf(book.getRating()));
            holder.ratingBar.setRating(Float.parseFloat(book.getRating()));
        }
        holder.tvCategory.setText("Page Count: "+String.valueOf(book.getPageCount()));

        //load image from internet and set it into imageView using Glide
        if(book.getThumbnail()==null){
            //Glide.with(mContext).load("http://books.google.com/books/content?id=ikl91bkuYpMC&printsec=frontcover&img=1&zoom=5&edge=curl&source=gbs_api").apply(options).dontAnimate().into(holder.ivThumbnail);
        }
        else {


            Glide.with(mContext).load(book.getThumbnail()).apply(options).dontAnimate().into(holder.ivThumbnail);
            Log.d("Image_URL", book.getThumbnail());
        }

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView ivThumbnail;
        TextView tvTitle, tvCategory, tvPrice, tvAuthor;
        LinearLayout container;
        RatingBar ratingBar;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            ivThumbnail = itemView.findViewById(R.id.thumbnail);
            tvTitle = itemView.findViewById(R.id.title);
            tvAuthor = itemView.findViewById(R.id.author);
            tvCategory = itemView.findViewById(R.id.category);
            tvPrice = itemView.findViewById(R.id.price);
            container = itemView.findViewById(R.id.container);
            ratingBar = itemView.findViewById(R.id.ratingBar);

        }

    }
}
