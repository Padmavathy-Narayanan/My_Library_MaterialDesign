package com.padmavathy.mylibrary.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.padmavathy.mylibrary.model.History_Borrow;

import java.util.ArrayList;
import java.util.List;

public class HistoryDatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "History_Borrow_db";


    public HistoryDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        // create notes table
        db.execSQL(History_Borrow.CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + History_Borrow.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    public long insertHistory_Borrow(String note,String personNmae,String imgPath,String DateBorr,String dateRet) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(History_Borrow.COLUMN_Title, note);
        values.put(History_Borrow.COLUMN_PersonName, personNmae);
        values.put(History_Borrow.COLUMN_IMG_PATH,imgPath);
        values.put(History_Borrow.COLUMN_DATE_BoR, DateBorr);
        values.put(History_Borrow.COLUMN_DATE_RET, dateRet);

        // insert row
        long id = db.insert(History_Borrow.TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    public History_Borrow getHistory_Borrow(long id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(History_Borrow.TABLE_NAME,
                new String[]{History_Borrow.COLUMN_ID, History_Borrow.COLUMN_Title, History_Borrow.COLUMN_PersonName,History_Borrow.COLUMN_IMG_PATH,History_Borrow.COLUMN_DATE_BoR,History_Borrow.COLUMN_DATE_RET},
                History_Borrow.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare note object
        History_Borrow note = new History_Borrow(cursor.getInt(cursor.getColumnIndex(History_Borrow.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(History_Borrow.COLUMN_Title)),
                cursor.getString(cursor.getColumnIndex(History_Borrow.COLUMN_PersonName)),
                cursor.getString(cursor.getColumnIndex(History_Borrow.COLUMN_IMG_PATH)),
                cursor.getString(cursor.getColumnIndex(History_Borrow.COLUMN_DATE_BoR)),
                cursor.getString(cursor.getColumnIndex(History_Borrow.COLUMN_DATE_BoR)));

        // close the db connection
        cursor.close();

        return note;
    }

    public List<History_Borrow> getAllHistory_Borrows() {
        List<History_Borrow> notes = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + History_Borrow.TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                History_Borrow note = new History_Borrow();
                note.setId(cursor.getInt(cursor.getColumnIndex(History_Borrow.COLUMN_ID)));
                note.setTitle(cursor.getString(cursor.getColumnIndex(History_Borrow.COLUMN_Title)));
                note.setPersonNmae(cursor.getString(cursor.getColumnIndex(History_Borrow.COLUMN_PersonName)));
                note.setImgPath(cursor.getString(cursor.getColumnIndex(History_Borrow.COLUMN_IMG_PATH)));
                note.setDateBor(cursor.getString(cursor.getColumnIndex(History_Borrow.COLUMN_DATE_BoR)));
                note.setDateRet(cursor.getString(cursor.getColumnIndex(History_Borrow.COLUMN_DATE_RET)));
                notes.add(note);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        for(History_Borrow b:notes){
            Log.d("DB_BORROW",b.getTitle());
        }
        return notes;
    }

    public int getHistory_BorrowsCount() {
        String countQuery = "SELECT  * FROM " + History_Borrow.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();


        // return count
        return count;
    }

    public int updateHistory_Borrow(History_Borrow note) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(History_Borrow.COLUMN_Title, note.getTitle());

        // updating row
        return db.update(History_Borrow.TABLE_NAME, values, History_Borrow.COLUMN_ID + " = ?",
                new String[]{String.valueOf(note.getId())});
    }

    public void deleteHistory_Borrow(History_Borrow note) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(History_Borrow.TABLE_NAME, History_Borrow.COLUMN_ID + " = ?",
                new String[]{String.valueOf(note.getId())});
        db.close();
    }
}
