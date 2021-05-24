package com.example.course29.UserInfo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.course29.MenuActivity;
import com.example.course29.R;

public class PrivacyActivity extends AppCompatActivity {
    private LinearLayout mLlPrivacyPassword;
    private ImageView mIvPrivacyReturn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);

        mLlPrivacyPassword = findViewById(R.id.ll_privacy_password);
        mIvPrivacyReturn = findViewById(R.id.iv_privacy_return);

        setListener();
        mIvPrivacyReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setListener() {
        OnClick onClick = new OnClick();

        mLlPrivacyPassword.setOnClickListener(onClick);

    }

    private class OnClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()) {
                case R.id.ll_privacy_password:
                    intent = new Intent(PrivacyActivity.this, PasswordActivity.class);
                    break;
                default:
                    break;
            }
            startActivity(intent);
        }
    }
}