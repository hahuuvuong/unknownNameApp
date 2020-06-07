package com.example.sach.model;

import android.util.Log;

import com.example.sach.HttpHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class book {
    private int idBook;
    private String idCategory;
    private String name;
    private String author;
    private String intro;
    private String imageURL;
    private ArrayList<String> tenChapter;
    private ArrayList<Integer> idChapter;
    private static final String TAG = "book";
    public ArrayList<String>getTenChapter() {
        return tenChapter;
    }

    public void setTenChapter(ArrayList<String>tenChapter) {
        this.tenChapter = tenChapter;
    }

    public ArrayList<Integer>  getIdChapter() {
        return idChapter;
    }

    public void setIdChapter(ArrayList<Integer>  idChapter) {
        this.idChapter = idChapter;
    }

    public int getIdBook() {
        return idBook;
    }

    public void setIdBook(int idBook) {
        this.idBook = idBook;
    }

    public String getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(String idCategory) {
        this.idCategory = idCategory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public book(int idBook, String idCategory, String name, String author, String intro, String imageURL) {
        this.idBook = idBook;
        this.idCategory = idCategory;
        this.name = name;
        this.author = author;
        this.intro = intro;
        this.imageURL = imageURL;
    }

    public book() {
    }

    public static book returnABookByID(int id){
        HttpHandler sh = new HttpHandler();
        String url = API.URL +"Sach?idSach=" +id;
        Log.e(TAG, "getJsonString: "+url );
        String jsonStr = sh.makeServiceCall(url);
        if (jsonStr != null) {
            try {
                JSONArray jsonArray = new JSONArray(jsonStr);
                // looping through All Contacts

                return returnBook(jsonArray,0);
            } catch (final JSONException e) {
                Log.e(TAG, "Json parsing error: " + e.getMessage());
            }
        }
        return null;
    }

    public static book returnBook(JSONArray jsonArray, int i) throws JSONException {
        JSONObject c = jsonArray.getJSONObject(i);
        String TacGia = c.getString("TacGia");
        String TenTruyen = c.getString("TenTruyen");
        String mImage = c.getString("image");
        int idBook = c.getInt("idTruyen");
        String TomTat = c.getString("TomTat");
        book b = new book();
        b.setIdBook(idBook);
        b.setAuthor(TacGia);
        b.setName(TenTruyen);
        b.setImageURL(mImage);
        b.setIntro(TomTat);
        return b;
    }
}
