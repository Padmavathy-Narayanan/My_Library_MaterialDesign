package com.padmavathy.mylibrary.model;

public class History_Borrow {
    public static final String TABLE_NAME = "history_borrow";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_Title = "note";
    public static final String COLUMN_PersonName = "timestamp";
    public static final String COLUMN_IMG_PATH = "img_path";
    public static final String COLUMN_DATE_BoR = "dateBorr";
    public static final String COLUMN_DATE_RET = "dateRet";

    private int id;
    private String title;
    private String personNmae;
    public String imgPath;
    public String dateBor;
    public String dateRet;

    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_Title + " TEXT,"
                    + COLUMN_PersonName + " TEXT,"
                    + COLUMN_IMG_PATH + " TEXT,"
                    + COLUMN_DATE_BoR + " TEXT,"
                    + COLUMN_DATE_RET + " TEXT " +  ");";

    public History_Borrow() {
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

    public String getDateBor() {
        return dateBor;
    }

    public void setDateBor(String dateBor) {
        this.dateBor = dateBor;
    }

    public String getDateRet() {
        return dateRet;
    }

    public void setDateRet(String dateRet) {
        this.dateRet = dateRet;
    }

    public History_Borrow(int id, String title, String personNmae, String imgPath, String dateBor, String dateRet) {
        this.id = id;
        this.title = title;
        this.personNmae = personNmae;
        this.imgPath = imgPath;
        this.dateBor = dateBor;
        this.dateRet = dateRet;
    }
}
