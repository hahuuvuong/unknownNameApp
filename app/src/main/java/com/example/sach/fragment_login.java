package com.example.sach;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sach.database.UserDatabase;
import com.example.sach.model.font;
import com.example.sach.model.user;

import java.util.Locale;

import dmax.dialog.SpotsDialog;


/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_login extends Fragment {

    public fragment_login() {
        // Required empty public constructor
    }
    private View root;
    private TextView userName;
    private TextView pass;
    UserDatabase userDatabase;
    private ProgressDialog progressDialog;
    private Button btnDN;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        SharedPreferences prefs = getActivity().getSharedPreferences("Settings",getActivity().MODE_PRIVATE);
        String language = prefs.getString("my_lang","");
        font.setFontSize(getActivity());
        String font = prefs.getString("my_font","");
        TypefaceUtil.overrideFont(getContext(), "SERIF", font);



        setLocale(language);
        // Inflate the layout for this fragment
        userDatabase = new UserDatabase(getContext());
        root =  inflater.inflate(R.layout.fragment_login, container, false);
        setControl();
        setEvent();
        return root;
    }

    private void setEvent() {
        btnDN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userName.getText().toString().equals("")){
                    userName.setError(getContext().getString(R.string.username_needed));
                    userName.requestFocus();
                    return;
                }
                if(pass.getText().toString().equals("")){
                    pass.setError(getContext().getString(R.string.passwordneed));
                    pass.requestFocus();
                    return;
                }
                String temp = userName.getText().toString();
                String temp1 = pass.getText().toString();
                user u ;
                u = userDatabase.findUserById(temp,temp1);
                if(u==null){
                    Toast.makeText(getContext(),getContext().getString(R.string.login_error),Toast.LENGTH_SHORT).show();
                }
                else  {

                    Toast.makeText(getContext(),getContext().getString(R.string.login_success),Toast.LENGTH_SHORT).show();
                    user.Name = user.Name.concat(temp);
                    user.NICKNAME = user.NICKNAME.concat(u.getNickName());
                    progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setTitle(getContext().getString(R.string.loading));
                    progressDialog.setMessage(getContext().getString(R.string.waiting));
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    new loading().execute();
                }
            }
        });
    }

    private void setControl() {
        userName = root.findViewById(R.id.et_userNameDN);
        pass = root.findViewById(R.id.et_passwordDN);
        btnDN = root.findViewById(R.id.btn_login);
    }
    class loading extends AsyncTask<Void,Void,Void>
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

            Intent intent = new Intent(getContext(),MainActivity.class);
            getContext().startActivity(intent);
            return null;
        }
    }
    private void setLocale(String en) {
        Locale locale = new Locale(en);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getActivity().getBaseContext().getResources().updateConfiguration(config,getActivity().getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = getActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE).edit();
        editor.putString("my_lang",en);
        editor.apply();
    }
}
