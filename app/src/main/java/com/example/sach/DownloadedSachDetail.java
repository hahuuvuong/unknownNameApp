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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class DownloadedSachDetail extends AppCompatActivity {
    private book mBook;
    private String jsonStr;
    private Intent intent;
    TextView mTacGia;
    TextView mTenTruyen;
    TextView mIntro;
    ImageView mImage;
    ImageView mfavorite;
    ImageView mDownload;
    ImageView mImageBlur;
    HistoryDatabase historyDatabase;
    FavoriteDatabase favoriteDatabase;
    SachDatabase sachDatabase;
    FrameLayout frameLayout;
    ListView listView;
    private ProgressDialog progressDialog;
    ArrayList<String> chapterName = new ArrayList<>();
    ArrayList<Integer> idChapter = new ArrayList<>();

    private static final String TAG = "SachDetail";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downloaded_sach_detail);
        historyDatabase = new HistoryDatabase(getApplicationContext());
        sachDatabase = new SachDatabase(getApplicationContext());
        favoriteDatabase = new FavoriteDatabase(getApplicationContext());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        intent = getIntent();
        jsonStr = sachDatabase.returnjsonStr(intent.getExtras().getInt("idBook"));
        Log.e(TAG, "onCreate: " + jsonStr );
        mBook = makeBook();
        setControl();
        setEvent();
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
                Intent intent = new Intent(getApplicationContext(),DownloadedChapterDetail.class);
                intent.putExtra("query", jsonStr);
                intent.putIntegerArrayListExtra("listIdChapter",idChapter);
                intent.putExtra("selectedItem",position);
                intent.putExtra("idTruyen",mBook.getIdBook());
                intent.putExtra("idChapter",idChapter.get(position));
                intent.putStringArrayListExtra("ListChapterName",chapterName);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                historyDatabase.updateInChapter(new history(user.Name,mBook.getIdBook(),idChapter.get(position)));
                getApplicationContext().startActivity(intent);
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
    }

    private book makeBook(){
        book b = sachDatabase.returnAbook(user.Name,intent.getExtras().getInt("idBook"));
        makeListChapter();
        return b;
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
                    Log.e(TAG, "makeListChapter: "+tenChapter );
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
}