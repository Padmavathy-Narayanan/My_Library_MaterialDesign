package com.padmavathy.mylibrary.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Book {
    public static final String TABLE_NAME = "book_details";

    // Table columns
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_BOOK = "book";
    public static final String COLUMN_AUTHOR = "author";
    public static final String COLUMN_ISBN= "isbn";
    public static final String COLUMN_CONDITION = "condition";
    public static final String COLUMN_MARKING = "marking";
    public static final String COLUMN_BINDING = "binding";
    public static final String COLUMN_LOCATION = "location";
    public static final String COLUMN_LENT_PRICE = "lent_price";
    public static final String COLUMN_BOOK_PRICE = "book_price";
    public static final String COLUMN_PAID_PRICE = "paid_price";
    public static final String COLUMN_QUANTITY = "quantity";
    public static final String COLUMN_IMG_PATH = "img_path";

    private int id;
    private String book;
    private String author;
    private String isbn;
    private String condition;
    private String marking;
    private String binding;
    private String location;
    private String lent_price;
    private String book_price;
    private String paid_price;
    private String quantity;
    private String img_path;


    // Creating table query
    public static final String CREATE_TABLE = "create table " + TABLE_NAME + "(" + COLUMN_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_BOOK + " TEXT NOT NULL, " + COLUMN_AUTHOR + " TEXT," + COLUMN_ISBN + " TEXT, " + COLUMN_CONDITION + " TEXT, " + COLUMN_MARKING + " TEXT, " +  COLUMN_BINDING +" TEXT, " + COLUMN_LOCATION +" TEXT," + COLUMN_LENT_PRICE + " TEXT, " + COLUMN_BOOK_PRICE + " TEXT, " + COLUMN_PAID_PRICE + " TEXT, "+ COLUMN_QUANTITY + " TEXT, " +  COLUMN_IMG_PATH + " TEXT " + ");";

    public Book() {
    }


    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Book(int id, String book, String author, String isbn, String condition, String marking, String binding, String location, String lent_price, String book_price, String paid_price, String quantity,String img_path) {
        this.id = id;
        this.book = book;
        this.author = author;
        this.isbn = isbn;
        this.condition = condition;
        this.marking = marking;
        this.binding = binding;
        this.location = location;
        this.lent_price = lent_price;
        this.book_price = book_price;
        this.paid_price = paid_price;
        this.quantity = quantity;
        this.img_path = img_path;
    }

    public String getImagePath(){
        return img_path;
    }

    public void setImagePath(String imgPath){
        this.img_path = imgPath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBook() {
        return book;
    }

    public void setBook(String book) {
        this.book = book;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getMarking() {
        return marking;
    }

    public void setMarking(String marking) {
        this.marking = marking;
    }

    public String getBinding() {
        return binding;
    }

    public void setBinding(String binding) {
        this.binding = binding;
    }

    public String getLent_price() {
        return lent_price;
    }

    public void setLent_price(String lent_price) {
        this.lent_price = lent_price;
    }

    public String getBook_price() {
        return book_price;
    }

    public void setBook_price(String book_price) {
        this.book_price = book_price;
    }

    public String getPaid_price() {
        return paid_price;
    }

    public void setPaid_price(String paid_price) {
        this.paid_price = paid_price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

}
