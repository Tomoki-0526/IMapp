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
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;

import com.bumptech.glide.Glide;
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

public class MomentPictureActivity extends AppCompatActivity {
    private ImageView mIvMomentPictureReturn;
    private ImageView mIvMomentPictureConfirm;
    private ImageButton mIbMomentPictureAddPicture;
    private EditText mEtMomentPictureText;
    private ImageView mIvMomentPicture0;
    private ImageView mIvMomentPicture1;
    private ImageView mIvMomentPicture2;
    private ImageView mIvMomentPicture3;
    private int totalNum = 0;
    private List<Uri> fileList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moment_picture);

        mIvMomentPictureConfirm = findViewById(R.id.iv_momentPicture_confirm);
        mIvMomentPictureReturn = findViewById(R.id.iv_momentPicture_return);
        mEtMomentPictureText = findViewById(R.id.et_momentPicture_text);
        mIbMomentPictureAddPicture = findViewById(R.id.ib_momentPicture_addPicture);
        mIvMomentPicture0 = findViewById(R.id.iv_momentPicture_0);
        mIvMomentPicture1 = findViewById(R.id.iv_momentPicture_1);
        mIvMomentPicture2 = findViewById(R.id.iv_momentPicture_2);
        mIvMomentPicture3 = findViewById(R.id.iv_momentPicture_3);

        mIvMomentPictureReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myFinish();
            }
        });

        mIvMomentPicture0.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PopupMenu popup = new PopupMenu(MomentPictureActivity.this, v);//第二个参数是绑定的那个view
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.delete_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Intent intent = null;
                        if (item.getItemId() == R.id.delete_delete)
                        {
                            fileList.remove(0);
                            totalNum -= 1;
                            showPictures();
                        }
                        return false;
                    }
                });
                popup.show();
                return false;
            }
        });
        mIvMomentPicture1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PopupMenu popup = new PopupMenu(MomentPictureActivity.this, v);//第二个参数是绑定的那个view
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.delete_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Intent intent = null;
                        if (item.getItemId() == R.id.delete_delete)
                        {
                            fileList.remove(1);
                            totalNum -= 1;
                            showPictures();
                        }
                        return false;
                    }
                });
                popup.show();
                return false;
            }
        });
        mIvMomentPicture2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PopupMenu popup = new PopupMenu(MomentPictureActivity.this, v);//第二个参数是绑定的那个view
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.delete_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Intent intent = null;
                        if (item.getItemId() == R.id.delete_delete)
                        {
                            fileList.remove(2);
                            totalNum -= 1;
                            showPictures();
                        }
                        return false;
                    }
                });
                popup.show();
                return false;
            }
        });
        mIvMomentPicture3.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PopupMenu popup = new PopupMenu(MomentPictureActivity.this, v);//第二个参数是绑定的那个view
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.delete_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Intent intent = null;
                        if (item.getItemId() == R.id.delete_delete)
                        {
                            fileList.remove(3);
                            totalNum -= 1;
                            showPictures();
                        }
                        return false;
                    }
                });
                popup.show();
                return false;
            }
        });

        mIbMomentPictureAddPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPhotos();
            }
        });

        mIvMomentPictureConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = mEtMomentPictureText.getText().toString();
                if(!str.equals("") && fileList.size()==0) {
                    Map map = new HashMap();
                    map.put("type",0);
                    map.put("content",str);
                    Map res = HttpUtil.post("/moment/publishMoment",map, MomentPictureActivity.this);
                    if (res.get("success").toString() == "true") {
                        ToastUtil.showMsg(MomentPictureActivity.this, "发布成功");
                        finish();
                    }
                    else {
                        ToastUtil.showMsg(MomentPictureActivity.this,
                                res.get("msg") != null?res.get("msg").toString() : "Unknown Error");
                    }
                }
                if(str.equals("") && fileList.size()!=0) {
                    Map map = new HashMap();
                    map.put("type",1);
                    List<File> files = new ArrayList<>();
                    for(int i=0; i<fileList.size();i++) {
                        File file = null;
                        try {
                            file = new File(new URI(fileList.get(i).toString()));
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                        files.add(file);
                    }
                    Map res = HttpUtil.postFiles("/moment/publishMoment",map, files, "images",MomentPictureActivity.this);
                    if (res.get("success").toString() == "true") {
                        ToastUtil.showMsg(MomentPictureActivity.this, "发布成功");
                        finish();
                    }
                    else {
                        ToastUtil.showMsg(MomentPictureActivity.this,
                                res.get("msg") != null?res.get("msg").toString() : "Unknown Error");
                    }
                }
                if(!str.equals("") && fileList.size()!=0) {
                    Map map = new HashMap();
                    map.put("type",2);
                    map.put("content",str);
                    List<File> files = new ArrayList<>();
                    for(int i=0; i<fileList.size();i++) {
                        File file = null;
                        try {
                            file = new File(new URI(fileList.get(i).toString()));
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                        files.add(file);
                    }
                    Map res = HttpUtil.postFiles("/moment/publishMoment",map, files, "images",MomentPictureActivity.this);
                    if (res.get("success").toString() == "true") {
                        ToastUtil.showMsg(MomentPictureActivity.this, "发布成功");
                        finish();
                    }
                    else {
                        ToastUtil.showMsg(MomentPictureActivity.this,
                                res.get("msg") != null?res.get("msg").toString() : "Unknown Error");
                    }
                }
                if(str.equals("") && fileList.size()==0) {
                    ToastUtil.showMsg(MomentPictureActivity.this,"请输入文字或选择图片");
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        myFinish();
    }

    private void openPhotos() {
        if (ContextCompat.checkSelfPermission(MomentPictureActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {   //权限还没有授予，需要在这里写申请权限的代码
            // 第二个参数是一个字符串数组，里面是你需要申请的权限 可以设置申请多个权限
            // 最后一个参数是标志你这次申请的权限，该常量在onRequestPermissionsResult中使用到
            ActivityCompat.requestPermissions(MomentPictureActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    GlobalVariable.GALLERY_REQUEST_CODE);

        } else { //权限已经被授予，在这里直接写要执行的相应方法即可
            choosePhoto();
        }
    }

    private void choosePhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        // 如果限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型" 所有类型则写 "image/*"
        intent.setType("image/jpeg");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
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
                ToastUtil.showMsg(MomentPictureActivity.this, "Permission Denied");
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
                ClipData imageNames = data.getClipData();
                if (imageNames != null) {
                    int n;
                    if (imageNames.getItemCount() > 4 - totalNum) {
                        n = 4 - totalNum;
                        totalNum += n;
                        ToastUtil.showMsg(MomentPictureActivity.this,"最多选择4张照片");
                    }
                    else {
                        n=imageNames.getItemCount();
                        totalNum += n;
                    }
                    for (int i = 0; i < n; i++) {
                        Uri imageUri = imageNames.getItemAt(i).getUri();

                        File file = FileUriUtil.getFileByUri(imageUri,MomentPictureActivity.this);
                        Uri uriFile = null;
                        if(file.exists()) {
                            uriFile = Uri.fromFile(file);
                        }
                        else {
                            uriFile = Uri.parse(Uri.decode(imageUri.toString()));
                            Log.e("noFile", "noFile");
                        }
                        fileList.add(uriFile);

                    }
                    //uri = imageNames.getItemAt(0).getUri();
                    showPictures();
                } else {
                    uri = data.getData();
                    //fileList.add(uri.toString());
                }
            } else {
                uri = data.getData();
                //fileList.add(uri.toString());
            }
        }
    }


    private void showPictures() {
        int n = fileList.size();
        if(n == 0) {
            mIvMomentPicture0.setVisibility(View.GONE);
            mIvMomentPicture1.setVisibility(View.GONE);
            mIvMomentPicture2.setVisibility(View.GONE);
            mIvMomentPicture3.setVisibility(View.GONE);
        }
        if(n == 1) {
            mIvMomentPicture0.setVisibility(View.VISIBLE);
            Glide.with(this).load(fileList.get(0)).into(mIvMomentPicture0);
            mIvMomentPicture1.setVisibility(View.GONE);
            mIvMomentPicture2.setVisibility(View.GONE);
            mIvMomentPicture3.setVisibility(View.GONE);
        }
        if(n == 2) {
            mIvMomentPicture0.setVisibility(View.VISIBLE);
            Glide.with(this).load(fileList.get(0)).into(mIvMomentPicture0);
            mIvMomentPicture1.setVisibility(View.VISIBLE);
            Glide.with(this).load(fileList.get(1)).into(mIvMomentPicture1);
            mIvMomentPicture2.setVisibility(View.GONE);
            mIvMomentPicture3.setVisibility(View.GONE);
        }
        if(n == 3) {
            mIvMomentPicture0.setVisibility(View.VISIBLE);
            Glide.with(this).load(fileList.get(0)).into(mIvMomentPicture0);
            mIvMomentPicture1.setVisibility(View.VISIBLE);
            Glide.with(this).load(fileList.get(1)).into(mIvMomentPicture1);
            mIvMomentPicture2.setVisibility(View.VISIBLE);
            Glide.with(this).load(fileList.get(2)).into(mIvMomentPicture2);
            mIvMomentPicture3.setVisibility(View.GONE);
        }
        if(n == 4) {
            mIvMomentPicture0.setVisibility(View.VISIBLE);
            Glide.with(this).load(fileList.get(0)).into(mIvMomentPicture0);
            mIvMomentPicture1.setVisibility(View.VISIBLE);
            Glide.with(this).load(fileList.get(1)).into(mIvMomentPicture1);
            mIvMomentPicture2.setVisibility(View.VISIBLE);
            Glide.with(this).load(fileList.get(2)).into(mIvMomentPicture2);
            mIvMomentPicture3.setVisibility(View.VISIBLE);
            Glide.with(this).load(fileList.get(3)).into(mIvMomentPicture3);
        }

    }

    public void myFinish () {
//        super.finish();
            View contentView = LayoutInflater.from(MomentPictureActivity.this).inflate(R.layout.dialog_leave_moment, null);
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