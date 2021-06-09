package com.example.course29.moment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.course29.R;
import com.example.course29.UserInfo.UserActivity;
import com.example.course29.util.GlobalVariable;
import com.example.course29.util.ToastUtil;

public class MomentPictureActivity extends AppCompatActivity {
    private ImageView mIvMomentPictureReturn;
    private ImageView mIvMomentPictureConfirm;
    private ImageButton mIbMomentPictureAddPicture;
    private EditText mEtMomentPictureText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moment_picture);

        mIvMomentPictureConfirm = findViewById(R.id.iv_momentPicture_confirm);
        mIvMomentPictureReturn = findViewById(R.id.iv_momentPicture_return);
        mEtMomentPictureText = findViewById(R.id.et_momentPicture_text);
        mIbMomentPictureAddPicture = findViewById(R.id.ib_momentPicture_addPicture);

        mIvMomentPictureReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myFinish();
            }
        });

        mIbMomentPictureAddPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        myFinish();
    }




    public void myFinish() {
//        super.finish();
        View contentView = LayoutInflater.from(MomentPictureActivity.this).inflate(R.layout.dialog_leave_moment,null);
        AlertDialog.Builder layoutDialog = new AlertDialog.Builder(MomentPictureActivity.this);
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