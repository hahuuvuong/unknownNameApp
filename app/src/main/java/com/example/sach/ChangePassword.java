package com.example.sach;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sach.database.UserDatabase;
import com.example.sach.model.user;

public class ChangePassword extends AppCompatActivity {
    EditText oldPassword;
    EditText newPassword;
    EditText reNewPassword;
    Button btnComfirm;
    UserDatabase userDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        userDatabase = new UserDatabase(getApplicationContext());
        setControl();
        setEvent();
    }

    private void setEvent() {

        btnComfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(oldPassword.getText().toString().equals("")){
                    oldPassword.setError(getApplicationContext().getString(R.string.old_password_needed));
                    oldPassword.requestFocus();
                    return;
                }
                if(newPassword.getText().toString().equals("")){
                    newPassword.setError(getApplicationContext().getString(R.string.new_password_needed));
                    newPassword.requestFocus();
                    return;
                }
                if(reNewPassword.getText().toString().equals("")){
                    reNewPassword.setError(getApplicationContext().getString(R.string.password_needed));
                    reNewPassword.requestFocus();
                    return;
                }
                if(!newPassword.getText().toString().equals(reNewPassword.getText().toString())){
                    reNewPassword.setError(getApplicationContext().getString(R.string.password_conflict));
                    reNewPassword.requestFocus();
                    return;
                }
                if(userDatabase.findUserById(user.Name,oldPassword.getText().toString())==null){
                    oldPassword.setError(getApplicationContext().getString(R.string.old_password_conflict));
                    oldPassword.requestFocus();
                    return;
                }
                user u = new user();
                u.setUserName(user.Name);
                u.setPassWord(reNewPassword.getText().toString());
                userDatabase.updateUser(u);
                Toast.makeText(getApplicationContext(),getApplicationContext().getString(R.string.change_password_success),Toast.LENGTH_SHORT);
                onBackPressed();
            }
        });
    }

    private void setControl() {
        oldPassword = findViewById(R.id.ed_oldpassword);
        newPassword = findViewById(R.id.et_newpassword);
        reNewPassword = findViewById(R.id.et_renewpassword);
        btnComfirm = findViewById(R.id.btn_changepassword);
    }
}
