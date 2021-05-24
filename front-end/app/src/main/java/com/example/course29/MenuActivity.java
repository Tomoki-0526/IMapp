package com.example.course29;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.course29.UserInfo.PrivacyActivity;
import com.example.course29.UserInfo.UserActivity;
import com.example.course29.util.BitmapUtil;
import com.example.course29.util.HttpUtil;
import com.example.course29.util.ToastUtil;

import java.util.Map;

public class MenuActivity extends AppCompatActivity {
    //声明控件
    private ImageView mIvMainProfile;
    private TextView mTvMainUsername;
    private ImageView mIvMenuProfile;
    private TextView mTvMenuUsername;
    private TextView mTvMenuSignature;
    private Button mBtnLogout;
    private SlideMenu mSileMenu;
    private LinearLayout mLlMenuPrivacy;
    private LinearLayout mLlMenuPicture;
    private LinearLayout mLlMenuSettings;
    private LinearLayout mLlMenuGeneral;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
        setContentView(R.layout.activity_menu);

        // 找到控件
        mIvMainProfile = findViewById(R.id.iv_main_profile);
        mTvMainUsername = findViewById(R.id.tv_main_username);
        mSileMenu  = findViewById(R.id.sm_userInfoMenu);
        mIvMenuProfile = findViewById(R.id.iv_menu_profile);
        mTvMenuUsername = findViewById(R.id.tv_menu_username);
        mTvMenuSignature = findViewById(R.id.tv_menu_signature);
        mBtnLogout = findViewById(R.id.btn_logout);
        mLlMenuPrivacy = findViewById(R.id.ll_menu_privacy);
        mLlMenuPicture = findViewById(R.id.ll_menu_picture);
        mLlMenuGeneral = findViewById(R.id.ll_menu_general);
        mLlMenuSettings = findViewById(R.id.ll_menu_settings);
        initInfo();
        setListener();
        mIvMainProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSileMenu.switchMenu();
            }
        });

        mBtnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= null;
                Map res = HttpUtil.get("/user/logout",MenuActivity.this);
//                Log.e("11", String.valueOf(res.get("msg")==null));
                if (res.get("success").toString() == "true") {
                    ToastUtil.showMsg(MenuActivity.this, getResources().getString(R.string.logout_successfully));
                    intent = new Intent(MenuActivity.this, MainActivity.class);
                    startActivity(intent);
//                overridePendingTransition(0,0);
                }
                else {
                    ToastUtil.showMsg(MenuActivity.this,
                            res.get("msg") != null?res.get("msg").toString() : "Unknown Error");
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        initInfo();
    }

    private void initInfo () {
        Map res = HttpUtil.get("/user/getInfo", MenuActivity.this);
        if (res.get("success").toString() == "true") {
            Bitmap bitmap = BitmapUtil.getHttpBitmap(res.get("avatar").toString());
            mIvMainProfile.setImageBitmap(bitmap);
            mTvMainUsername.setText(res.get("nickname").toString());
            mIvMenuProfile.setImageBitmap(bitmap);
            mTvMenuUsername.setText(res.get("nickname").toString());
            mTvMenuSignature.setText(res.get("signature").toString());
        }
        else {
            ToastUtil.showMsg(MenuActivity.this,
                    res.get("msg") != null?res.get("msg").toString() : "Unknown Error");
        }
    }

    private void setListener() {
       OnClick onClick = new OnClick();

        mIvMenuProfile.setOnClickListener(onClick);
        mLlMenuPrivacy.setOnClickListener(onClick);

    }

    private class OnClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()) {
                case R.id.iv_menu_profile:
                    intent = new Intent(MenuActivity.this, UserActivity.class);
                    break;
                case R.id.ll_menu_privacy:
                    intent = new Intent(MenuActivity.this, PrivacyActivity.class);
                    break;
                default:
                    break;
            }
            startActivity(intent);
        }
    }
}