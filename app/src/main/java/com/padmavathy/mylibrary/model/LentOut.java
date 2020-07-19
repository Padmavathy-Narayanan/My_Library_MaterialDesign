package com.padmavathy.mylibrary.model;

public class LentOut {
    public static final String TABLE_NAME = "book_lent_out_details";

    // Table columns
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_PERSON_NAME = "book";
    public static final String COLUMN_DATE = "author";
    public static final String COLUMN_ISBN= "isbn";

    private int id;
    private String personName;
    private String dateLent;
    private String isbn_lent;


    // Creating table query
    public static final String CREATE_TABLE = "create table " + TABLE_NAME + "(" + COLUMN_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_PERSON_NAME + " TEXT , " + COLUMN_DATE + " TEXT," + COLUMN_ISBN + " TEXT " + ");";

    public LentOut() {
    }

    public LentOut(int id, String personName, String dateLent, String isbn) {
        this.id = id;
        this.personName = personName;
        this.dateLent = dateLent;
        this.isbn_lent = isbn;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getDateLent() {
        return dateLent;
    }

    public void setDateLent(String dateLent) {
        this.dateLent = dateLent;
    }

    public String getIsbn() {
        return isbn_lent;
    }

    public void setIsbn(String isbn) {
        this.isbn_lent = isbn;
    }
}
