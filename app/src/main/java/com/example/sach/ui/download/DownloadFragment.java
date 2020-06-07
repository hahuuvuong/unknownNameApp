package com.example.sach.ui.download;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sach.R;
import com.example.sach.RecycleViewSachDownloadedAdapter;
import com.example.sach.RecyclerViewHistoryAdapter;
import com.example.sach.database.FavoriteDatabase;
import com.example.sach.database.SachDatabase;
import com.example.sach.model.book;
import com.example.sach.model.font;
import com.example.sach.model.user;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class DownloadFragment extends Fragment {

    RecycleViewSachDownloadedAdapter recyclerViewAdapter;
    View root;
    ArrayList<book> arrayList;
    SachDatabase sachDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        font.setFontSize(getActivity());
        root = inflater.inflate(R.layout.fragment_download, container, false);
        sachDatabase = new SachDatabase(root.getContext());
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        RecyclerView recyclerView = root.findViewById(R.id.recyclerViewCategory3);
        recyclerView.setLayoutManager(layoutManager2);
        arrayList = sachDatabase.layTatCaDuLieu(user.Name);
        recyclerViewAdapter = new RecycleViewSachDownloadedAdapter(arrayList,root.getContext());
        recyclerView.setAdapter(recyclerViewAdapter);
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
        return root;
    }
}
