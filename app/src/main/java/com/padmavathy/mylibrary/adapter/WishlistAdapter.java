package com.padmavathy.mylibrary.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.padmavathy.mylibrary.R;
import com.padmavathy.mylibrary.database.HistoryDatabaseHelper;
import com.padmavathy.mylibrary.database.WishlistDatabaseHelper;
import com.padmavathy.mylibrary.model.History_Borrow;
import com.padmavathy.mylibrary.model.Wishlist;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.MyViewHolder> {

    private Context context;
    private List<Wishlist> notesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView book_img;
        public TextView title;
        public TextView personname;
        public TextView dateBor;
        private RelativeLayout move_icon,buyLink;

        public MyViewHolder(@NonNull View view) {
            super(view);
            book_img = view.findViewById(R.id.dot_online);
            title = view.findViewById(R.id.title_online_book);
            personname=view.findViewById(R.id.authorNameOnline);
            dateBor = view.findViewById(R.id.isbnOnlineTv);
            buyLink = view.findViewById(R.id.buyLink);

            move_icon = view.findViewById(R.id.online_book_del);


            buyLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buyItem(getAdapterPosition());
                }
            });

            move_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    moveItem(getAdapterPosition());
                }
            });

        }
    }

    private void buyItem(int adapterPos){
        WishlistDatabaseHelper db1 =  new WishlistDatabaseHelper(context);
        Wishlist wishlist,wishlist1;
        wishlist = notesList.get(adapterPos);
        wishlist1 = db1.getWishlist(wishlist.getId());

        String buyLink = wishlist1.getBuyLink();

        if(buyLink!=null && !buyLink.isEmpty()) {
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(buyLink));
            context.startActivity(i);
        }
        else {
            Toast.makeText(context,"Not for sale",Toast.LENGTH_LONG).show();
        }
    }

    private void moveItem(int adapterPosition) {
        WishlistDatabaseHelper db = new WishlistDatabaseHelper(context);
        Wishlist borrow_1,borrow ;
        borrow_1 = notesList.get(adapterPosition);
        borrow = db.getWishlist(borrow_1.getId());
        db.deleteWishlist(borrow);
        notesList.remove(adapterPosition);
        notifyDataSetChanged();
        alertDialog(context);
    }

    private void alertDialog(Context context) {
        AlertDialog.Builder dialog=new AlertDialog.Builder(this.context);
        dialog.setMessage("Wishlist Deleted!");
        dialog.setTitle("Wishlist");
        dialog.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        dialog.dismiss();
                        //Toast.makeText(context,"Yes is clicked",Toast.LENGTH_LONG).show();
                    }
                });


        AlertDialog alertDialog=dialog.create();
        alertDialog.show();
    }

    public WishlistAdapter(Context context, List<Wishlist> notesList) {
        this.context = context;
        this.notesList = notesList;
        //setHasStableIds(true);

    }

    @Override
    public WishlistAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.wishlist_list_row, parent, false);
        //setHasStableIds(true);
        return new WishlistAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(WishlistAdapter.MyViewHolder holder, int position) {
        Wishlist note = notesList.get(position);
        File f = new File(note.getImgPath());
        Log.d("IMG_B",note.getImgPath());
        String title,personName,dateReturned,dateBorrow,Imgpath;
        title = note.getTitle();
        personName = note.getPersonNmae();
        dateBorrow = note.getIsbn();
        Imgpath = note.getImgPath();
        Log.d("HISTORY",title+"\n"+personName+"\n"+dateBorrow+"\n"+"\n"+Imgpath);

        holder.title.setText(note.getTitle());
        holder.personname.setText(note.getPersonNmae());
        holder.dateBor.setText(note.getIsbn());
        holder.setIsRecyclable(false);
        //setHasStableIds(true);

        Log.d("BORROW_Data",note.getTitle());
        if(!((note.getImgPath()).isEmpty())) {
            if (note.getImgPath().startsWith("http")) {
                Log.d("Image_URL", note.getImgPath());
                Picasso.get().load(note.getImgPath()).into(holder.book_img);
            }
            else {
                Picasso.get().load(f).into(holder.book_img);
            }

        }

        /*// Displaying dot from HTML character code
        holder.dot.setText(Html.fromHtml("&#8226;"));*/

        // Formatting and displaying timestamp
        //holder.timestamp.setText(formatDate(note.getTimestamp()));
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    public void filteredList(ArrayList<Wishlist> list){
        notesList = list;
        notifyDataSetChanged();
    }

    /**
     * Formatting timestamp to `MMM d` format
     * Input: 2018-02-21 00:15:42
     * Output: Feb 21
     */

    private String getFirstChar(String bookname){
        char firstchar = bookname.charAt(0);
        return Character.toString(firstchar);
    }
    private String formatDate(String dateStr) {
        try {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = fmt.parse(dateStr);
            SimpleDateFormat fmtOut = new SimpleDateFormat("MMM d");
            return fmtOut.format(date);
        } catch (ParseException e) {

        }

        return "";
    }
    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(hasStableIds);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }
}

