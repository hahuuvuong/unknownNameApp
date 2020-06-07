package com.example.sach.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.sach.model.user;

public class UserDatabase {
    SQLiteDatabase database;
    dbhelper dbHelper;
    private static final String TAG = "UserDatabase";
    public UserDatabase(Context context) {
        dbHelper = new dbhelper(context);
        try{
            database = dbHelper.getWritableDatabase();
        } catch (SQLException e) {
            database = dbHelper.getReadableDatabase();
        }
    }
    public long addUser(user u){
        ContentValues values = new ContentValues();
        values.put(dbHelper.COT_TEN,u.getNickName());
        values.put(dbHelper.COT_USERNAME,u.getUserName());
        values.put(dbHelper.COT_PASS,u.getPassWord());
        return database.insert(dbHelper.
                TEN_BANG_USER, null, values);
    }

    public long updateUser(user u){

        String sql = "UPDATE User SET _password = '"+u.getPassWord()+"' WHERE _username = '"+u.getUserName()+"'";
        Log.e(TAG, "updateUser: "+sql );
        database.execSQL(sql);
        return 0;
    }

    public user findUserById(String id, String pass) {
        String sql = "SELECT * FROM User WHERE _username = '"+id+"'";
        Cursor cursor = database.rawQuery(sql,null);
        cursor.moveToFirst();
        if(cursor.getCount() == 0|| !pass.equals(cursor.getString(cursor.getColumnIndex("_password")))){
            cursor.close();
            return null;
        }
        user u =  new user();
        u.setNickName(cursor.getString(cursor.getColumnIndex("_hoten")));
        u.setUserName(cursor.getString(cursor.getColumnIndex("_username")));
        u.setPassWord(cursor.getString(cursor.getColumnIndex("_password")));
        cursor.close();
        return  u;
    }
    public int checkUserExist(String user){
        String sql = "SELECT * FROM User WHERE _username = '"+user+"'";
        Cursor cursor = database.rawQuery(sql,null);
        if(cursor.getCount() == 0){
            cursor.close();
            return 1;
        }
        else
            return 0;
    }
}
