package com.example.course29.contact.friend;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.course29.R;
import com.example.course29.contact.newFriend.StrangerAddActivity;
import com.example.course29.contact.newFriend.StrangerInfoActivity;
import com.example.course29.util.BitmapUtil;
import com.example.course29.util.HttpUtil;
import com.example.course29.util.ToastUtil;

import java.util.HashMap;
import java.util.Map;

public class FriendActivity extends AppCompatActivity {
    private ImageView mIvFriendReturn;
    private ImageView mIvFriendMore;
    private String mStrUsername;
    private LinearLayout mLlDialogFriendSetStar;
    private LinearLayout mLlDialogFriendDelete;
    private LinearLayout mLlDialogFriendRemark;
    private TextView mTvFriendNickname;
    private TextView mTvFriendBirthday;
    private TextView mTvFriendUsername;
    private TextView mTvFriendTelephone;
    private TextView mTvFriendSignature;
    private TextView mTvFriendGender;
    private TextView mTvFriendRemark;
    private LinearLayout mLlFriendRemark;
    private ImageView mIvFriendAvatar;
    private TextView mTvDialogFriendSetStar;
    private TextView mTvFriendStar;
    private Dialog bottomDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
        setContentView(R.layout.activity_friend);

        Intent getIntent = getIntent();
        mStrUsername = getIntent.getStringExtra("strUsername");

        mIvFriendReturn = findViewById(R.id.iv_friend_return);
        mIvFriendMore = findViewById(R.id.iv_friend_more);
//        mBtnFriendDelete = findViewById(R.id.btn_friend_delete);

        mTvFriendUsername = findViewById(R.id.tv_friend_username);
        mTvFriendStar = findViewById(R.id.tv_friend_star);
        mTvFriendBirthday = findViewById(R.id.tv_friend_birthday);
        mTvFriendNickname = findViewById(R.id.tv_friend_nickname);
        mTvFriendTelephone = findViewById(R.id.tv_friend_telephone);
        mTvFriendSignature = findViewById(R.id.tv_friend_signature);
        mTvFriendGender = findViewById(R.id.tv_friend_gender);
        mIvFriendAvatar = findViewById(R.id.iv_friend_profile);
        mTvFriendRemark = findViewById(R.id.tv_friend_remark);
        mLlFriendRemark = findViewById(R.id.ll_friebd_remark);



        initBottomDialog();
        mLlDialogFriendDelete = bottomDialog.findViewById(R.id.ll_dialogFriend_delete);
        mLlDialogFriendRemark = bottomDialog.findViewById(R.id.ll_dialogFriend_remark);
        mLlDialogFriendSetStar = bottomDialog.findViewById(R.id.ll_dialogFriend_setStar);
        mTvDialogFriendSetStar = bottomDialog.findViewById(R.id.tv_dialogFriend_setStar);

        initInfo();
        mIvFriendReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mIvFriendMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bottomDialog != null) {
                    bottomDialog.show();
                }
            }
        });
        mLlDialogFriendRemark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View contentView = LayoutInflater.from(FriendActivity.this).inflate(R.layout.dialog_remark_friend,null);
                AlertDialog.Builder layoutDialog = new AlertDialog.Builder(FriendActivity.this);
                Button dialogBtnConfirm = contentView.findViewById(R.id.btn_dialogFriendRemark_confirm);
                Button dialogBtnCancel = contentView.findViewById(R.id.btn_dialogFriendRemark_cancel);
                EditText dialogEtRemark = contentView.findViewById(R.id.et_dialogFriendRemark_remark);
                layoutDialog.setView(contentView);
                Dialog dialog = layoutDialog.create();

                dialogBtnConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map map = new HashMap();
                        map.put("friend_username",mStrUsername);
                        map.put("remark",dialogEtRemark.getText().toString());
                        String str = dialogEtRemark.getText().toString();
                        Map res = HttpUtil.post("/friend/setFriendRemark",map, FriendActivity.this);
                        if (res.get("success").toString() == "true") {
                            ToastUtil.showMsg(FriendActivity.this, "设置成功");
                            if (str.equals(""))
                            {
                                mLlFriendRemark.setVisibility(View.GONE);
                                mTvFriendRemark.setText("");
                            }
                            else
                            {
                                mLlFriendRemark.setVisibility(View.VISIBLE);
                                mTvFriendRemark.setText(str);
                            }
                            dialog.dismiss();
                        }
                        else {
                            ToastUtil.showMsg(FriendActivity.this,
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
                bottomDialog.dismiss();
            }
        });
        mLlDialogFriendDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View contentView = LayoutInflater.from(FriendActivity.this).inflate(R.layout.dialog_delete_friend,null);
                AlertDialog.Builder layoutDialog = new AlertDialog.Builder(FriendActivity.this);
                Button dialogBtnConfirm = contentView.findViewById(R.id.btn_dialogFriendDelete_confirm);
                Button dialogBtnCancel = contentView.findViewById(R.id.btn_dialogFriendDelete_cancel);
                layoutDialog.setView(contentView);
                Dialog dialog = layoutDialog.create();

                dialogBtnConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map map = new HashMap();
                        map.put("friend_username",mStrUsername);
                        Map res = HttpUtil.post("/friend/removeFriend",map, FriendActivity.this);
                        if (res.get("success").toString() == "true") {
                            ToastUtil.showMsg(FriendActivity.this, "删除成功");
                            dialog.dismiss();
                            finish();
                        }
                        else {
                            ToastUtil.showMsg(FriendActivity.this,
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
                bottomDialog.dismiss();
            }
        });
        //TODO: 设置备注、设置星标
        mLlDialogFriendSetStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mTvDialogFriendSetStar.getText().toString().equals("取消星标好友")) {
                    Map map = new HashMap();
                    map.put("friend_username",mStrUsername);
                    Map res = HttpUtil.post("/friend/cancelStarFriend",map, FriendActivity.this);
                    if (res.get("success").toString() == "true") {
                        ToastUtil.showMsg(FriendActivity.this, "取消成功");
                        mTvDialogFriendSetStar.setText("设为星标好友");
                        mTvFriendStar.setVisibility(View.GONE);
                        bottomDialog.dismiss();
                    }
                    else {
                        ToastUtil.showMsg(FriendActivity.this,
                                res.get("msg") != null?res.get("msg").toString() : "Unknown Error");
                    }
                }
                else {
                    Map map = new HashMap();
                    map.put("friend_username",mStrUsername);
                    Map res = HttpUtil.post("/friend/setStarFriend",map, FriendActivity.this);
                    if (res.get("success").toString() == "true") {
                        ToastUtil.showMsg(FriendActivity.this, "设置成功");
                        mTvDialogFriendSetStar.setText("取消星标好友");
                        mTvFriendStar.setVisibility(View.VISIBLE);
                        bottomDialog.dismiss();
                    }
                    else {
                        ToastUtil.showMsg(FriendActivity.this,
                                res.get("msg") != null?res.get("msg").toString() : "Unknown Error");
                    }
                }
            }
        });
