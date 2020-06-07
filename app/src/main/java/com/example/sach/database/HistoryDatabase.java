package com.example.sach.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.sach.model.book;
import com.example.sach.model.history;

import java.util.ArrayList;

public class HistoryDatabase {
    SQLiteDatabase database;
    dbhelper dbHelper;
    private static final String TAG = "HistoryDatabase";
    public HistoryDatabase(Context context) {
        dbHelper = new dbhelper(context);
        try{
            database = dbHelper.getWritableDatabase();
        } catch (SQLException e) {
            database = dbHelper.getReadableDatabase();
        }
    }
    public long addHistoryOriginal(history h){
        ContentValues values = new ContentValues();
        values.put(dbHelper.COT_USERNAME,h.getUserName());
        values.put(dbHelper.COT_IDTRUYEN,h.getIdTruyen());
        if(checkFirstTime(h.getUserName(),h.getIdTruyen()) ==0){
            values.put(dbHelper.COT_IDCHAPTER,h.getIdChapter());
            return database.insert(dbHelper.
                    TEN_BANG_HISTORY, null, values);
        }
        return 0;
    }

    public long updateInChapter(history h) {
        ContentValues values = new ContentValues();
        values.put(dbHelper.COT_USERNAME,h.getUserName());

        values.put(dbHelper.COT_IDTRUYEN,h.getIdTruyen());
        values.put(dbHelper.COT_IDCHAPTER,h.getIdChapter());

        return database.update(dbHelper
                        .TEN_BANG_HISTORY,
                        values,
                dbHelper.COT_USERNAME + " = '"
                        + h.getUserName() + "' AND "
                        + dbHelper.COT_IDTRUYEN + " = '"
                        + h.getIdTruyen()+"'", null);
    }
    public long deleteHistory(String userName) {
        String sql = "DELETE FROM History WHERE  _username = '"+userName+"'";
        Log.e(TAG, "deleteHistory: "+sql );
        database.execSQL(sql);
        return 0;
    }
    public ArrayList<book> layTatCaDuLieu(String userName) {
        String sql = "SELECT * FROM History WHERE _username = '"+userName+"'";
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
    public int checkFirstTime(String userName, int idTruyen){
        String sql = "SELECT * FROM History WHERE _username = '"+userName+"' AND _idtruyen = '" + idTruyen +"'";
        Cursor c = database.rawQuery(sql,null);
        if (c != null &&  c.moveToFirst()) {

            int temp = c.getInt(c.getColumnIndex("_idchapter"));
            if(temp == -1)
                return 2;
            else
                return temp;
        }
        else
            return 0;
    }
}