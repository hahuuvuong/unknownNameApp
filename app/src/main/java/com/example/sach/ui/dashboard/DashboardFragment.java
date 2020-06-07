package com.example.sach.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sach.R;
import com.example.sach.RecyclerViewHistoryAdapter;
import com.example.sach.database.HistoryDatabase;
import com.example.sach.model.book;
import com.example.sach.model.font;
import com.example.sach.model.user;

import java.util.ArrayList;

public class DashboardFragment extends Fragment {
    RecyclerViewHistoryAdapter recyclerViewAdapter;
    View root;
    ArrayList<book> arrayList;
    HistoryDatabase historyDatabase;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        font.setFontSize(getActivity());
        root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        historyDatabase = new HistoryDatabase(root.getContext());
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        RecyclerView recyclerView = root.findViewById(R.id.recyclerViewCategory1);
        recyclerView.setLayoutManager(layoutManager2);
        arrayList = historyDatabase.layTatCaDuLieu(user.Name);
        recyclerViewAdapter = new RecyclerViewHistoryAdapter(arrayList,root.getContext());
        recyclerView.setAdapter(recyclerViewAdapter);
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
        return root;
    }

}
