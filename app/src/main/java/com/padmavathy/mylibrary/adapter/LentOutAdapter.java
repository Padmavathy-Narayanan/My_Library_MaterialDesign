package com.padmavathy.mylibrary.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.padmavathy.mylibrary.R;
import com.padmavathy.mylibrary.model.LentOut;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LentOutAdapter extends RecyclerView.Adapter<LentOutAdapter.BookViewHolder>{
    private Context context;
    private List<LentOut> notesList;
    private List<LentOut> exampleListFull;

    public class BookViewHolder extends RecyclerView.ViewHolder {
        private TextView note;
        private TextView timestamp;

        public BookViewHolder(@NonNull View view) {
            super(view);
            note = view.findViewById(R.id.textViewDateLentInPerson);
            timestamp = view.findViewById(R.id.textViewLentInDate);
        }
    }
    public LentOutAdapter(Context context, List<LentOut> notesList) {
        this.context = context;
        this.notesList = notesList;
        //setHasStableIds(true);
        exampleListFull = new ArrayList<>(notesList);
    }

    @Override
    public LentOutAdapter.BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_lent_out_row, parent, false);
        //setHasStableIds(true);
        return new LentOutAdapter.BookViewHolder(itemView);


    }

    @Override
    public void onBindViewHolder(LentOutAdapter.BookViewHolder holder, int position) {
        LentOut note = notesList.get(position);

        holder.timestamp.setText(note.getDateLent());
        holder.note.setText(note.getPersonName());
        holder.setIsRecyclable(false);

//        setHasStableIds(true);
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    public void filteredList(ArrayList<LentOut> list){
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
            List<LentOut> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(exampleListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (LentOut item : exampleListFull) {
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
