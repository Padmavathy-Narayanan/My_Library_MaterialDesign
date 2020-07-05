package com.padmavathy.mylibrary.adapter;

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
import com.padmavathy.mylibrary.database.HistoryDatabaseHelper;
import com.padmavathy.mylibrary.model.History_Borrow;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {

    private Context context;
    private List<History_Borrow> notesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView book_img;
        public TextView title;
        public TextView personname;
        public TextView dateBor;
        public TextView dateLent;
        private RelativeLayout move_icon;

        public MyViewHolder(@NonNull View view) {
            super(view);
            book_img = view.findViewById(R.id.dot_History);
            title = view.findViewById(R.id.title_his);
            personname=view.findViewById(R.id.personNameHistory);
            dateBor = view.findViewById(R.id.dateBorrowHistory);
            dateLent = view.findViewById(R.id.dateReturned_HISTORY);
            move_icon = view.findViewById(R.id.bor_move_his);

            move_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    moveItem(getAdapterPosition());
                }
            });

        }
    }

    private void moveItem(int adapterPosition) {
        HistoryDatabaseHelper db = new HistoryDatabaseHelper(context);
        History_Borrow borrow_1,borrow ;
        borrow_1 = notesList.get(adapterPosition);
        borrow = db.getHistory_Borrow(borrow_1.getId());
        db.deleteHistory_Borrow(borrow);
        notesList.remove(adapterPosition);
        notifyDataSetChanged();
        alertDialog(context);
    }

    private void alertDialog(Context context) {
        AlertDialog.Builder dialog=new AlertDialog.Builder(this.context);
        dialog.setMessage("History Deleted!");
        dialog.setTitle("History_Borrowed Books");
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

    public HistoryAdapter(Context context, List<History_Borrow> notesList) {
        this.context = context;
        this.notesList = notesList;
        //setHasStableIds(true);

    }



    @Override
    public HistoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_list_row, parent, false);
        //setHasStableIds(true);
        return new HistoryAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(HistoryAdapter.MyViewHolder holder, int position) {
        History_Borrow note = notesList.get(position);
        File f = new File(note.getDateRet());
        Log.d("IMG_B",note.getDateRet());
        String title,personName,dateReturned,dateBorrow,Imgpath;
        title = note.getTitle();
        personName = note.getPersonNmae();
        dateBorrow = note.getDateBor();
        dateReturned = note.getDateRet();
        Imgpath = note.getImgPath();
        Log.d("HISTORY",title+"\n"+personName+"\n"+dateBorrow+"\n"+dateReturned+"\n"+Imgpath);

        holder.title.setText(note.getTitle());
        holder.personname.setText(note.getPersonNmae());
        holder.dateBor.setText(note.getDateBor());
        holder.dateLent.setText(Imgpath);
        holder.setIsRecyclable(false);
        //setHasStableIds(true);

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

    public void filteredList(ArrayList<History_Borrow> list){
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
