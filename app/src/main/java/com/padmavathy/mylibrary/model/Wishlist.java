package com.padmavathy.mylibrary.model;

public class Wishlist {
    public static final String TABLE_NAME = "wishlist";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_Title = "title";
    public static final String COLUMN_PersonName = "author";
    public static final String COLUMN_IMG_PATH = "img_path";
    public static final String COLUMN_ISBN = "isbn";
    public static final String COLUMN_BUY =  "buy_link";

    private int id;
    private String title;
    private String personNmae;
    public String imgPath;
    public String isbn;
    public String buyLink;

    public String getBuyLink() {
        return buyLink;
    }

    public void setBuyLink(String buyLink) {
        this.buyLink = buyLink;
    }

    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_Title + " TEXT,"
                    + COLUMN_PersonName + " TEXT,"
                    + COLUMN_IMG_PATH + " TEXT,"
                    + COLUMN_ISBN + " TEXT,"
                    + COLUMN_BUY + " TEXT "
                    +");";

    public Wishlist() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPersonNmae() {
        return personNmae;
    }

    public void setPersonNmae(String personNmae) {
        this.personNmae = personNmae;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }


    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Wishlist(int id, String title, String personNmae, String imgPath, String isbn,String buyLink) {
        this.id = id;
        this.title = title;
        this.personNmae = personNmae;
        this.imgPath = imgPath;
        this.isbn = isbn;
        this.buyLink = buyLink;
    }
}



