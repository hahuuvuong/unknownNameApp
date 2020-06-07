package com.example.sach;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sach.model.API;

import dmax.dialog.SpotsDialog;

public class RecyclerViewCategoryAdapter extends RecyclerView.Adapter<RecyclerViewCategoryAdapter.ViewHolder>{
    private static final String TAG = "RecyclerViewCategoryAda";
    int temptID;
    private ProgressDialog progressDialog;
    private int [] idTheloai;
    private int [] mImage;
    private int [] mBackgroundColor;
    private String [] mName;
    public Context mContext;
    public RecyclerViewCategoryAdapter(int[] mImage, String[] mName, Context mContext,int[] mBackgroundColor,int[] idTheloai) {
        this.mImage = mImage;
        this.mName = mName;
        this.mContext = mContext;
        this.mBackgroundColor = mBackgroundColor;
        this.idTheloai = idTheloai;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listview_category,parent,false);
        return new RecyclerViewCategoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.Name.setText(mName[position]);
        holder.image.setImageResource(mImage[position]);
        holder.layout.setBackgroundColor(mBackgroundColor[position]);

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: "+idTheloai[position]);
                temptID = idTheloai[position];
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
        return mName.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image ;
        TextView Name;
        LinearLayout layout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imageCategory);
            Name = itemView.findViewById(R.id.nameCategory);
            layout = itemView.findViewById(R.id.layoutCategory);
        }
    }
    class loading extends AsyncTask<Void,Void,Void>
    {
       // AlertDialog dialog=new SpotsDialog(mContext, R.style.CustomLoadingDialog);
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
           // dialog.show();
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
            Intent intent = new Intent(mContext,SearchDetail.class);
            intent.putExtra("query", API.URL +"Sach?idTheLoai=" + temptID);
            mContext.startActivity(intent);
            return null;
        }
    }

}
