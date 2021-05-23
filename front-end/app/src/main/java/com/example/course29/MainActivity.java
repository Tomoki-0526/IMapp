package com.example.course29;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.course29.util.HttpUtil;
import com.example.course29.util.ToastUtil;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    //声明控件
    private Button mBtnLogon;
    private Button mBtnLogin;
    private EditText mEtUsername;
    private EditText mEtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 找控件
        mBtnLogon = findViewById(R.id.btn_gotoLogon);
        mBtnLogin = findViewById(R.id.btn_login);
        mEtUsername = findViewById(R.id.et_login_username);
        mEtPassword = findViewById(R.id.et_login_password);

        mBtnLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {
                Intent intent= null;
                Map map = new HashMap();
                map.put("username",mEtUsername.getText().toString());
                map.put("password",mEtPassword.getText().toString());
                Map res = HttpUtil.post("/user/login",map,MainActivity.this);
//                Log.e("11", String.valueOf(res.get("msg")==null));
                if (res.get("success").toString() == "true") {
                    ToastUtil.showMsg(MainActivity.this, getResources().getString(R.string.login_successfully));
                    intent = new Intent(MainActivity.this, MenuActivity.class);
                    startActivity(intent);
//                overridePendingTransition(0,0);
                }
                else {
                    ToastUtil.showMsg(MainActivity.this,
                            res.get("msg") != null?res.get("msg").toString() : "Unknown Error");
                }
            }
        });
        mBtnLogon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {
                Intent intent= null;
                intent = new Intent (MainActivity.this,LogonActivity.class);
                startActivity(intent);
//                overridePendingTransition(0,0);
            }
        });
    }

}