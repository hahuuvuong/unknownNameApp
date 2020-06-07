package com.example.sach.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.sach.model.book;

import java.util.ArrayList;


public class FavoriteDatabase {
    SQLiteDatabase database;
    dbhelper dbHelper;
    private static final String TAG = "FavoriteDatabase";
    public FavoriteDatabase(Context context) {
        dbHelper = new dbhelper(context);
        try{
            database = dbHelper.getWritableDatabase();
        } catch (SQLException e) {
            database = dbHelper.getReadableDatabase();
        }
    }
    public long addFavorite(String userName, int idTruyen){
        ContentValues values = new ContentValues();
        values.put(dbHelper.COT_USERNAME,userName);
        values.put(dbHelper.COT_IDTRUYEN,idTruyen);
            return database.insert(dbHelper.
                    TEN_BANG_FAVORITE, null, values);
    }
    public long deleteFavorite(String userName, int idTruyen) {
        String sql = "DELETE FROM Favorite WHERE  _username = '"+userName+"'"+" AND _idtruyen = '"+idTruyen+"'";
        Log.e(TAG, "deleteFavorite: "+sql );
        database.execSQL(sql);
        return 0;
    }
    public int checkFavoriteExist(String user,int idTruyen){
        String sql = "SELECT * FROM Favorite WHERE _username = '"+user+"'"+" AND _idtruyen = "+idTruyen;;
        Cursor cursor = database.rawQuery(sql,null);
        if(cursor.getCount() == 0){
            cursor.close();
            return 1;
        }
        else
            cursor.close();
        return 0;
    }
    public ArrayList<book> layTatCaDuLieu(String userName) {
        String sql = "SELECT * FROM Favorite WHERE _username = '"+userName+"'";
        Cursor c = database.rawQuery(sql,null);
        ArrayList<book> h = new ArrayList<>();
        // Biến cot là khai báo danh sách các cột cần lấy.
        if (c != null ) {
            if  (c.moveToFirst()) {
                do {
                    int temp = c.getInt(c.getColumnIndex("_idtruyen"));
                    book b  = new book() ;
                    h.add(b.returnABookByID(temp));
                }while (c.moveToNext());
            }
        }
        c.close();
        return h;
    }
}
