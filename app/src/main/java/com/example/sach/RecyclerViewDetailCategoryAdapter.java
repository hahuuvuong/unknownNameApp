package com.example.sach;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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
import com.example.sach.model.book;
import com.example.sach.model.history;
import com.example.sach.model.user;

import java.util.ArrayList;

public class RecyclerViewDetailCategoryAdapter extends RecyclerView.Adapter<RecyclerViewDetailCategoryAdapter.ViewHolder> {
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private int tempt;
    ArrayList<book> books = new ArrayList<book>();
    private Context mContext;
    private static final String TAG = "RecyclerViewDetailCateg";
    private ProgressDialog progressDialog;
    private HistoryDatabase historyDatabase;
    public RecyclerViewDetailCategoryAdapter(ArrayList<book> books, Context mContext) {
        this.books = books;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        historyDatabase = new HistoryDatabase(mContext);
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
        if(holder.linearLayout != null) {
            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tempt= position;
                    progressDialog = new ProgressDialog(mContext);
                    progressDialog.setTitle(mContext.getString(R.string.loading));
                    progressDialog.setMessage(mContext.getString(R.string.waiting));
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    new loading().execute();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return books == null ? 0 : books.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder {
        ImageView image ;
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


    private void showLoadingView(LoadingViewHolder viewHolder, int position) {
        //ProgressBar would be displayed

    }

    private void populateItemRows(ViewHolder viewHolder, int position) {
        viewHolder.Name.setText(books.get(position).getName());
        viewHolder.Author.setText(books.get(position).getAuthor());
        Glide.with(mContext)
                .asBitmap()
                .load(books.get(position).getImageURL())
                .into(viewHolder.image);

    }
    private class ItemViewHolder extends ViewHolder {

        ImageView image  ;
        TextView Name  ;
        TextView Author ;
        TextView Description ;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.imageCategory);
            Name = itemView.findViewById(R.id.nameTruyen);
            Author = itemView.findViewById(R.id.nameAuthor);
        }
    }
    private class LoadingViewHolder extends ViewHolder {

        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }
    class loading extends AsyncTask<Void, Void, Void>
    {
        // AlertDialog dialog=new SpotsDialog(getContext(), R.style.CustomLoadingDialog);

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            //dialog.show();

        }

        @Override
        protected void onPostExecute(Void result)
        {
            super.onPostExecute(result);
//            if(dialog.isShowing())
//            {
//                dialog.hide();
//            }
            if(progressDialog.isShowing())
            {
                progressDialog.dismiss();
            }
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            // Perform your task Here and then call intent
            historyDatabase.addHistoryOriginal(new history( user.Name,books.get(tempt).getIdBook(),-1));
            Intent intent = new Intent(mContext, SachDetail.class);
            Log.i(TAG, "onClick: " + books.get(tempt).getIdBook());
            intent.putExtra("idBook", books.get(tempt).getIdBook());
            mContext.startActivity(intent);
            return null;
        }
    }
}
