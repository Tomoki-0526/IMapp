package com.example.course29.UserInfo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.course29.LogonActivity;
import com.example.course29.MainActivity;
import com.example.course29.R;
import com.example.course29.util.HttpUtil;
import com.example.course29.util.ToastUtil;

import java.util.HashMap;
import java.util.Map;

public class PasswordActivity extends AppCompatActivity {
    private ImageView mIvPasswordReturn;
    private EditText mEtOldPassword;
    private EditText mEtNewPassword;
    private EditText mEtConfirmPassword;
    private Button mBtnModifyPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        mIvPasswordReturn = findViewById(R.id.iv_password_return);
        mEtOldPassword = findViewById(R.id.et_password_oldPassword);
        mEtNewPassword = findViewById(R.id.et_password_newPassword);
        mEtConfirmPassword = findViewById(R.id.et_password_newPasswordConfirm);
        mBtnModifyPassword = findViewById(R.id.btn_modifyPassword);

        mBtnModifyPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map map = new HashMap();
                map.put("old_pwd",mEtOldPassword.getText().toString());
                map.put("new_pwd",mEtNewPassword.getText().toString());
                map.put("confirm_pwd",mEtConfirmPassword.getText().toString());
                Map res = HttpUtil.post("/user/modifyPassword",map, PasswordActivity.this);
                if (res.get("success").toString() == "true") {
                    SharedPreferences share = getSharedPreferences("Login",
                            Context.MODE_PRIVATE);
                    share.edit().putString("Password", mEtNewPassword.getText().toString()).commit();
                    ToastUtil.showMsg(PasswordActivity.this, getResources().getString(R.string.modify_successfully));
                    finish();
//                overridePendingTransition(0,0);
                }
                else {
                    ToastUtil.showMsg(PasswordActivity.this,
                            res.get("msg") != null?res.get("msg").toString() : "Unknown Error");
                }
            }
        });
    }
}