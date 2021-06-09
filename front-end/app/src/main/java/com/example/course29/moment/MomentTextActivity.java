package com.example.course29.moment;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.course29.R;
import com.example.course29.contact.friend.FriendActivity;
import com.example.course29.contact.newFriend.StrangerInfoActivity;
import com.example.course29.util.HttpUtil;
import com.example.course29.util.ToastUtil;

import java.util.HashMap;
import java.util.Map;

public class MomentTextActivity extends AppCompatActivity {
    private ImageView mIvMomentTextReturn;
    private EditText mEtMomentTextText;
    private ImageView mIvMomentTextConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moment_text);

        mIvMomentTextConfirm = findViewById(R.id.iv_momentText_confirm);
        mIvMomentTextReturn = findViewById(R.id.iv_momentText_return);
        mEtMomentTextText = findViewById(R.id.et_momentText_text);

        mIvMomentTextReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myFinish();
            }
        });

        mIvMomentTextConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = mEtMomentTextText.getText().toString();
                if(!str.equals("")) {
                    Map map = new HashMap();
                    map.put("type",0);
                    map.put("content",str);
                    Map res = HttpUtil.post("/moment/publishMoment",map, MomentTextActivity.this);
                    if (res.get("success").toString() == "true") {
                        ToastUtil.showMsg(MomentTextActivity.this, "发布成功");
                        finish();
                    }
                    else {
                        ToastUtil.showMsg(MomentTextActivity.this,
                                res.get("msg") != null?res.get("msg").toString() : "Unknown Error");
                    }
                }
                else {
                    ToastUtil.showMsg(MomentTextActivity.this,"请输入文字");
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        myFinish();
    }

    //    @Override
    public void myFinish() {
//        super.finish();
        View contentView = LayoutInflater.from(MomentTextActivity.this).inflate(R.layout.dialog_leave_moment,null);
        AlertDialog.Builder layoutDialog = new AlertDialog.Builder(MomentTextActivity.this);
        Button dialogBtnConfirm = contentView.findViewById(R.id.btn_dialogMomentLeave_confirm);
        Button dialogBtnCancel = contentView.findViewById(R.id.btn_dialogMomentLeave_cancel);
        layoutDialog.setView(contentView);
        Dialog dialog = layoutDialog.create();

        dialogBtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
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
}