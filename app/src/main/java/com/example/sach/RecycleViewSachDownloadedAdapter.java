package com.example.sach;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sach.database.HistoryDatabase;
import com.example.sach.model.API;
import com.example.sach.model.book;
import com.example.sach.model.user;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RecycleViewSachDownloadedAdapter extends RecyclerView.Adapter<RecycleViewSachDownloadedAdapter.ViewHolder> {
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private String jsonStr;
    int midChapter;
    ArrayList<String> chapterName = new ArrayList<>();
    ArrayList<Integer> idChapter = new ArrayList<>();
    ArrayList<book> books = new ArrayList<book>();
    private Context mContext;
    private static final String TAG = "RecyclerViewHistoryAdap";
    HistoryDatabase historyDatabase;
    public RecycleViewSachDownloadedAdapter(ArrayList<book> books, Context mContext) {
        this.books = books;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public RecycleViewSachDownloadedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        historyDatabase = new HistoryDatabase(parent.getContext());

        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listtruyen_vertical, parent, false);
            return new RecycleViewSachDownloadedAdapter.ItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new RecycleViewSachDownloadedAdapter.LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewSachDownloadedAdapter.ViewHolder holder, final int position) {
        if (holder instanceof RecycleViewSachDownloadedAdapter.ItemViewHolder) {
            populateItemRows((RecycleViewSachDownloadedAdapter.ItemViewHolder) holder, position);
        } else if (holder instanceof RecycleViewSachDownloadedAdapter.LoadingViewHolder) {
            showLoadingView((RecycleViewSachDownloadedAdapter.LoadingViewHolder) holder, position);
        }
        if (holder.linearLayout != null) {
            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                        Intent intent = new Intent(mContext, DownloadedSachDetail.class);
                        Log.i(TAG, "onClick: " + books.get(position).getIdBook());
                        intent.putExtra("idBook", books.get(position).getIdBook());
                        mContext.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return books == null ? 0 : books.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView Name;
        TextView Author;
        TextView Description;
        LinearLayout linearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imageCategory);
            Name = itemView.findViewById(R.id.nameTruyen);
            Author = itemView.findViewById(R.id.nameAuthor);
            linearLayout = itemView.findViewById(R.id.layoutListTruyenCategory);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return books.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }


    private void showLoadingView(RecycleViewSachDownloadedAdapter.LoadingViewHolder viewHolder, int position) {
        //ProgressBar would be displayed

    }

    private void populateItemRows(RecycleViewSachDownloadedAdapter.ViewHolder viewHolder, int position) {
        viewHolder.Name.setText(books.get(position).getName());
        viewHolder.Author.setText(books.get(position).getAuthor());
        Glide.with(mContext)
                .asBitmap()
                .load(books.get(position).getImageURL())
                .into(viewHolder.image);

    }

    private class ItemViewHolder extends RecycleViewSachDownloadedAdapter.ViewHolder {

        ImageView image;
        TextView Name;
        TextView Author;
        TextView Description;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.imageCategory);
            Name = itemView.findViewById(R.id.nameTruyen);
            Author = itemView.findViewById(R.id.nameAuthor);
        }
    }

    private class LoadingViewHolder extends RecycleViewSachDownloadedAdapter.ViewHolder {

        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

}
