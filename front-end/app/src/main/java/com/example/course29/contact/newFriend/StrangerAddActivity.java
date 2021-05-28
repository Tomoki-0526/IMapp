package com.example.course29.contact.newFriend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.course29.R;
import com.example.course29.contact.friend.FriendActivity;
import com.example.course29.util.BitmapUtil;
import com.example.course29.util.HttpUtil;
import com.example.course29.util.ToastUtil;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class StrangerAddActivity extends AppCompatActivity {
    private String mStrUsername;
    private Button mBtnStrangerAddAgree;
    private Button mBtnStrangerAddRefuse;
    private TextView mTvStrangerAddUsername;
    private ImageView mIvStrangerAddReturn;
    private TextView mTvStrangerAddNickname;
    private TextView mTvStrangerAddGender;
    private TextView mTvStrangerAddSignature;
    private ImageView mIvStrangerAddProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stranger_add);

        Intent getIntent = getIntent();
        mStrUsername = getIntent.getStringExtra("strUsername");
//        Log.e("tag",mStrUsername);
        mIvStrangerAddReturn = findViewById(R.id.iv_strangerAdd_return);
        mBtnStrangerAddAgree = findViewById(R.id.btn_strangerAdd_agree);
        mBtnStrangerAddRefuse = findViewById(R.id.btn_strangerAdd_refuse);
        mTvStrangerAddUsername = findViewById(R.id.tv_strangerAdd_username);
        mTvStrangerAddGender = findViewById(R.id.tv_strangerAdd_gender);
        mTvStrangerAddNickname = findViewById(R.id.tv_strangerAdd_nickname);
        mTvStrangerAddSignature = findViewById(R.id.tv_strangerAdd_signature);
        mIvStrangerAddProfile = findViewById(R.id.iv_strangerAdd_profile);

        mIvStrangerAddReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initInfo();

        mBtnStrangerAddAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map map = new HashMap();
                map.put("from_username",mStrUsername);
                map.put("result",'1');
                Map res = HttpUtil.post("/friend/checkFriendRequest",map, StrangerAddActivity.this);
                if (res.get("success").toString() == "true") {
                    ToastUtil.showMsg(StrangerAddActivity.this, "添加成功");
                    Intent intent = new Intent(StrangerAddActivity.this,FriendActivity.class);
                    intent.putExtra("strUsername",mStrUsername);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
                else {
                    ToastUtil.showMsg(StrangerAddActivity.this,
                            res.get("msg") != null?res.get("msg").toString() : "Unknown Error");
                }
            }
        });

        mBtnStrangerAddRefuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map map = new HashMap();
                map.put("from_username",mTvStrangerAddUsername.getText().toString());
                map.put("result",'0');
                Map res = HttpUtil.post("/friend/checkFriendRequest",map, StrangerAddActivity.this);
                if (res.get("success").toString() == "true") {
                    ToastUtil.showMsg(StrangerAddActivity.this, "已拒绝申请");
                    Intent intent = new Intent(StrangerAddActivity.this,StrangerInfoActivity.class);
                    intent.putExtra("strUsername",mStrUsername);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
                else {
                    ToastUtil.showMsg(StrangerAddActivity.this,
                            res.get("msg") != null?res.get("msg").toString() : "Unknown Error");
                }
            }
        });
    }

    private void initInfo() {
        mTvStrangerAddUsername.setText(mStrUsername);
        Map map = new HashMap();
        map.put("stranger_username",mStrUsername);
        Map res = HttpUtil.post("/friend/getStrangerInfo",map, StrangerAddActivity.this);
        if (res.get("success").toString() == "true") {
            Bitmap bitmap = BitmapUtil.getHttpBitmap(res.get("stranger_avatar").toString());
            mIvStrangerAddProfile.setImageBitmap(bitmap);
            mTvStrangerAddSignature.setText(res.get("stranger_signature").toString());
            mTvStrangerAddNickname.setText(res.get("stranger_nickname").toString());
            switch (res.get("stranger_gender").toString()) {
                case "male":
                    mTvStrangerAddGender.setText("男生");
                    break;
                case "female":
                    mTvStrangerAddGender.setText("女生");
                    break;
                default:
                    mTvStrangerAddGender.setText("未知");
                    break;
            }
        }
        else {
            ToastUtil.showMsg(StrangerAddActivity.this,
                    res.get("msg") != null?res.get("msg").toString() : "Unknown Error");
        }
    }
}