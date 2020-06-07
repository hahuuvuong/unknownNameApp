package com.example.sach.ui.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.sach.HttpHandler;
import com.example.sach.MainActivity;
import com.example.sach.R;
import com.example.sach.RecyclerViewTruyenHotAdapter;
import com.example.sach.ViewPagerAdapter;
import com.example.sach.model.API;
import com.example.sach.model.book;
import com.example.sach.RecyclerViewCategoryAdapter;
import com.example.sach.model.font;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    View root;
    ViewPager viewPager;
    private ArrayList<book> books = new ArrayList<book>();
    private ArrayList<book> books1 = new ArrayList<book>();
    book b;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        font.setFontSize(getActivity());
        loadLocale();
        if(((AppCompatActivity)getActivity()).getSupportActionBar().isShowing() == false){
            ((AppCompatActivity)getActivity()).getSupportActionBar().show();
        }
        root = inflater.inflate(R.layout.fragment_home, container, false);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        viewPager = root.findViewById(R.id.viewPager1);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getContext());
        viewPager.setAdapter(viewPagerAdapter);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new MyTimerTask(),2000,4000);
        getImage();
        return root;
    }
    private void getImage(){
        initBook();
        initRecyclerView();
    }
    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: ");

        // truyen moi nhat
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        RecyclerView recyclerView = root.findViewById(R.id.recyclerViewTruyenHot);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerViewTruyenHotAdapter adapter = new RecyclerViewTruyenHotAdapter(getActivity(),books,1);
        recyclerView.setAdapter(adapter);

        //truyen nhieu nguoi doc
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        RecyclerView recyclerView1 = root.findViewById(R.id.recyclerViewTruyenMoiNhat);
        recyclerView1.setLayoutManager(layoutManager1);
        RecyclerViewTruyenHotAdapter adapter1 = new RecyclerViewTruyenHotAdapter(getActivity(),books1,2);
        recyclerView1.setAdapter(adapter1);

        //list the loai
        String [] mName = {getContext().getString(R.string.adventure), getContext().getString(R.string.romance),getContext().getString(R.string.detective),getContext().getString(R.string.horror),getContext().getString(R.string.school),getContext().getString(R.string.fairytale),getContext().getString(R.string.learning_english)};
        int [] mImage = {R.drawable.man_elf,R.drawable.kiss_woman_man, R.drawable.man_detective,R.drawable.anxious_face_with_sweat,R.drawable.tuoi_hoc_tro,R.drawable.unicorn,R.drawable.united_kingdom};
        int [] mBackgroundColor = {0xFF03DAC5,0xFFCA0B90,0xFF56EC62, 0xFF172C53,0xFFE567FB,0xFFCB5B94,0xFFD0FB91};
        int [] idTheLoai = {1, 5, 12, 7, 3, 4, 16};
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        RecyclerView recyclerView2 = root.findViewById(R.id.recyclerViewCategory);
        recyclerView2.setLayoutManager(layoutManager2);
        RecyclerViewCategoryAdapter adapter2 = new RecyclerViewCategoryAdapter(mImage,mName,getActivity(),mBackgroundColor,idTheLoai);
        recyclerView2.setAdapter(adapter2);
    }
    private void initBook(){
        HttpHandler sh = new HttpHandler();
        String url = API.URL+"Sach?quantity=6";
        String jsonStr = sh.makeServiceCall(url);

        Log.e(TAG, "Response from url: " + jsonStr);
        if (jsonStr != null) {
            try {

                JSONArray jsonArray = new JSONArray(jsonStr);
                // looping through All Contacts
                for (int i = 0; i < jsonArray.length(); i++) {
                        book b1 ;
                        b1 = book.returnBook(jsonArray,i);
                    books.add(b1);
                }
            } catch (final JSONException e) {
                Log.e(TAG, "Json parsing error: " + e.getMessage());
            }
        }

        String url1 = API.URL+"Sach?quantity1=6";
        String jsonStr1 = sh.makeServiceCall(url1);

        Log.e(TAG, "Response from url: " + jsonStr1);
        if (jsonStr1 != null) {
            try {

                JSONArray jsonArray = new JSONArray(jsonStr1);
                // looping through All Contacts
                for (int i = 0; i < jsonArray.length(); i++) {

                    books1.add(book.returnBook(jsonArray,i));
                }
            } catch (final JSONException e) {
                Log.e(TAG, "Json parsing error: " + e.getMessage());
            }
        }
    }

    public class MyTimerTask extends TimerTask{

        @Override
        public void run() {
            if(getActivity() !=null)
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(viewPager.getCurrentItem() == 0){
                        viewPager.setCurrentItem(1);
                    }
                    else if (viewPager.getCurrentItem() == 1){
                        viewPager.setCurrentItem(2);
                    }
                    else
                        viewPager.setCurrentItem(0);
                }
            });
        }
    }
    public  void loadLocale(){
        SharedPreferences prefs = getActivity().getSharedPreferences("Settings",getContext().MODE_PRIVATE);
        String language = prefs.getString("my_lang","");
        setLocale(language);
    }
    private void setLocale(String en) {
        Locale locale = new Locale(en);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;

        getActivity().getBaseContext().getResources().updateConfiguration(config,getActivity().getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = this.getActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE).edit();
        editor.putString("my_lang",en);
        editor.apply();
    }
}
