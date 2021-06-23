package com.example.course29.moment;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.course29.R;
import com.example.course29.UserInfo.UserActivity;
import com.example.course29.util.FileUriUtil;
import com.example.course29.util.GlobalVariable;
import com.example.course29.util.HttpUtil;
import com.example.course29.util.ToastUtil;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MomentVideoActivity extends AppCompatActivity {
    private Uri videoUri;
    private ImageView mIvMomentVideoReturn;
    private ImageView mIvMomentVideoConfirm;
    private ImageButton mIbMomentVideoAddVideo;
    private ImageView mIvMomentVideoVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moment_video);
        mIvMomentVideoVideo = findViewById(R.id.iv_momentVideo_video);
        mIvMomentVideoReturn = findViewById(R.id.iv_momentVideo_return);
        mIvMomentVideoConfirm = findViewById(R.id.iv_momentVideo_confirm);
        mIbMomentVideoAddVideo = findViewById(R.id.ib_momentVideo_addVideo);

        mIvMomentVideoReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myFinish();
            }
        });

        mIvMomentVideoVideo.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PopupMenu popup = new PopupMenu(MomentVideoActivity.this, v);//第二个参数是绑定的那个view
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.delete_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Intent intent = null;
                        if (item.getItemId() == R.id.delete_delete)
                        {
                            mIvMomentVideoVideo.setVisibility(View.GONE);
                            mIbMomentVideoAddVideo.setVisibility(View.VISIBLE);
                        }
                        return false;
                    }
                });
                popup.show();
                return false;
            }
        });

        mIbMomentVideoAddVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPhotos();
            }
        });

        mIvMomentVideoConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = null;
                try {

                    File Afile = FileUriUtil.getFileByUri(videoUri,MomentVideoActivity.this);
                    Uri uriFile = null;
                    if(Afile.exists()) {
                        uriFile = Uri.fromFile(Afile);
                    }
                    else {
                        uriFile = Uri.parse(Uri.decode(videoUri.toString()));
                        Log.e("noFile", "noFile");
                    }
                    file = new File(new URI(uriFile.toString()));
                    Log.e("video",file.toString());
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                postProfile(file);
            }
        });
    }

    @Override
    public void onBackPressed() {
        myFinish();
    }

    private void openPhotos() {
        if (ContextCompat.checkSelfPermission(MomentVideoActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {   //权限还没有授予，需要在这里写申请权限的代码
            // 第二个参数是一个字符串数组，里面是你需要申请的权限 可以设置申请多个权限
            // 最后一个参数是标志你这次申请的权限，该常量在onRequestPermissionsResult中使用到
            ActivityCompat.requestPermissions(MomentVideoActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    GlobalVariable.GALLERY_REQUEST_CODE);

        } else { //权限已经被授予，在这里直接写要执行的相应方法即可
            choosePhoto();
        }
    }

    private void choosePhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        // 如果限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型" 所有类型则写 "image/*"
        intent.setType("video/*");
        startActivityForResult(intent, GlobalVariable.GALLERY_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        Log.e("code",String.valueOf(requestCode));

        if (requestCode == GlobalVariable.GALLERY_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                choosePhoto();
            } else {
                // Permission Denied
                ToastUtil.showMsg(MomentVideoActivity.this, "Permission Denied");
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri uri;

        if (resultCode == UserActivity.RESULT_OK) {
            if (requestCode == GlobalVariable.GALLERY_REQUEST_CODE) {
                videoUri = data.getData();
                Glide.with(MomentVideoActivity.this)
                        .setDefaultRequestOptions(
                                new RequestOptions()
                                        .frame(1000000)
                                        .centerCrop()
                        )
                        .load(videoUri)
                        .into(mIvMomentVideoVideo);
            }
            mIvMomentVideoVideo.setVisibility(View.VISIBLE);
            mIbMomentVideoAddVideo.setVisibility(View.GONE);
        }
    }

    private void postProfile (File file) {
        List<File> files = new ArrayList<>();
        Map map = new HashMap();
        map.put("type",3);
        files.add(file);
        Map res = HttpUtil.postFiles("/moment/publishMoment",map, files, "video",MomentVideoActivity.this);
        if (res.get("success").toString() == "true") {
            ToastUtil.showMsg(MomentVideoActivity.this, "发布成功");
//                overridePendingTransition(0,0);
        }
        else {
            ToastUtil.showMsg(MomentVideoActivity.this,
                    res.get("msg") != null?res.get("msg").toString() : "Unknown Error");
        }
    }

    public void myFinish () {
//        super.finish();
        View contentView = LayoutInflater.from(MomentVideoActivity.this).inflate(R.layout.dialog_leave_moment, null);
        AlertDialog.Builder layoutDialog = new AlertDialog.Builder(MomentVideoActivity.this);
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