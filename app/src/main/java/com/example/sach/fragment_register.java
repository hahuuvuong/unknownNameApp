package com.example.sach;

import android.content.Intent;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_register extends Fragment {

    public fragment_register() {
        // Required empty public constructor
    }
    private View root;
    private TextView userName;
    private TextView nickName;
    private TextView pass;
    private TextView rePass;
    private Button btnDK;
    UserDatabase userDatabase;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        font.setFontSize(getActivity());
        userDatabase = new UserDatabase(getContext());
        root =  inflater.inflate(R.layout.fragment_register, container, false);
        setControl();
        setEvent();
        return  root;
    }

    private void setEvent() {
        btnDK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userName.getText().toString().equals("")){
                    userName.setError(getContext().getString(R.string.username_needed));
                    userName.requestFocus();
                    return;
                }
                if(nickName.getText().toString().equals("")){
                    nickName.setError(getContext().getString(R.string.nickname_need));
                    nickName.requestFocus();
                    return;
                }
                if(pass.getText().toString().equals("")){
                    pass.setError(getContext().getString(R.string.passwordneed));
                    pass.requestFocus();
                    return;
                }
                if(rePass.getText().toString().equals("")){
                    rePass.setError(getContext().getString(R.string.password_needed));
                    rePass.requestFocus();
                    return;
                }
                if(!pass.getText().toString().equals(rePass.getText().toString())){
                    rePass.setError(getContext().getString(R.string.password_conflict));
                    rePass.requestFocus();
                    return;
                }
                if(userDatabase.checkUserExist(userName.getText().toString()) == 0){
                    userName.setError(getContext().getString(R.string.exist_username));
                    userName.requestFocus();
                    return;
                }
                user u = new user();
                u.setUserName(userName.getText().toString());
                u.setNickName(nickName.getText().toString());
                u.setPassWord(pass.getText().toString());
                userDatabase.addUser(u);
                Toast.makeText(getContext(),getContext().getString(R.string.signin_success),Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(),activity_login.class);
                getContext().startActivity(intent);
            }
        });
    }

    private void setControl() {
        btnDK = root.findViewById(R.id.btn_register);
        userName = root.findViewById(R.id.et_userName);
        nickName = root.findViewById(R.id.et_name);
        pass = root.findViewById(R.id.et_password);
        rePass = root.findViewById(R.id.et_repassword);
    }
}
