package com.example.course29;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

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

import com.bumptech.glide.Glide;
import com.example.course29.UserInfo.PrivacyActivity;
import com.example.course29.UserInfo.UserActivity;
import com.example.course29.chat.ChatFragment;
import com.example.course29.contact.ContactFragment;
import com.example.course29.moment.MomentFragment;
import com.example.course29.nearby.NearbyFragment;
import com.example.course29.util.BitmapUtil;
import com.example.course29.util.HttpUtil;
import com.example.course29.util.ToastUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;

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
    private BottomNavigationView mBtmNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
        setContentView(R.layout.activity_menu);

        // 找到控件
        mIvMainProfile = findViewById(R.id.iv_main_profile);
        mTvMainUsername = findViewById(R.id.tv_main_nickname);
        mSileMenu  = findViewById(R.id.sm_userInfoMenu);
        mIvMenuProfile = findViewById(R.id.iv_menu_profile);
        mTvMenuUsername = findViewById(R.id.tv_menu_nickname);
        mTvMenuSignature = findViewById(R.id.tv_menu_signature);
        mBtnLogout = findViewById(R.id.btn_logout);
        mLlMenuPrivacy = findViewById(R.id.ll_menu_privacy);
        mLlMenuPicture = findViewById(R.id.ll_menu_picture);
        mLlMenuGeneral = findViewById(R.id.ll_menu_general);
        mLlMenuSettings = findViewById(R.id.ll_menu_settings);
        mBtmNavigationView = findViewById(R.id.bottomNavigationView);
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

        Fragment contactFragment = new ContactFragment();
        Fragment chatFragment = new ChatFragment();
        Fragment nearbyFragment = new NearbyFragment();
        Fragment momentFragment = new MomentFragment();
        setCurrentFragment(chatFragment);
        mBtmNavigationView.setOnNavigationItemSelectedListener(item -> {
                    switch (item.getItemId()) {
                        case R.id.navigation_chat:
                            setCurrentFragment(chatFragment);
                            return true;
                        case R.id.navigation_contact:
                            setCurrentFragment(contactFragment);
                            return true;
                        case R.id.navigation_nearby:
                            setCurrentFragment(nearbyFragment);
                            return true;
                        case R.id.navigation_moment:
                            setCurrentFragment(momentFragment);
                            return true;
                    }
                    return false;
                }
        );
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
//            mIvMainProfile.setImageBitmap(bitmap);
            Glide.with(this)
                    .load(res.get("avatar").toString())
                    .into(mIvMainProfile);
            mTvMainUsername.setText(res.get("nickname").toString());
//            mIvMenuProfile.setImageBitmap(bitmap);
            Glide.with(this)
                    .load(res.get("avatar").toString())
                    .into(mIvMenuProfile);
            mTvMenuUsername.setText(res.get("nicknam" +
                    "e").toString());
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

    private void setCurrentFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.flMainFragment, fragment).commit();
    }
}