package com.padmavathy.mylibrary.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.padmavathy.mylibrary.model.LentIn;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class LentInDatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "BookLentin_db";


    public LentInDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        // create notes table
        db.execSQL(LentIn.CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + LentIn.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    public long insertLentIn(String isbn,String personNmae,String DateLent,String lent) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(LentIn.COLUMN_ISBN, isbn);
        values.put(LentIn.COLUMN_PERSON_NAME, personNmae);
        values.put(LentIn.COLUMN_DATE,DateLent);
        values.put(LentIn.COLUMN_LENT_OPT,lent);

        // insert row
        long id = db.insert(LentIn.TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    public Collection<? extends LentIn> getLentIn(String id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(LentIn.TABLE_NAME,
                new String[]{LentIn.COLUMN_ISBN, LentIn.COLUMN_PERSON_NAME, LentIn.COLUMN_DATE, LentIn.COLUMN_LENT_OPT},
                LentIn.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        LentIn note = new LentIn();

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    // prepare note object
                    note = new LentIn(cursor.getInt(cursor.getColumnIndex(LentIn.COLUMN_ID)),
                            cursor.getString(cursor.getColumnIndex(LentIn.COLUMN_ISBN)),
                            cursor.getString(cursor.getColumnIndex(LentIn.COLUMN_PERSON_NAME)),
                            cursor.getString(cursor.getColumnIndex(LentIn.COLUMN_DATE)),
                            cursor.getString(cursor.getColumnIndex(LentIn.COLUMN_LENT_OPT)));
                } while (cursor.moveToNext());
            }

            // close the db connection
            cursor.close();
        }
            return (Collection<? extends LentIn>) note;
    }

    public List<LentIn> getAllISBNLentIns(String isbn) {
        List<LentIn> notes = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + LentIn.TABLE_NAME +" WHERE "+LentIn.COLUMN_ISBN+"= "+isbn ;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                LentIn note = new LentIn();
                note.setId(cursor.getInt(cursor.getColumnIndex(LentIn.COLUMN_ID)));
                note.setIsbn(cursor.getString(cursor.getColumnIndex(LentIn.COLUMN_ISBN)));
                note.setPersonName(cursor.getString(cursor.getColumnIndex(LentIn.COLUMN_PERSON_NAME)));
                note.setDateLent(cursor.getString(cursor.getColumnIndex(LentIn.COLUMN_DATE)));
                note.setLent_in_out(cursor.getString(cursor.getColumnIndex(LentIn.COLUMN_LENT_OPT)));
                notes.add(note);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        for(LentIn b:notes){
            Log.d("DB_LENT_IN",b.getIsbn());
        }
        return notes;
    }

    public List<LentIn> getAllLentIns() {
        List<LentIn> notes = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + LentIn.TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                LentIn note = new LentIn();
                note.setId(cursor.getInt(cursor.getColumnIndex(LentIn.COLUMN_ID)));
                note.setIsbn(cursor.getString(cursor.getColumnIndex(LentIn.COLUMN_ISBN)));
                note.setPersonName(cursor.getString(cursor.getColumnIndex(LentIn.COLUMN_PERSON_NAME)));
                note.setDateLent(cursor.getString(cursor.getColumnIndex(LentIn.COLUMN_DATE)));
                note.setLent_in_out(cursor.getString(cursor.getColumnIndex(LentIn.COLUMN_LENT_OPT)));
                notes.add(note);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        for(LentIn b:notes){
            Log.d("DB_LENT_IN",b.getIsbn());
        }
        return notes;
    }

    public int getLentInsCount() {
        String countQuery = "SELECT  * FROM " + LentIn.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();


        // return count
        return count;
    }

    public int updateLentIn(LentIn note) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(LentIn.COLUMN_PERSON_NAME, note.getPersonName());

        // updating row
        return db.update(LentIn.TABLE_NAME, values, LentIn.COLUMN_ID + " = ?",
                new String[]{String.valueOf(note.getId())});
    }

    public void deleteLentIn(LentIn note) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(LentIn.TABLE_NAME, LentIn.COLUMN_ID + " = ?",
                new String[]{String.valueOf(note.getId())});
        db.close();
    }
}
