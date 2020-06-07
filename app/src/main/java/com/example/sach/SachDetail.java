package com.example.sach;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.sach.database.FavoriteDatabase;
import com.example.sach.database.HistoryDatabase;
import com.example.sach.database.SachDatabase;
import com.example.sach.model.API;
import com.example.sach.model.book;
import com.example.sach.model.history;
import com.example.sach.model.user;
import com.like.LikeButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class SachDetail extends AppCompatActivity {
    private book mBook;
    private String jsonStr1;
    private String jsonStr;
    private Intent intent;
    TextView mTacGia;
    TextView mTenTruyen;
    TextView mIntro;
    ImageView mImage;
    ImageView mDownload;
    ImageView mImageBlur;
    HistoryDatabase historyDatabase;
    FavoriteDatabase favoriteDatabase;
    SachDatabase sachDatabase;
    FrameLayout frameLayout;
    ListView listView;
    LikeButton likeButton;
    private ProgressDialog progressDialog;
    ArrayList<String> chapterName = new ArrayList<>();
    ArrayList<Integer> idChapter = new ArrayList<>();

    private static final String TAG = "SachDetail";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sach_detail);
        historyDatabase = new HistoryDatabase(getApplicationContext());
        sachDatabase = new SachDatabase(getApplicationContext());
        favoriteDatabase = new FavoriteDatabase(getApplicationContext());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        intent = getIntent();
        jsonStr1 =  getJsonString(API.URL+"Sach?idSach=" + intent.getExtras().getInt("idBook"));
        jsonStr = getJsonString(API.URL+"Chapter?idSach=" +intent.getExtras().getInt("idBook"));
        mBook = makeBook();
        setControl();
        setEvent();
        checkHeart();
        checkDownload();
    }

    private void setEvent() {
        mTenTruyen.setText(mBook.getName());
        mTacGia.setText(mBook.getAuthor());
        mIntro.setText(mBook.getIntro());
        Glide.with(this)
                .asBitmap()
                .load(mBook.getImageURL())
                .into(mImage);

        Glide.with(this).load(mBook.getImageURL())
                .apply(RequestOptions.bitmapTransform(new BlurTransformation(25, 3)))
                .into(mImageBlur);

        String[] array = chapterName.toArray(new String[chapterName.size()]);

        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_list_item_1,array);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),ChapterDetail.class);
                intent.putExtra("query", API.URL +"chapter?idChapter=" + String.valueOf(idChapter.get(position)));
                intent.putIntegerArrayListExtra("listIdChapter",idChapter);
                intent.putExtra("selectedItem",position);
                intent.putExtra("idTruyen",mBook.getIdBook());
                intent.putStringArrayListExtra("ListChapterName",chapterName);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                historyDatabase.updateInChapter(new history(user.Name,mBook.getIdBook(),idChapter.get(position)));
                getApplicationContext().startActivity(intent);
            }
        });
        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkHeartClick();
            }
        });
        mDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkDownloadClick();
            }
        });
    }

    private void setControl() {
        mTenTruyen = findViewById(R.id.tenSachDetailBook);
        mTacGia = findViewById(R.id.tacGiaDetailBook);
        mIntro = findViewById(R.id.introDetailBook);
        mImage = findViewById(R.id.imageDetailBook);
        mImageBlur = findViewById(R.id.imageDetailBookBlur);
        frameLayout = findViewById(R.id.frameLayoutBookDetai);
        listView = findViewById(R.id.listViewChapter);
        mDownload = findViewById(R.id.downloadSach);
        likeButton = findViewById(R.id.star_button);
    }

    private book makeBook(){
        Log.e(TAG, "Response from url: " + jsonStr1);
        if (jsonStr1 != null) {
            try {

                JSONArray jsonArray = new JSONArray(jsonStr1);
                // looping through All Contacts

                book b = book.returnBook(jsonArray,0);

                makeListChapter();
                return b;
            } catch (final JSONException e) {
                Log.e(TAG, "Json parsing error: " + e.getMessage());
            }
        }
        return null;
    }

    public String getJsonString(String mURL){
        HttpHandler sh = new HttpHandler();
        Log.e(TAG, "getJsonString: " +mURL);
        String jsonStr = sh.makeServiceCall(mURL);
        return jsonStr ;
    }
    private void makeListChapter(){
        Log.e(TAG, "Response from url: " + jsonStr);

        if (jsonStr != null) {
            try {
                JSONArray jsonArray = new JSONArray(jsonStr);
                // looping through All Contacts
                int i = 0;
                while (i<jsonArray.length()) {
                    JSONObject c = jsonArray.getJSONObject(i);
                    String tenChapter = c.getString("TenChapter");
                    int id = c.getInt("idChapter");
                    chapterName.add(tenChapter);
                    idChapter.add(id);
                    i++;
                }
            } catch (final JSONException e) {
                Log.e(TAG, "Json parsing error: " + e.getMessage());
            }

        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
    private void checkHeart(){
        if(favoriteDatabase.checkFavoriteExist(user.Name,intent.getExtras().getInt("idBook"))==1){
            likeButton.setLiked(false);
        }
        else{
            likeButton.setLiked(true);
        }
    }
    private void checkDownload(){
        if(sachDatabase.checkFirstTime(user.Name,intent.getExtras().getInt("idBook"))==1){
            mDownload.setImageResource(R.drawable.ic_cloud_download_black_24dp);
        }
        else{
            mDownload.setImageResource(R.drawable.ic_cloud_done_black_24dp);
        }
    }
    private void checkHeartClick(){
        if(favoriteDatabase.checkFavoriteExist(user.Name,intent.getExtras().getInt("idBook"))==1){
            likeButton.setLiked(true);
            favoriteDatabase.addFavorite(user.Name,intent.getExtras().getInt("idBook"));
        }
        else{
            likeButton.setLiked(false);
            favoriteDatabase.deleteFavorite(user.Name,intent.getExtras().getInt("idBook"));
        }
    }
    private void checkDownloadClick(){
        if(favoriteDatabase.checkFavoriteExist(user.Name,intent.getExtras().getInt("idBook"))==1){
            sachDatabase.addSachToDatabase(book.returnABookByID(intent.getExtras().getInt("idBook")),user.Name);
            sachDatabase.addChapterToDatabase(jsonStr,intent.getExtras().getInt("idBook"));
            mDownload.setImageResource(R.drawable.ic_cloud_done_black_24dp);
        }
        else{
        }
    }
}
