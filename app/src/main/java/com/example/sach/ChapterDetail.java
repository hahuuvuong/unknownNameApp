package com.example.sach;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.sach.database.HistoryDatabase;
import com.example.sach.model.API;
import com.example.sach.model.book;
import com.example.sach.model.chapter;
import com.example.sach.model.history;
import com.example.sach.model.user;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ChapterDetail extends AppCompatActivity {
    private static final String TAG = "ChapterDetail";
    private String jsonStr;
    private Intent intent;
    private Spinner spinner;
    private Spinner spinner1;
    private ImageView imageViewTopLeft;
    private ImageView imageViewTopRight;
    private ImageView imageViewBottomLeft;
    private ImageView imageViewBottomRight;
    private TextView tvChapterName;
    private TextView tvChapterContent;
    private ArrayList<Integer> idChapter;
    private ArrayList<String> chapterName;
    HistoryDatabase historyDatabase;
    int temp;
    int temp1;
    int check = 0;
    chapter ct;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        historyDatabase = new HistoryDatabase(getApplicationContext());
        intent = getIntent();
        jsonStr = getJsonString(intent.getExtras().getString("query"));
        idChapter = intent.getIntegerArrayListExtra("listIdChapter");
        chapterName = intent.getStringArrayListExtra("ListChapterName");
        ct = makeChapter();
        setControl();
        setEvent();
    }

    private void setEvent() {
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(getApplicationContext(),  R.layout.spinner_item, chapterName);
        adapter.setDropDownViewResource( R.layout.spinner_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(intent.getExtras().getInt("selectedItem"));
        spinner1.setAdapter(adapter);
        spinner1.setSelection(intent.getExtras().getInt("selectedItem"));
        tvChapterName.setText(ct.getChapterName());
        tvChapterContent.setText(ct.getChapterContext());
        if(intent.getExtras().getInt("selectedItem")==0){
            imageViewTopLeft.setVisibility(View.INVISIBLE);
            imageViewBottomLeft.setVisibility(View.INVISIBLE);
        }
        else if(intent.getExtras().getInt("selectedItem")==chapterName.size()-1){
            imageViewTopRight.setVisibility(View.INVISIBLE);
            imageViewBottomRight.setVisibility(View.INVISIBLE);
        }
        temp = intent.getExtras().getInt("selectedItem") - 1;
        temp1 = intent.getExtras().getInt("selectedItem") +1;
        imageViewTopLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startIntent(temp);
            }
        });
        imageViewTopRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startIntent(temp1);
            }
        });
        imageViewBottomLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startIntent(temp);
            }
        });
        imageViewBottomRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startIntent(temp1);
            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(++check >1 ){
                    startIntent(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(++check > 2 ){
                    startIntent(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setControl() {
        spinner = findViewById(R.id.SpinnerListChapter);
        spinner1 = findViewById(R.id.SpinnerListChapter1);
        tvChapterName = findViewById(R.id.TenChapter);
        tvChapterContent = findViewById(R.id.contextChapter);
        imageViewTopLeft = findViewById(R.id.imageViewTopLeft);
        imageViewTopRight = findViewById(R.id.imageViewTopRight);
        imageViewBottomLeft = findViewById(R.id.imageViewBottomLeft);
        imageViewBottomRight = findViewById(R.id.imageViewBottomRight);
    }


    public String getJsonString(String mURL){
        HttpHandler sh = new HttpHandler();
        Log.e(TAG, "getJsonString: " +mURL);
        String jsonStr = sh.makeServiceCall(mURL);
        return jsonStr ;
    }
    private chapter makeChapter(){
        Log.e(TAG, "Response from url: " + jsonStr);
        if (jsonStr != null) {
            try {

                // looping through All Contacts
                JSONObject c = new JSONObject(jsonStr);
                String TenChapter = c.getString("TenChapter");
                String NoiDung = c.getString("NoiDung");

                chapter b = new chapter();
                b.setChapterName(TenChapter);
                b.setChapterContext(NoiDung);
                return b;
            } catch (final JSONException e) {
                Log.e(TAG, "Json parsing error: " + e.getMessage());
            }
        }
        return null;
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish(); // or your code
    }
    public void startIntent(int id){
        int temp =intent.getExtras().getInt("idTruyen");
        historyDatabase.updateInChapter(new history(user.Name,temp,idChapter.get(id)));

        Intent intent = new Intent(getApplicationContext(),ChapterDetail.class);
        intent.putExtra("query", API.URL +"chapter?idChapter=" +idChapter.get(id));
        intent.putIntegerArrayListExtra("listIdChapter",idChapter);
        intent.putExtra("selectedItem",id);
        intent.putStringArrayListExtra("ListChapterName",chapterName);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(intent);
    }
}
