package com.padmavathy.mylibrary.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.padmavathy.mylibrary.R;
import com.padmavathy.mylibrary.database.BorrowDatabaseHelper;
import com.padmavathy.mylibrary.database.HistoryDatabaseHelper;
import com.padmavathy.mylibrary.model.BorrowBook;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BorrowAdapter extends RecyclerView.Adapter<BorrowAdapter.MyViewHolder> {

    private Context context;
    private List<BorrowBook> notesList;
    ProgressDialog pd;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView book_img;
        public TextView title;
        public TextView personname;
        public TextView dateBor;
        public TextView dateLent;
        public TextView move_icon;
        public RelativeLayout relMove;

        public MyViewHolder(@NonNull View view) {
            super(view);
            book_img = view.findViewById(R.id.dot_boorrow);
            title = view.findViewById(R.id.title_bor);
            personname=view.findViewById(R.id.personName);
            dateBor = view.findViewById(R.id.dateBorrow);
            dateLent = view.findViewById(R.id.dateReturned);
            move_icon = view.findViewById(R.id.move);
            relMove = view.findViewById(R.id.bor_move);

            relMove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    moveItem(getAdapterPosition());
                }
            });

        }
    }

    private void moveItem(int adapterPosition) {
        BorrowDatabaseHelper db = new BorrowDatabaseHelper(context);
        HistoryDatabaseHelper historyDatabaseHelper = new HistoryDatabaseHelper(context);
        boolean insertData = false;

        BorrowBook borrow_1,borrow ;
        borrow_1 = notesList.get(adapterPosition);
        borrow = db.getBorrow(borrow_1.getId());

        String title,personName,dateReturned,dateBorrow,Imgpath;
        title = borrow_1.getTitle();
        personName = borrow_1.getPersonNmae();
        dateBorrow = borrow_1.getDateBor();
        dateReturned = borrow_1.getDateRet();
        Imgpath = borrow_1.getImgPath();
        Log.d("PRINT",title+"\n"+personName+"\n"+dateBorrow+"\n"+dateReturned+"\n"+Imgpath);
        insertData = inserData(historyDatabaseHelper,title,personName,dateBorrow,dateReturned,Imgpath);

        if (insertData == true) {
            db.deleteBorrow(borrow);
            notesList.remove(adapterPosition);
            notifyDataSetChanged();
            alertDialog();
        }

    }

    private boolean inserData(HistoryDatabaseHelper historyDatabaseHelper, String title, String personName, String dateBorrow, String dateReturned, String imgpath) {
        historyDatabaseHelper.insertHistory_Borrow(title,personName,dateBorrow,dateReturned,imgpath);
        return true;
    }

    private void alertDialog() {
        AlertDialog.Builder dialog=new AlertDialog.Builder(context);
        dialog.setMessage("Borrowed Book Details Moved Succesfully");
        dialog.setTitle("Borrowed Books");
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

    public BorrowAdapter(Context context, List<BorrowBook> notesList) {
        this.context = context;
        this.notesList = notesList;
        //setHasStableIds(true);
    }

    @Override
    public BorrowAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.borrow_list_row, parent, false);
        //setHasStableIds(true);
        return new BorrowAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(BorrowAdapter.MyViewHolder holder, int position) {
        BorrowBook note = notesList.get(position);
        File f = new File(note.getImgPath());
        Log.d("IMG_B",note.getImgPath());
        holder.title.setText(note.getTitle());
        holder.personname.setText(note.getPersonNmae());
        holder.dateBor.setText(note.getDateBor());
        holder.dateLent.setText(note.getDateRet());
        holder.setIsRecyclable(false);
       // setHasStableIds(true);
        Log.d("BORROW_Data",note.getTitle());
        if(!((note.getImgPath()).isEmpty())) {
            Picasso.get().load(f).into(holder.book_img);
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

    public void filteredList(ArrayList<BorrowBook> list){
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
