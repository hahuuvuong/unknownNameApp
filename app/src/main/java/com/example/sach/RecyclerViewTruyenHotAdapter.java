package com.example.sach;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sach.database.HistoryDatabase;
import com.example.sach.model.book;
import com.example.sach.model.history;
import com.example.sach.model.user;

import java.util.ArrayList;

import dmax.dialog.SpotsDialog;

public class RecyclerViewTruyenHotAdapter extends RecyclerView.Adapter<RecyclerViewTruyenHotAdapter.ViewHolder> {
    private static final String TAG = "RecyclerViewTruyenHotAd";
    private ProgressDialog progressDialog;
    int idBook1;
    private ArrayList<book> books = new ArrayList<book>();
    private Context mContext;
    private HistoryDatabase historyDatabase;
    private int typeRecyclerView;
    public RecyclerViewTruyenHotAdapter(Context context,   ArrayList<book> books,int typeRecyclerView) {
        this.books = books;
        this.mContext = context;
        this.typeRecyclerView = typeRecyclerView;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        historyDatabase = new HistoryDatabase(mContext);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listtruyen_horizontal,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Glide.with(mContext)
                .asBitmap()
                .load(books.get(position).getImageURL())
                .into(holder.image);
        holder.Name.setText(books.get(position).getName());

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                historyDatabase.addHistoryOriginal(new history( user.Name,books.get(position).getIdBook(),-1));
                Log.i(TAG, "onClick: "+books.get(position).getIdBook());
                idBook1 = books.get(position).getIdBook();
                progressDialog = new ProgressDialog(mContext);
                progressDialog.setTitle(mContext.getString(R.string.loading));
                progressDialog.setMessage(mContext.getString(R.string.waiting));
                progressDialog.setCancelable(false);
                progressDialog.show();
                new loading().execute();
            }
        });
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image ;
        TextView Name;
        TextView Author;
        RelativeLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            Author = itemView.findViewById(R.id.author);
            Name = itemView.findViewById(R.id.name);
            layout = itemView.findViewById(R.id.relativeLayoutTruyen);
        }
    }
    class loading extends AsyncTask<Void,Void,Void>
    {

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
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
            Intent intent = new Intent(mContext,SachDetail.class);
            intent.putExtra("idBook",idBook1);
            mContext.startActivity(intent);
            return null;
        }
    }
}
