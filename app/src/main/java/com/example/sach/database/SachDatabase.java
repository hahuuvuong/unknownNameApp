package com.example.sach.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.sach.model.book;
import com.example.sach.model.chapter;

import java.util.ArrayList;

public class SachDatabase {
    SQLiteDatabase database;
    dbhelper dbHelper;
    private static final String TAG = "SachDatabase";
    public SachDatabase(Context context) {
        dbHelper = new dbhelper(context);
        try{
            database = dbHelper.getWritableDatabase();
        } catch (SQLException e) {
            database = dbHelper.getReadableDatabase();
        }
    }
    public long addSachToDatabase(book b,String userName){
        ContentValues values = new ContentValues();
        values.put(dbHelper.COT_USERNAME,userName);
        values.put(dbHelper.COT_IDTRUYEN,b.getIdBook());
        values.put(dbHelper.COT_IDCATEGORY,b.getIdCategory());
        values.put(dbHelper.COT_NAME,b.getName());
        values.put(dbHelper.COT_AUTHOR,b.getAuthor());
        values.put(dbHelper.COT_INTRO,b.getIntro());
        values.put(dbHelper.COT_IMAGEURL,b.getImageURL());

        return database.insert(dbHelper.
                    TEN_BANG_SACH, null, values);
    }
    public ArrayList<book> layTatCaDuLieu(String userName) {
        String sql = "SELECT * FROM Sach WHERE _username = '"+userName+"'";
        Cursor c = database.rawQuery(sql,null);
        ArrayList<book> h = new ArrayList<>();
        // Biến cot là khai báo danh sách các cột cần lấy.
        if (c != null ) {
            if  (c.moveToFirst()) {
                do {
                    int temp = c.getInt(c.getColumnIndex("_idtruyen"));
                    String idCategory = c.getString(c.getColumnIndex(dbHelper.COT_IDCATEGORY));
                    String name = c.getString(c.getColumnIndex(dbHelper.COT_NAME));
                    String author = c.getString(c.getColumnIndex(dbHelper.COT_AUTHOR));
                    String intro = c.getString(c.getColumnIndex(dbHelper.COT_INTRO));
                    String imageURL = c.getString(c.getColumnIndex(dbHelper.COT_IMAGEURL));
                    book b  = new book(temp,idCategory,name,author,intro,imageURL) ;
                    h.add(b);
                }while (c.moveToNext());
            }
        }
        c.close();
        return h;
    }
    public int checkFirstTime(String userName, int idTruyen){
        String sql = "SELECT * FROM Sach WHERE _username = '"+userName+"' AND _idtruyen = '" + idTruyen +"'";
        Log.e(TAG, "checkFirstTime: "+sql );
        Cursor c = database.rawQuery(sql,null);
        if (c.getCount() == 0) {
            return 1;
        }
        else
            return 0;
    }
    public book returnAbook(String userName, int idTruyen){
        String sql = "SELECT * FROM Sach WHERE _username = '"+userName+"' AND _idtruyen = '"+idTruyen+"'";
        Cursor c = database.rawQuery(sql,null);
        if( c != null && c.moveToFirst() ) {
            int temp = c.getInt(c.getColumnIndex("_idtruyen"));
            String idCategory = c.getString(c.getColumnIndex(dbHelper.COT_IDCATEGORY));
            String name = c.getString(c.getColumnIndex(dbHelper.COT_NAME));
            String author = c.getString(c.getColumnIndex(dbHelper.COT_AUTHOR));
            String intro = c.getString(c.getColumnIndex(dbHelper.COT_INTRO));
            String imageURL = c.getString(c.getColumnIndex(dbHelper.COT_IMAGEURL));
            book b = new book(temp, idCategory, name, author, intro, imageURL);
            return  b;
        }
        return  null;
    }

    public long addChapterToDatabase(String b, int idTruyen){
        ContentValues values = new ContentValues();
        values.put(dbHelper.COT_IDTRUYEN,idTruyen);
        values.put(dbHelper.COT_CHAPTERCONTENT,b);

        return database.insert(dbHelper.
                TEN_BANG_CHAPTER, null, values);
    }
    public String returnjsonStr( int idTruyen){
        String sql = "SELECT * FROM Chapter WHERE  _idtruyen = '"+idTruyen+"'";
        Cursor c = database.rawQuery(sql,null);
        String json;
        if( c != null && c.moveToFirst() ){
            json = c.getString(c.getColumnIndex(dbHelper.COT_CHAPTERCONTENT));
            c.close();
            return json;
        }
        return null;
    }
}
