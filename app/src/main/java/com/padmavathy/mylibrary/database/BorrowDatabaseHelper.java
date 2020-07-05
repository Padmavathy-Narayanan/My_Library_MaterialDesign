package com.padmavathy.mylibrary.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.padmavathy.mylibrary.model.BorrowBook;

import java.util.ArrayList;
import java.util.List;

public class BorrowDatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "Borrow_db";


    public BorrowDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        // create notes table
        db.execSQL(BorrowBook.CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + BorrowBook.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    public long insertBorrow(String note,String personNmae,String imgPath,String DateBorr,String dateRet) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(BorrowBook.COLUMN_Title, note);
        values.put(BorrowBook.COLUMN_PersonName, personNmae);
        values.put(BorrowBook.COLUMN_IMG_PATH,imgPath);
        values.put(BorrowBook.COLUMN_DATE_BoR, DateBorr);
        values.put(BorrowBook.COLUMN_DATE_RET, dateRet);

        // insert row
        long id = db.insert(BorrowBook.TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    public BorrowBook getBorrow(long id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(BorrowBook.TABLE_NAME,
                new String[]{BorrowBook.COLUMN_ID, BorrowBook.COLUMN_Title, BorrowBook.COLUMN_PersonName,BorrowBook.COLUMN_IMG_PATH,BorrowBook.COLUMN_DATE_BoR,BorrowBook.COLUMN_DATE_RET},
                BorrowBook.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare note object
        BorrowBook note = new BorrowBook(cursor.getInt(cursor.getColumnIndex(BorrowBook.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(BorrowBook.COLUMN_Title)),
                cursor.getString(cursor.getColumnIndex(BorrowBook.COLUMN_PersonName)),
                cursor.getString(cursor.getColumnIndex(BorrowBook.COLUMN_IMG_PATH)),
                cursor.getString(cursor.getColumnIndex(BorrowBook.COLUMN_DATE_BoR)),
                cursor.getString(cursor.getColumnIndex(BorrowBook.COLUMN_DATE_BoR)));

        // close the db connection
        cursor.close();

        return note;
    }

    public List<BorrowBook> getAllBorrows() {
        List<BorrowBook> notes = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + BorrowBook.TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                BorrowBook note = new BorrowBook();
                note.setId(cursor.getInt(cursor.getColumnIndex(BorrowBook.COLUMN_ID)));
                note.setTitle(cursor.getString(cursor.getColumnIndex(BorrowBook.COLUMN_Title)));
                note.setPersonNmae(cursor.getString(cursor.getColumnIndex(BorrowBook.COLUMN_PersonName)));
                note.setImgPath(cursor.getString(cursor.getColumnIndex(BorrowBook.COLUMN_IMG_PATH)));
                note.setDateBor(cursor.getString(cursor.getColumnIndex(BorrowBook.COLUMN_DATE_BoR)));
                note.setDateRet(cursor.getString(cursor.getColumnIndex(BorrowBook.COLUMN_DATE_RET)));
                notes.add(note);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        for(BorrowBook b:notes){
            Log.d("DB_BORROW",b.getTitle());
        }
        return notes;
    }

    public int getBorrowsCount() {
        String countQuery = "SELECT  * FROM " + BorrowBook.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();


        // return count
        return count;
    }

    public int updateBorrow(BorrowBook note) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(BorrowBook.COLUMN_Title, note.getTitle());

        // updating row
        return db.update(BorrowBook.TABLE_NAME, values, BorrowBook.COLUMN_ID + " = ?",
                new String[]{String.valueOf(note.getId())});
    }

    public void deleteBorrow(BorrowBook note) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(BorrowBook.TABLE_NAME, BorrowBook.COLUMN_ID + " = ?",
                new String[]{String.valueOf(note.getId())});
        db.close();
    }
}
