package com.padmavathy.mylibrary.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.padmavathy.mylibrary.model.LentOut;

import java.util.ArrayList;
import java.util.List;

public class LentOutDatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "BookLentOut_db";


    public LentOutDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        // create notes table
        db.execSQL(LentOut.CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + LentOut.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    public long insertLentOut(String isbn,String personNmae,String DateLent) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(LentOut.COLUMN_ISBN, isbn);
        values.put(LentOut.COLUMN_PERSON_NAME, personNmae);
        values.put(LentOut.COLUMN_DATE,DateLent);

        // insert row
        long id = db.insert(LentOut.TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    public LentOut getLentOut(long id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(LentOut.TABLE_NAME,
                new String[]{LentOut.COLUMN_ISBN, LentOut.COLUMN_PERSON_NAME,LentOut.COLUMN_DATE},
                LentOut.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare note object
        LentOut note = new LentOut(cursor.getInt(cursor.getColumnIndex(LentOut.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(LentOut.COLUMN_ISBN)),
                cursor.getString(cursor.getColumnIndex(LentOut.COLUMN_PERSON_NAME)),
                cursor.getString(cursor.getColumnIndex(LentOut.COLUMN_DATE)));
        // close the db connection
        cursor.close();

        return note;
    }

    public List<LentOut> getAllLentOuts() {
        List<LentOut> notes = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + LentOut.TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                LentOut note = new LentOut();
                note.setId(cursor.getInt(cursor.getColumnIndex(LentOut.COLUMN_ID)));
                note.setIsbn(cursor.getString(cursor.getColumnIndex(LentOut.COLUMN_ISBN)));
                note.setPersonName(cursor.getString(cursor.getColumnIndex(LentOut.COLUMN_PERSON_NAME)));
                note.setDateLent(cursor.getString(cursor.getColumnIndex(LentOut.COLUMN_DATE)));
                notes.add(note);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        for(LentOut b:notes){
            Log.d("DB_LENT_IN",b.getIsbn());
        }
        return notes;
    }

    public int getLentOutsCount() {
        String countQuery = "SELECT  * FROM " + LentOut.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();


        // return count
        return count;
    }

    public int updateLentOut(LentOut note) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(LentOut.COLUMN_PERSON_NAME, note.getPersonName());

        // updating row
        return db.update(LentOut.TABLE_NAME, values, LentOut.COLUMN_ID + " = ?",
                new String[]{String.valueOf(note.getId())});
    }

    public void deleteLentOut(LentOut note) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(LentOut.TABLE_NAME, LentOut.COLUMN_ID + " = ?",
                new String[]{String.valueOf(note.getId())});
        db.close();
    }
}
