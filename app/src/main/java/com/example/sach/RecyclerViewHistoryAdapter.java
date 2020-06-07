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
import com.example.sach.model.history;
import com.example.sach.model.user;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RecyclerViewHistoryAdapter extends RecyclerView.Adapter<RecyclerViewHistoryAdapter.ViewHolder> {
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
    public RecyclerViewHistoryAdapter(ArrayList<book> books, Context mContext) {
        this.books = books;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        historyDatabase = new HistoryDatabase(parent.getContext());

        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listtruyen_vertical, parent, false);
            return new ItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        if (holder instanceof ItemViewHolder) {
            populateItemRows((ItemViewHolder) holder, position);
        } else if (holder instanceof LoadingViewHolder) {
            showLoadingView((LoadingViewHolder) holder, position);
        }
        if (holder.linearLayout != null) {
            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    midChapter = historyDatabase.checkFirstTime(user.Name,books.get(position).getIdBook());
                    if(midChapter==2){
                        Intent intent = new Intent(mContext, SachDetail.class);
                        Log.i(TAG, "onClick: " + books.get(position).getIdBook());
                        intent.putExtra("idBook", books.get(position).getIdBook());
                        mContext.startActivity(intent);
                    }
                    else{
                        jsonStr = getJsonString(API.URL+"Chapter?idSach=" +books.get(position).getIdBook());
                        makeListChapter();
                        Intent intent = new Intent(mContext,ChapterDetail.class);
                        intent.putExtra("query", API.URL +"chapter?idChapter=" + midChapter);
                        intent.putIntegerArrayListExtra("listIdChapter",idChapter);
                        intent.putExtra("selectedItem",returnPosition());
                        intent.putExtra("idTruyen",books.get(position).getIdBook());
                        intent.putStringArrayListExtra("ListChapterName",chapterName);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        mContext.startActivity(intent);
                    }
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


    private void showLoadingView(RecyclerViewHistoryAdapter.LoadingViewHolder viewHolder, int position) {
        //ProgressBar would be displayed

    }

    private void populateItemRows(RecyclerViewHistoryAdapter.ViewHolder viewHolder, int position) {
        viewHolder.Name.setText(books.get(position).getName());
        viewHolder.Author.setText(books.get(position).getAuthor());
        Glide.with(mContext)
                .asBitmap()
                .load(books.get(position).getImageURL())
                .into(viewHolder.image);

    }

    private class ItemViewHolder extends RecyclerViewHistoryAdapter.ViewHolder {

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

    private class LoadingViewHolder extends RecyclerViewHistoryAdapter.ViewHolder {

        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }
    public String getJsonString(String mURL){
        HttpHandler sh = new HttpHandler();
        Log.e(TAG, "getJsonString: " +mURL);
        String jsonStr = sh.makeServiceCall(mURL);
        return jsonStr ;
    }
    private void makeListChapter(){
        Log.e(TAG, "Response from url: " + jsonStr);
        chapterName.clear();
        idChapter.clear();
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
    private int returnPosition(){
        for(int i = 0; i< idChapter.size(); i++){
            if(midChapter == idChapter.get(i)){
                return i;
            }
        }
        return 0;
    }
}
