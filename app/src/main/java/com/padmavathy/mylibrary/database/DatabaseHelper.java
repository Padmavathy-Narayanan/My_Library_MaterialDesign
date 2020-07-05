package com.padmavathy.mylibrary.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.padmavathy.mylibrary.model.Book;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "MyLibray_db";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        // create notes table
        db.execSQL(Book.CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + Book.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    public long insertNote(String book, String author,String isbn,String condition,String marking,String binding,String location,String lent_price,String book_price,String paid_price,String quantity,String Imagepath,String dateLent,String dateReturned,String currentTimesatmp) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValue = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        contentValue.put(Book.COLUMN_BOOK, book);
        contentValue.put(Book.COLUMN_AUTHOR,author);
        contentValue.put(Book.COLUMN_ISBN,isbn);
        contentValue.put(Book.COLUMN_CONDITION,condition);
        contentValue.put(Book.COLUMN_MARKING,marking);
        contentValue.put(Book.COLUMN_BINDING,binding);
        contentValue.put(Book.COLUMN_LOCATION,location);
        contentValue.put(Book.COLUMN_LENT_PRICE,lent_price);
        contentValue.put(Book.COLUMN_BOOK_PRICE,book_price);
        contentValue.put(Book.COLUMN_PAID_PRICE,paid_price);
        contentValue.put(Book.COLUMN_QUANTITY,quantity);
        contentValue.put(Book.COLUMN_IMG_PATH,Imagepath);
        contentValue.put(Book.COLUMN_DATE_LENT,dateLent);
        contentValue.put(Book.COLUMN_DATE_RETURNED,dateReturned);
        contentValue.put(Book.COLUMN_CURRENT_DATE,currentTimesatmp);

        // insert row
        long id = db.insert(Book.TABLE_NAME, null, contentValue);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    public Book getNote(long id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Book.TABLE_NAME,
                new String[]{Book.COLUMN_ID, Book.COLUMN_BOOK, Book.COLUMN_AUTHOR ,Book.COLUMN_ISBN,Book.COLUMN_CONDITION,Book.COLUMN_MARKING,Book.COLUMN_BINDING,Book.COLUMN_LOCATION,Book.COLUMN_LENT_PRICE, Book.COLUMN_BOOK_PRICE,Book.COLUMN_PAID_PRICE,Book.COLUMN_QUANTITY,Book.COLUMN_IMG_PATH,Book.COLUMN_DATE_LENT,Book.COLUMN_DATE_RETURNED,Book.COLUMN_CURRENT_DATE},
                Book.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare note object
        Book note = new Book(
                cursor.getInt(cursor.getColumnIndex(Book.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(Book.COLUMN_BOOK)),
                cursor.getString(cursor.getColumnIndex(Book.COLUMN_AUTHOR)),
                cursor.getString(cursor.getColumnIndex(Book.COLUMN_ISBN)),
                cursor.getString(cursor.getColumnIndex(Book.COLUMN_CONDITION)),
                cursor.getString(cursor.getColumnIndex(Book.COLUMN_MARKING)),
                cursor.getString(cursor.getColumnIndex(Book.COLUMN_BINDING)),
                cursor.getString(cursor.getColumnIndex(Book.COLUMN_LOCATION)),
                cursor.getString(cursor.getColumnIndex(Book.COLUMN_LENT_PRICE)),
                cursor.getString(cursor.getColumnIndex(Book.COLUMN_BOOK_PRICE)),
                cursor.getString(cursor.getColumnIndex(Book.COLUMN_PAID_PRICE)),
                cursor.getString(cursor.getColumnIndex(Book.COLUMN_QUANTITY)),
                cursor.getString(cursor.getColumnIndex(Book.COLUMN_IMG_PATH)),
                cursor.getString(cursor.getColumnIndex(Book.COLUMN_DATE_LENT)),
                cursor.getString(cursor.getColumnIndex(Book.COLUMN_DATE_RETURNED)),
                cursor.getString(cursor.getColumnIndex(Book.COLUMN_CURRENT_DATE)));

        // close the db connection
        cursor.close();

        return note;
    }

    public List<Book> getAllNotes() {
        List<Book> notes = new ArrayList<>();

        // Select All Query
        //String selectQuery = "SELECT  * FROM " + Book.TABLE_NAME + " ORDER BY " + Book.COLUMN_BOOK + " ASC";

        String selectQuery = "SELECT  * FROM " + Book.TABLE_NAME ;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Book note = new Book();
                note.setId(cursor.getInt(cursor.getColumnIndex(Book.COLUMN_ID)));
                note.setBook(cursor.getString(cursor.getColumnIndex(Book.COLUMN_BOOK)));
                note.setAuthor(cursor.getString(cursor.getColumnIndex(Book.COLUMN_AUTHOR)));
                note.setIsbn(cursor.getString(cursor.getColumnIndex(Book.COLUMN_ISBN)));
                note.setCondition(cursor.getString(cursor.getColumnIndex(Book.COLUMN_CONDITION)));
                note.setMarking(cursor.getString(cursor.getColumnIndex(Book.COLUMN_MARKING)));
                note.setBinding(cursor.getString(cursor.getColumnIndex(Book.COLUMN_BINDING)));
                note.setLocation(cursor.getString(cursor.getColumnIndex(Book.COLUMN_LOCATION)));
                note.setLent_price(cursor.getString(cursor.getColumnIndex(Book.COLUMN_LENT_PRICE)));
                note.setBook_price(cursor.getString(cursor.getColumnIndex(Book.COLUMN_BOOK_PRICE)));
                note.setPaid_price(cursor.getString(cursor.getColumnIndex(Book.COLUMN_PAID_PRICE)));
                note.setQuantity(cursor.getString(cursor.getColumnIndex(Book.COLUMN_QUANTITY)));
                note.setImagePath(cursor.getString(cursor.getColumnIndex(Book.COLUMN_IMG_PATH)));
                note.setDateLent(cursor.getString(cursor.getColumnIndex(Book.COLUMN_DATE_LENT)));
                note.setDateReturned(cursor.getString(cursor.getColumnIndex(Book.COLUMN_DATE_RETURNED)));
                note.setCurrentTimestamp(cursor.getString(cursor.getColumnIndex(Book.COLUMN_CURRENT_DATE)));

                notes.add(note);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return notes;
    }

    public int getNotesCount() {
        String countQuery = "SELECT  * FROM " + Book.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();


        // return count
        return count;
    }

    public int updateNote(Book note) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Book.COLUMN_BOOK, note.getBook());
        values.put(Book.COLUMN_AUTHOR,note.getAuthor());
        values.put(Book.COLUMN_ISBN,note.getIsbn());
        values.put(Book.COLUMN_CONDITION,note.getCondition());
        values.put(Book.COLUMN_MARKING,note.getMarking());
        values.put(Book.COLUMN_BINDING,note.getBinding());
        values.put(Book.COLUMN_LOCATION,note.getLocation());
        values.put(Book.COLUMN_LENT_PRICE,note.getLent_price());
        values.put(Book.COLUMN_BOOK_PRICE,note.getBook_price());
        values.put(Book.COLUMN_PAID_PRICE,note.getPaid_price());
        values.put(Book.COLUMN_QUANTITY,note.getQuantity());
        values.put(Book.COLUMN_IMG_PATH,note.getImagePath());
        values.put(Book.COLUMN_DATE_LENT,note.getDateLent());
        values.put(Book.COLUMN_DATE_RETURNED,note.getDateReturned());
        values.put(Book.COLUMN_CURRENT_DATE,note.getCurrentTimestamp());

        // updating row
        return db.update(Book.TABLE_NAME, values, Book.COLUMN_ID + " = ?",
                new String[]{String.valueOf(note.getId())});
    }

    public int updateNote(String book, String author,String isbn,String condition,String marking,String binding,String location,String lent_price,String book_price,String paid_price,String quantity,String imagePath) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValue = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        contentValue.put(Book.COLUMN_BOOK, book);
        contentValue.put(Book.COLUMN_AUTHOR,author);
        contentValue.put(Book.COLUMN_ISBN,isbn);
        contentValue.put(Book.COLUMN_CONDITION,condition);
        contentValue.put(Book.COLUMN_MARKING,marking);
        contentValue.put(Book.COLUMN_BINDING,binding);
        contentValue.put(Book.COLUMN_LOCATION,location);
        contentValue.put(Book.COLUMN_LENT_PRICE,lent_price);
        contentValue.put(Book.COLUMN_BOOK_PRICE,book_price);
        contentValue.put(Book.COLUMN_PAID_PRICE,paid_price);
        contentValue.put(Book.COLUMN_QUANTITY,quantity);
        contentValue.put(Book.COLUMN_IMG_PATH,imagePath);

        Log.d("IMAGE_VAL",imagePath);

        return db.update(Book.TABLE_NAME,contentValue,Book.COLUMN_ISBN + " = " + isbn,null);
    }




    public void deleteNote(Book note) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Book.TABLE_NAME, Book.COLUMN_ID + " = ?",
                new String[]{String.valueOf(note.getId())});
        db.close();
    }

}
