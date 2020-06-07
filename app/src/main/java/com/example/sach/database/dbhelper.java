package com.example.sach.database;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class dbhelper extends SQLiteOpenHelper {
    public static final String TEN_DATABASE = "DocSach";
    // Tên bảng
    public static final String TEN_BANG_USER = "User";
    // Bảng gồm 3 cột _id, _ten và _lop.
    public static final String COT_USERNAME = "_username";
    public static final String COT_TEN = "_hoten";
    public static final String COT_PASS = "_password";

    private static final String TAO_BANG_USER = ""
            + "create table IF NOT EXISTS " + TEN_BANG_USER + " ( "
            + COT_USERNAME + " text primary key ,"
            + COT_TEN + " text not null, "
            + COT_PASS + " text not null );";

    public static final String TEN_BANG_HISTORY= "History";
    // Bảng gồm 3 cột _id, _ten và _lop.
    public static final String COT_IDTRUYEN = "_idtruyen";
    public static final String COT_IDCHAPTER = "_idchapter";

    private static final String TAO_BANG_HISTORY= ""
            + "create table IF NOT EXISTS " + TEN_BANG_HISTORY + " ( "
            + COT_USERNAME + " text not null,"
            + COT_IDTRUYEN + " int not null, "
            + COT_IDCHAPTER + " int,"
    + " PRIMARY KEY ( _username, _idtruyen)) ; ";

    public static final String TEN_BANG_FAVORITE= "Favorite";
    // Bảng gồm 3 cột _id, _ten và _lop.

    private static final String TAO_BANG_FAVORITE= ""
            + "create table IF NOT EXISTS " + TEN_BANG_FAVORITE + " ( "
            + COT_USERNAME + " text not null,"
            + COT_IDTRUYEN + " int not null, "
            + " PRIMARY KEY ( _username, _idtruyen)) ; ";


    public static final String COT_IDCATEGORY = "_idcategory";
    public static final String COT_NAME = "_name";
    public static final String COT_AUTHOR = "_author";
    public static final String COT_INTRO = "_intro";
    public static final String COT_IMAGEURL = "_imageurl";
    public static final String COT_IMAGEDRAWABLE = "_imagedrawable";

    public static final String TEN_BANG_SACH= "Sach";
    private static final String TAO_BANG_SACH = ""
            + "create table IF NOT EXISTS " + TEN_BANG_SACH + " ( "
            + COT_USERNAME + " text not null,"
            + COT_IDTRUYEN + " int not null, "
            + COT_IDCATEGORY + " text , "
            + COT_NAME + " text not null, "
            + COT_AUTHOR + " text not null, "
            + COT_INTRO + " text not null, "
            + COT_IMAGEDRAWABLE + " text , "
            + COT_IMAGEURL + " text not null ,"
            + " PRIMARY KEY ( _username, _idtruyen)) ; ";

    public static final String COT_CHAPTERNAME= "_chaptername";
    public static final String COT_CHAPTERCONTENT = "_chaptercontent";

    public static final String TEN_BANG_CHAPTER= "Chapter";
    private static final String TAO_BANG_CHAPTER = ""
            + "create table IF NOT EXISTS " + TEN_BANG_CHAPTER + " ( "
            + COT_IDTRUYEN + " int primary key, "
            + COT_CHAPTERCONTENT + " text not null) ; ";


    public dbhelper(@Nullable Context context) {
        super(context, TEN_DATABASE, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TAO_BANG_HISTORY);
        db.execSQL(TAO_BANG_USER);
        db.execSQL(TAO_BANG_FAVORITE);
        db.execSQL(TAO_BANG_SACH);
        db.execSQL(TAO_BANG_CHAPTER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
