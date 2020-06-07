package com.example.sach.ui.notifications;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.sach.ChangePassword;
import com.example.sach.MainActivity;
import com.example.sach.R;
import com.example.sach.TypefaceUtil;
import com.example.sach.activity_login;
import com.example.sach.database.HistoryDatabase;
import com.example.sach.model.font;
import com.example.sach.model.user;

import java.util.Locale;

import static androidx.core.content.ContextCompat.getSystemService;

public class NotificationsFragment extends Fragment {
    TextView textView;
    LinearLayout linearLayoutLogout;
    LinearLayout linearLayoutChangePassword;
    LinearLayout linearLayoutDeleteHistory;
    HistoryDatabase historyDatabase;
    TextView changeLanguage;
    TextView changeFont;
    TextView changeFontSize;
    View root;
    private static final String TAG = "NotificationsFragment";
    public static NotificationsFragment newInstance() {
        return new NotificationsFragment();
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        font.setFontSize(getActivity());
        loadLocale();
        root = inflater.inflate(R.layout.fragment_notifications, container, false);
        textView = root.findViewById(R.id.nickName);
        textView.setText(user.NICKNAME);
        historyDatabase = new HistoryDatabase(getContext());
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle(getResources().getString(R.string.app_name));
        setControl();
        setEvent();
        return root;

    }

    private void setEvent() {
        linearLayoutLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), activity_login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                getContext().startActivity(intent);
                getActivity().finish();
            }
        });
        linearLayoutChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ChangePassword.class);
                getContext().startActivity(intent);
            }
        });
        linearLayoutDeleteHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getContext())
                        .setTitle(getContext().getString(R.string.notification))
                        .setMessage(getContext().getString(R.string.delete_history_comfirm))
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(getContext().getString(R.string.yes), new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                historyDatabase.deleteHistory(user.Name);
                            }})
                        .setNegativeButton(getContext().getString(R.string.no), null).show();
            }
        });
        changeLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangeLanguageDialog();
            }
        });
        changeFont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangeFontDialog();
            }
        });
        changeFontSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangeFontSizeDialog();
            }
        });
    }
    private void showChangeFontSizeDialog() {
        final String [] listItems = {getString(R.string.small),getString(R.string.medium),getString(R.string.large)};
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(root.getContext());
        mBuilder.setTitle(getContext().getString(R.string.choose_fontSize));
        mBuilder.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which==0){
                    setFontSize(1);
                    Intent intent = new Intent(getContext(), MainActivity.class);
                    getContext().startActivity(intent);
                }
                else if(which==1){
                    setFontSize((float) 1.3);
                    Intent intent = new Intent(getContext(),MainActivity.class);
                    getContext().startActivity(intent);
                }
                else if(which==2){
                    setFontSize((float) 1.8);
                    Intent intent = new Intent(getContext(),MainActivity.class);
                    getContext().startActivity(intent);
                }
                dialog.dismiss();
            }
        });
        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }
    private void showChangeLanguageDialog() {
        final String [] listItems = {"English","Việt nam"};
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(root.getContext());
        mBuilder.setTitle(getContext().getString(R.string.choose_language));
        mBuilder.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which==0){
                    setLocale("en");
                    Intent intent = new Intent(getContext(), MainActivity.class);
                    getContext().startActivity(intent);
                }
                else if(which==1){
                    setLocale("vn");
                    Intent intent = new Intent(getContext(),MainActivity.class);
                    getContext().startActivity(intent);
                }
                dialog.dismiss();
            }
        });
        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }
    private void showChangeFontDialog(){
        final String [] listFont = {"Roboto","Sefie"};
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(root.getContext());
        mBuilder.setTitle(getContext().getString(R.string.choose_language));
        mBuilder.setSingleChoiceItems(listFont, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which==0){
                    setFont("font/font_1.ttf");
                    Intent intent = new Intent(getContext(), MainActivity.class);
                    getContext().startActivity(intent);
                }
                else if(which==1){
                    setFont("font/roboto_regular.ttf");
                    Intent intent = new Intent(getContext(),MainActivity.class);
                    getContext().startActivity(intent);
                }
                dialog.dismiss();
            }
        });
        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }
    private void setLocale(String en) {
        Locale locale = new Locale(en);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
            config.locale = locale;

        getActivity().getBaseContext().getResources().updateConfiguration(config,getActivity().getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = this.getActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE).edit();
        editor.putString("my_lang",en);
        editor.apply();
    }
    private void setFont(String font){
        TypefaceUtil.overrideFont(getContext(), "SERIF", font);
        SharedPreferences.Editor editor = this.getActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE).edit();
        editor.putString("my_font",font);
        editor.apply();
    }
    private void setFontSize(float size) {
        Configuration config = new Configuration();

        config.fontScale =  size;
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        metrics.scaledDensity = config.fontScale * metrics.density;
        getActivity().getBaseContext().getResources().updateConfiguration(config, metrics);

        SharedPreferences.Editor editor = this.getActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE).edit();
        editor.putFloat("my_fontsize", size);
        Log.e(TAG, "setFontSize: "+size );
        editor.apply();
    }
    public  void loadLocale(){
        SharedPreferences prefs = getActivity().getSharedPreferences("Settings",getContext().MODE_PRIVATE);
        String language = prefs.getString("my_lang","");
        setLocale(language);
    }
    private void setControl() {
        linearLayoutLogout = root.findViewById(R.id.logout);
        linearLayoutChangePassword = root.findViewById(R.id.changePassword);
        linearLayoutDeleteHistory  = root.findViewById(R.id.deleteHistory);
        changeLanguage = root.findViewById(R.id.change_language);
        changeFont = root.findViewById(R.id.change_font);
        changeFontSize = root.findViewById(R.id.change_fontSize);
    }
}
