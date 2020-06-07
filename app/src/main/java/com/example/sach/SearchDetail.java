package com.example.sach;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.sach.model.book;
import com.example.sach.model.font;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchDetail extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerViewDetailCategoryAdapter recyclerViewAdapter;
    ArrayList<book> rowsArrayList = new ArrayList<>();
    int amountOfBook;
    String jsonStr1;
    Intent intent;

    private static final String TAG = "SearchDetail";
    boolean isLoading = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_category_detail);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView = findViewById(R.id.recyclerViewDetailCategory);
        recyclerView.setLayoutManager(layoutManager2);
        intent = getIntent();
        getJsonString();
        amountOfBook = countBook();
        if(amountOfBook > 0 ){
            populateData();
            initAdapter();
            initScrollListener();
        }
        else{
            fragment_notfound simpleFragment = fragment_notfound.newInstance();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager
                    .beginTransaction();
            fragmentTransaction.add(R.id.fragment_container,
                    simpleFragment).addToBackStack(null).commit();
        }
    }
    private void populateData() {
        int i = 0;
        if(amountOfBook<10){
            while (i < amountOfBook) {
                rowsArrayList.add(makeBook(i));
                i++;
            }
        }
        else{
            while (i < 10) {
                rowsArrayList.add(makeBook(i));
                i++;
            }
        }
    }

    private void initAdapter() {

        recyclerViewAdapter = new RecyclerViewDetailCategoryAdapter(rowsArrayList,this);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    private void initScrollListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if(amountOfBook > rowsArrayList.size())
                {
                    if (!isLoading) {
                        if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == rowsArrayList.size() - 1) {
                            //bottom of list!
                            loadMore();
                            isLoading = true;
                        }
                    }
                }
            }
        });


    }

    private void loadMore() {
        rowsArrayList.add(null);
        recyclerViewAdapter.notifyItemInserted(rowsArrayList.size() - 1);


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                rowsArrayList.remove(rowsArrayList.size() - 1);
                int scrollPosition = rowsArrayList.size();
                recyclerViewAdapter.notifyItemRemoved(scrollPosition);
                int currentSize = scrollPosition;
                int nextLimit = currentSize + 10;

                while (currentSize - 1 < nextLimit && currentSize < amountOfBook) {
                    rowsArrayList.add(makeBook(currentSize));
                    currentSize++;
                }

                recyclerViewAdapter.notifyDataSetChanged();
                isLoading = false;
            }
        }, 2000);
    }
    private book makeBook(int i){
        Log.e(TAG, "Response from url: " + jsonStr1);
        if (jsonStr1 != null) {
            try {

                JSONArray jsonArray = new JSONArray(jsonStr1);
                // looping through All Contacts
                JSONObject c = jsonArray.getJSONObject(i);
                String TacGia = c.getString("TacGia");
                String TenTruyen = c.getString("TenTruyen");
                String mImage = c.getString("image");
                String TomTat = c.getString("TomTat");
                int idBook = c.getInt("idTruyen");

                book b = new book();
                b.setIdBook(idBook);
                b.setAuthor(TacGia);
                b.setName(TenTruyen);
                b.setImageURL(mImage);
                b.setIntro(TomTat);
                return b;
            } catch (final JSONException e) {
                Log.e(TAG, "Json parsing error: " + e.getMessage());
            }
        }
        return null;
    }
    private int countBook(){
       try{
           if(jsonStr1==null)
               return  0;
           JSONArray jsonArray = new JSONArray(jsonStr1);

           return jsonArray.length();
       }
        catch (JSONException e){
        }
       return 0;
    }
    private void getJsonString(){
        HttpHandler sh = new HttpHandler();
        String url1 = intent.getExtras().getString("query");
        Log.e(TAG, "getJsonString: "+url1 );
        jsonStr1 = sh.makeServiceCall(url1);
        return ;
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
