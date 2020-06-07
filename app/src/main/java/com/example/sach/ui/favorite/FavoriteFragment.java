package com.example.sach.ui.favorite;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sach.R;
import com.example.sach.RecyclerViewHistoryAdapter;
import com.example.sach.database.FavoriteDatabase;
import com.example.sach.database.HistoryDatabase;
import com.example.sach.model.book;
import com.example.sach.model.font;
import com.example.sach.model.user;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteFragment extends Fragment {

    RecyclerViewHistoryAdapter recyclerViewAdapter;
    View root;
    ArrayList<book> arrayList;
    FavoriteDatabase favoriteDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        font.setFontSize(getActivity());
        root = inflater.inflate(R.layout.fragment_favorite, container, false);
        favoriteDatabase = new FavoriteDatabase(root.getContext());
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        RecyclerView recyclerView = root.findViewById(R.id.recyclerViewCategory2);
        recyclerView.setLayoutManager(layoutManager2);
        arrayList = favoriteDatabase.layTatCaDuLieu(user.Name);
        recyclerViewAdapter = new RecyclerViewHistoryAdapter(arrayList,root.getContext());
        recyclerView.setAdapter(recyclerViewAdapter);
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
        return root;
    }
}
