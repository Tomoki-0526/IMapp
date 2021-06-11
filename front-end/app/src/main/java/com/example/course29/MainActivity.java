package com.example.course29;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.course29.util.GlobalVariable;
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

    private String strUsername = "";
    private String strPassword = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 找控件
        mBtnLogon = findViewById(R.id.btn_gotoLogon);
        mBtnLogin = findViewById(R.id.btn_login);
        mEtUsername = findViewById(R.id.et_login_username);
        mEtPassword = findViewById(R.id.et_login_password);

        SharedPreferences share = getSharedPreferences("Login",
                Context.MODE_PRIVATE);
        strUsername = share.getString("Username", "");
        strPassword = share.getString("Password", "");

        // 判断是否是之前有登录过
        // 判断是否刚注销
        if(share != null){
            if (share.getBoolean("LoginBool", false)) {
                login();
            }
            else {
                mEtUsername.setText(strUsername);
                mEtPassword.setText(strPassword);
            }
        }



        mBtnLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {
                strUsername = mEtUsername.getText().toString();
                strPassword = mEtPassword.getText().toString();
                login();
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

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences share = getSharedPreferences("Login",
                Context.MODE_PRIVATE);
        strUsername = share.getString("Username", "");
        strPassword = share.getString("Password", "");

        // 判断是否是之前有登录过
        // 判断是否刚注销
        if(share != null){
            if (share.getBoolean("LoginBool", false)) {
                login();
            }
            else {
                mEtUsername.setText(strUsername);
                mEtPassword.setText(strPassword);
            }
        }
    }

    private void login() {
        Intent intent= null;
        Map map = new HashMap();
        map.put("username",strUsername);
        map.put("password",strPassword);
        Map res = HttpUtil.post("/user/login",map,MainActivity.this);
//                Log.e("11", String.valueOf(res.get("msg")==null));
        if (res.get("success").toString() == "true") {

            // 创建SharedPreferences对象用于储存帐号和密码,并将其私有化
            SharedPreferences share = getSharedPreferences("Login",
                    Context.MODE_PRIVATE);
            // 获取编辑器来存储数据到sharedpreferences中
            SharedPreferences.Editor editor = share.edit();
            editor.putString("Username", strUsername);
            editor.putString("Password", strPassword);
            editor.putBoolean("LoginBool", true);
            // 将数据提交到sharedpreferences中
            editor.commit();

            ToastUtil.showMsg(MainActivity.this, getResources().getString(R.string.login_successfully));
            GlobalVariable.setGlobalUsername(strUsername);
            intent = new Intent(MainActivity.this, MenuActivity.class);
            startActivity(intent);
            finish();
//                overridePendingTransition(0,0);
        }
        else {
            ToastUtil.showMsg(MainActivity.this,
                    res.get("msg") != null?res.get("msg").toString() : "Unknown Error");
        }
    }

}