//        mBtnFriendDelete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Map map = new HashMap();
//                map.put("friend_username",mStrUsername);
//                Map res = HttpUtil.post("/friend/removeFriend",map, FriendActivity.this);
//                if (res.get("success").toString() == "true") {
//                    ToastUtil.showMsg(FriendActivity.this, "删除成功");
//
//                }
//                else {
//                    ToastUtil.showMsg(FriendActivity.this,
//                            res.get("msg") != null?res.get("msg").toString() : "Unknown Error");
//                }
//            }
//        });
    }

    private void initBottomDialog(){
        bottomDialog = new Dialog(this, R.style.BottomDialog);
        View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_friend, null);
        bottomDialog.setContentView(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        layoutParams.width = getResources().getDisplayMetrics().widthPixels;
        contentView.setLayoutParams(layoutParams);
        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);

    }

    private void initInfo() {
        mTvFriendUsername.setText(mStrUsername);
        Map map = new HashMap();
        map.put("friend_username",mStrUsername);
        Map res = HttpUtil.post("/friend/getFriendInfo",map, FriendActivity.this);
        if (res.get("success").toString() == "true") {
            Bitmap bitmap = BitmapUtil.getHttpBitmap(res.get("avatar").toString());
//            mIvFriendAvatar.setImageBitmap(bitmap);
            Glide.with(this)
                    .load(res.get("avatar").toString())
                    .into(mIvFriendAvatar);
            mTvFriendSignature.setText(res.get("signature").toString());
            mTvFriendTelephone.setText(res.get("telephone").toString());
            mTvFriendBirthday.setText(res.get("birthday").toString());
            mTvFriendNickname.setText(res.get("nickname").toString());
            if (res.get("star").toString() == "true") {
                mTvDialogFriendSetStar.setText("取消星标好友");
                mTvFriendStar.setVisibility(View.VISIBLE);
            }
            else {
                mTvDialogFriendSetStar.setText("设为星标好友");
                mTvFriendStar.setVisibility(View.GONE);
            }
            Log.e("tag",String.valueOf(res.get("remark").toString().equals("")));
            if (res.get("remark").toString().equals("")) {
                mLlFriendRemark.setVisibility(View.GONE);
                mTvFriendRemark.setText("");
            }
            else {
                mLlFriendRemark.setVisibility(View.VISIBLE);
                mTvFriendRemark.setText(res.get("remark").toString());
            }
            switch (res.get("gender").toString()) {
                case "male":
                    mTvFriendGender.setText("男生");
                    break;
                case "female":
                    mTvFriendGender.setText("女生");
                    break;
                default:
                    mTvFriendGender.setText("未知");
                    break;
            }
        }
        else {
            ToastUtil.showMsg(FriendActivity.this,
                    res.get("msg") != null?res.get("msg").toString() : "Unknown Error");
        }
    }
}