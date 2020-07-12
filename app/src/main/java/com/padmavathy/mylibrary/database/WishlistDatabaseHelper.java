package com.padmavathy.mylibrary.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.padmavathy.mylibrary.model.Wishlist;

import java.util.ArrayList;
import java.util.List;

public class WishlistDatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "BookWishlist_db";


    public WishlistDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        // create notes table
        db.execSQL(Wishlist.CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + Wishlist.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    public long insertWishlist(String note,String personNmae,String imgPath,String DateBorr,String buyLink) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(Wishlist.COLUMN_Title, note);
        values.put(Wishlist.COLUMN_PersonName, personNmae);
        values.put(Wishlist.COLUMN_IMG_PATH,imgPath);
        values.put(Wishlist.COLUMN_ISBN, DateBorr);
        values.put(Wishlist.COLUMN_BUY,buyLink);

        // insert row
        long id = db.insert(Wishlist.TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    public Wishlist getWishlist(long id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Wishlist.TABLE_NAME,
                new String[]{Wishlist.COLUMN_ID, Wishlist.COLUMN_Title, Wishlist.COLUMN_PersonName,Wishlist.COLUMN_IMG_PATH,Wishlist.COLUMN_ISBN,Wishlist.COLUMN_BUY},
                Wishlist.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare note object
        Wishlist note = new Wishlist(cursor.getInt(cursor.getColumnIndex(Wishlist.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(Wishlist.COLUMN_Title)),
                cursor.getString(cursor.getColumnIndex(Wishlist.COLUMN_PersonName)),
                cursor.getString(cursor.getColumnIndex(Wishlist.COLUMN_IMG_PATH)),
                cursor.getString(cursor.getColumnIndex(Wishlist.COLUMN_ISBN)),
                cursor.getString(cursor.getColumnIndex(Wishlist.COLUMN_BUY)));
        // close the db connection
        cursor.close();

        return note;
    }

    public List<Wishlist> getAllWishlists() {
        List<Wishlist> notes = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + Wishlist.TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Wishlist note = new Wishlist();
                note.setId(cursor.getInt(cursor.getColumnIndex(Wishlist.COLUMN_ID)));
                note.setTitle(cursor.getString(cursor.getColumnIndex(Wishlist.COLUMN_Title)));
                note.setPersonNmae(cursor.getString(cursor.getColumnIndex(Wishlist.COLUMN_PersonName)));
                note.setImgPath(cursor.getString(cursor.getColumnIndex(Wishlist.COLUMN_IMG_PATH)));
                note.setIsbn(cursor.getString(cursor.getColumnIndex(Wishlist.COLUMN_ISBN)));
                note.setBuyLink(cursor.getString(cursor.getColumnIndex(Wishlist.COLUMN_BUY)));
                notes.add(note);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        for(Wishlist b:notes){
            Log.d("DB_BORROW",b.getTitle());
        }
        return notes;
    }

    public int getWishlistsCount() {
        String countQuery = "SELECT  * FROM " + Wishlist.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();


        // return count
        return count;
    }

    public int updateWishlist(Wishlist note) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Wishlist.COLUMN_Title, note.getTitle());

        // updating row
        return db.update(Wishlist.TABLE_NAME, values, Wishlist.COLUMN_ID + " = ?",
                new String[]{String.valueOf(note.getId())});
    }

    public void deleteWishlist(Wishlist note) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Wishlist.TABLE_NAME, Wishlist.COLUMN_ID + " = ?",
                new String[]{String.valueOf(note.getId())});
        db.close();
    }
}
