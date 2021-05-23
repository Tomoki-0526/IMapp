package com.example.course29;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.course29.util.BitmapUtil;
import com.example.course29.util.HttpUtil;
import com.example.course29.util.ToastUtil;

import java.util.HashMap;
import java.util.Map;

public class MenuActivity extends AppCompatActivity {
    //声明控件
    private ImageView mIvProfile;
    private TextView mTvUsername;
    private SlideMenu mSileMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
        setContentView(R.layout.activity_menu);

        // 找到控件
        mIvProfile = findViewById(R.id.iv_main_profile);
        mTvUsername = findViewById(R.id.tv_main_username);
        mSileMenu  = findViewById(R.id.sm_userInfoMenu);
        initInfo();

        mIvProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSileMenu.switchMenu();
            }
        });
    }

    private void initInfo () {
        Map res = HttpUtil.get("/user/getInfo", MenuActivity.this);
        if (res.get("success").toString() == "true") {
            Bitmap bitmap = BitmapUtil.getHttpBitmap("http://8.140.133.34:7564/avatar/default.png");
            mIvProfile.setImageBitmap(bitmap);
            mTvUsername.setText(res.get("nickname").toString());
        }
        else {
            ToastUtil.showMsg(MenuActivity.this,
                    res.get("msg") != null?res.get("msg").toString() : "Unknown Error");
        }
    }
}