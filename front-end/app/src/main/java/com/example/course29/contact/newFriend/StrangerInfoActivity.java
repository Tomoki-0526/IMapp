package com.example.course29.contact.newFriend;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.course29.R;
import com.example.course29.contact.friend.FriendActivity;
import com.example.course29.util.BitmapUtil;
import com.example.course29.util.HttpUtil;
import com.example.course29.util.ToastUtil;

import java.util.HashMap;
import java.util.Map;

public class StrangerInfoActivity extends AppCompatActivity {
    private String mStrUsername;
    private ImageView mIvStrangerInfoReturn;
    private ImageView mIvStrangerInfoProfile;
    private TextView mTvStrangerInfoNickname;
    private TextView mTvStrangerInfoUsername;
    private TextView mTvStrangerInfoSignature;
    private TextView mTvStrangerInfoGender;
    private Button mBtnStrangerInfoAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stranger_info);

        Intent getIntent = getIntent();
        mStrUsername = getIntent.getStringExtra("strUsername");

        mIvStrangerInfoReturn = findViewById(R.id.iv_strangerInfo_return);
        mIvStrangerInfoProfile =findViewById(R.id.iv_strangerInfo_profile);
        mTvStrangerInfoGender = findViewById(R.id.tv_strangerInfo_gender);
        mTvStrangerInfoNickname =findViewById(R.id.tv_strangerInfo_nickname);
        mTvStrangerInfoSignature = findViewById(R.id.tv_strangerInfo_signature);
        mTvStrangerInfoUsername = findViewById(R.id.tv_strangerInfo_username);
        mBtnStrangerInfoAdd = findViewById(R.id.btn_strangerInfo_add);

        initInfo();

        mIvStrangerInfoReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mBtnStrangerInfoAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View contentView = LayoutInflater.from(StrangerInfoActivity.this).inflate(R.layout.dialog_add_stranger,null);
                AlertDialog.Builder layoutDialog = new AlertDialog.Builder(StrangerInfoActivity.this);
                Button dialogBtnConfirm = contentView.findViewById(R.id.btn_dialogStrangerAdd_confirm);
                Button dialogBtnCancel = contentView.findViewById(R.id.btn_dialogStrangerAdd_cancel);
                EditText dialogEtExtra = contentView.findViewById(R.id.et_dialogStrangerAdd_extra);
                layoutDialog.setView(contentView);
                Dialog dialog = layoutDialog.create();

                dialogBtnConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map map = new HashMap();
                        map.put("to_username",mStrUsername);
                        map.put("extra",dialogEtExtra.getText().toString());
                        Map res = HttpUtil.post("/friend/newFriendRequest",map, StrangerInfoActivity.this);
                        if (res.get("success").toString() == "true") {
                            ToastUtil.showMsg(StrangerInfoActivity.this, "好友请求发送成功");
                            dialog.dismiss();
                        }
                        else {
                            ToastUtil.showMsg(StrangerInfoActivity.this,
                                    res.get("msg") != null?res.get("msg").toString() : "Unknown Error");
                        }
                    }
                });
                dialogBtnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

    private void initInfo() {
        mTvStrangerInfoUsername.setText(mStrUsername);
        Map map = new HashMap();
        map.put("stranger_username",mStrUsername);
        Map res = HttpUtil.post("/friend/getStrangerInfo",map, StrangerInfoActivity.this);
        if (res.get("success").toString() == "true") {
            Bitmap bitmap = BitmapUtil.getHttpBitmap(res.get("stranger_avatar").toString());
            mIvStrangerInfoProfile.setImageBitmap(bitmap);
            mTvStrangerInfoSignature.setText(res.get("stranger_signature").toString());
            mTvStrangerInfoNickname.setText(res.get("stranger_nickname").toString());
            switch (res.get("stranger_gender").toString()) {
                case "male":
                    mTvStrangerInfoGender.setText("男生");
                    break;
                case "female":
                    mTvStrangerInfoGender.setText("女生");
                    break;
                default:
                    mTvStrangerInfoGender.setText("未知");
                    break;
            }
        }
        else {
            ToastUtil.showMsg(StrangerInfoActivity.this,
                    res.get("msg") != null?res.get("msg").toString() : "Unknown Error");
        }
    }
}