package com.padmavathy.mylibrary.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.padmavathy.mylibrary.R;
import com.padmavathy.mylibrary.model.Book;
import com.padmavathy.mylibrary.model.LentIn;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LentInAdapter extends RecyclerView.Adapter<LentInAdapter.BookViewHolder>{
    private Context context;
    private List<LentIn> notesList;
    private List<LentIn> exampleListFull;

    public class BookViewHolder extends RecyclerView.ViewHolder {
        private TextView note;
        private TextView timestamp,lentInDateTv;

        public BookViewHolder(@NonNull View view) {
            super(view);
            note = view.findViewById(R.id.textViewDateLentInPerson);
            timestamp = view.findViewById(R.id.textViewLentInDate);
            lentInDateTv = view.findViewById(R.id.lentInDate);
        }
    }
    public LentInAdapter(Context context, List<LentIn> notesList) {
        this.context = context;
        this.notesList = notesList;
        //setHasStableIds(true);
        exampleListFull = new ArrayList<>(notesList);
    }

    @Override
    public LentInAdapter.BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_lent_in_row, parent, false);
        //setHasStableIds(true);
        return new LentInAdapter.BookViewHolder(itemView);


    }

    @Override
    public void onBindViewHolder(LentInAdapter.BookViewHolder holder, int position) {
        LentIn note = notesList.get(position);

        String lentDate = note.getLent_in_out();

        if(lentDate.matches("Lent_In")){
            holder.lentInDateTv.setText("Lent In Date: ");
        }
        else if(lentDate.matches("Lent_Out")){
            holder.lentInDateTv.setText("Lent Out Date: ");
        }

        Log.d("LENT ",note.getLent_in_out()+","+note.getPersonName());

        holder.timestamp.setText(note.getDateLent());
        holder.note.setText(note.getPersonName());
        holder.setIsRecyclable(false);

//        setHasStableIds(true);
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    public void filteredList(ArrayList<LentIn> list){
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

   /* @Override
    public Filter getFilter() {
        return exampleFilter;
    }*/
    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<LentIn> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(exampleListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (LentIn item : exampleListFull) {
                    if (item.getPersonName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            notesList.clear();
            notesList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

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
