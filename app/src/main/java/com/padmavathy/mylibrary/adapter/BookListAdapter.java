package com.padmavathy.mylibrary.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.padmavathy.mylibrary.R;
import com.padmavathy.mylibrary.model.Book;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BookListAdapter extends RecyclerView.Adapter<BookListAdapter.BookViewHolder> implements Filterable {
    private Context context;
    private List<Book> notesList;
    private List<Book> exampleListFull;

    public class BookViewHolder extends RecyclerView.ViewHolder {
        public TextView note;
        public ImageView dot;
        public TextView timestamp;

        public BookViewHolder(View view) {
            super(view);
            note = view.findViewById(R.id.textViewName);
            dot = view.findViewById(R.id.imageView);
            timestamp = view.findViewById(R.id.textViewAuthor);
        }
    }
    public BookListAdapter(Context context, List<Book> notesList) {
        this.context = context;
        this.notesList = notesList;
        exampleListFull = new ArrayList<>(notesList);
    }

    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_list_row, parent, false);
        return new BookViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(BookViewHolder holder, int position) {
        Book note = notesList.get(position);
        File f = new File(note.getImagePath());
        Log.d("IMG",note.getImagePath());
        holder.timestamp.setText(note.getAuthor());
        holder.note.setText(note.getBook());
        if(!note.getImagePath().isEmpty()) {
            Picasso.get().load(f).into(holder.dot);
        }
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    public void filteredList(ArrayList<Book> list){
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
    public Filter getFilter() {
        return exampleFilter;
    }
    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Book> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(exampleListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Book item : exampleListFull) {
                    if (item.getBook().toLowerCase().contains(filterPattern)) {
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

}
