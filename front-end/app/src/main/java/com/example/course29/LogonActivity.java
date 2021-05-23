package com.example.course29;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.course29.util.HttpUtil;
import com.example.course29.util.ToastUtil;

import java.util.HashMap;
import java.util.Map;

public class LogonActivity extends AppCompatActivity {
    //声明控件
    private Button mBtnReturn;
    private Button mBtnLogon;
    private EditText mEtUsername;
    private EditText mEtPassword;
    private EditText mEtPasswordConfirm;
    private EditText mEtNickname;
    private EditText mEtTelephone;
    private TextView mTvPasswordWrong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logon);

        //找控件
        mBtnReturn = findViewById(R.id.btn_returnToLogin);
        mBtnLogon = findViewById(R.id.btn_logon);
        mEtUsername = findViewById(R.id.et_logon_username);
        mEtPassword = findViewById(R.id.et_logon_password);
        mEtPasswordConfirm = findViewById(R.id.et_logon_password_confirm);
        mEtNickname = findViewById(R.id.et_logon_nickname);
        mEtTelephone = findViewById(R.id.et_logon_telephone);
        mTvPasswordWrong = findViewById(R.id.tv_password_wrong);

        mBtnLogon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.e("click","click");
                Intent intent= null;
                Map map = new HashMap();
                map.put("username",mEtUsername.getText().toString());
                map.put("password",mEtPassword.getText().toString());
                map.put("nickname",mEtNickname.getText().toString());
                map.put("telephone",mEtTelephone.getText().toString());
                //TODO:二次验证
                Map res = HttpUtil.post("/user/register",map,LogonActivity.this);
                if (res.get("success").toString() == "true") {
                    ToastUtil.showMsg(LogonActivity.this, getResources().getString(R.string.logon_successfully));
                    intent = new Intent(LogonActivity.this,MainActivity.class);
                    startActivity(intent);
//                overridePendingTransition(0,0);
                }
                else {
                    ToastUtil.showMsg(LogonActivity.this,
                            res.get("msg") != null?res.get("msg").toString() : "Unknown Error");
                }
            }
        });

        mBtnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                intent = new Intent(LogonActivity.this,MainActivity.class);
                startActivity(intent);
//                overridePendingTransition(0,0);
            }
        });
    }
}