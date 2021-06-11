package com.example.course29.UserInfo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.bumptech.glide.Glide;
import com.example.course29.LogonActivity;
import com.example.course29.MainActivity;
import com.example.course29.MenuActivity;
import com.example.course29.R;
import com.example.course29.util.BitmapUtil;
import com.example.course29.util.GlobalVariable;
import com.example.course29.util.HttpUtil;
import com.example.course29.util.ToastUtil;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;

import java.io.File;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserActivity extends AppCompatActivity {
    private EditText mEtUserEditNickname;
    private ImageView mIvUserEditProfile;
    private ImageView mIvUserEditReturn;
    private EditText mEtUSerEditSignature;
    private EditText mEtUserEditTelephone;
    private TextView mTvUserEditGender;
    private Button mBtnUpdateInfo;
    private TextView mTvUserEditBirthday;
    private Button mBtnUserEditChangeProfile;
    private String mStrBirthday = "";
    private TimePickerView pvTime;
    private OptionsPickerView pvGender;
    // 拍照的照片的存储位置
    private String mTempPhotoPath;
    // 照片所在的Uri地址
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
        setContentView(R.layout.activity_user);

        mEtUserEditNickname = findViewById(R.id.et_userEdit_nickname);
        mIvUserEditProfile = findViewById(R.id.iv_userEdit_profile);
        mIvUserEditReturn = findViewById(R.id.iv_userEdit_return);
        mEtUSerEditSignature = findViewById(R.id.et_userEdit_signature);
        mTvUserEditGender = findViewById(R.id.et_userEdit_gender);
        mEtUserEditTelephone = findViewById(R.id.et_userEdit_telephone);
        mTvUserEditBirthday = findViewById(R.id.et_userEdit_birthday);
        mBtnUpdateInfo = findViewById(R.id.btn_updateInfo);
        mBtnUserEditChangeProfile = findViewById(R.id.btn_userEdit_changeProfile);

        initInfo();
        initTimePicker();
        initGenderPicker();

        mTvUserEditGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pvGender != null) {
                    pvGender.show();
                }
            }
        });

        mBtnUserEditChangeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPhotos();
            }
        });

        mIvUserEditReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mBtnUpdateInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map map = new HashMap();
                switch (mTvUserEditGender.getText().toString()) {
                    case "男生":
                        map.put("gender", "male");
                        break;
                    case "女生":
                        map.put("gender", "female");
                        break;
                    default:
                        map.put("gender", "");
                        break;
                }
                map.put("birthday", mTvUserEditBirthday.getText().toString());
                map.put("signature", mEtUSerEditSignature.getText().toString());
                map.put("nickname", mEtUserEditNickname.getText().toString());
                map.put("telephone", mEtUserEditTelephone.getText().toString());

                Map res = HttpUtil.post("/user/updateInfo", map, UserActivity.this);
                if (res.get("success").toString() == "true") {
                    ToastUtil.showMsg(UserActivity.this, getResources().getString(R.string.modify_successfully));
//                overridePendingTransition(0,0);
                } else {
                    ToastUtil.showMsg(UserActivity.this,
                            res.get("msg") != null ? res.get("msg").toString() : "Unknown Error");
                }
            }
        });

        mTvUserEditBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pvTime != null) {
                    pvTime.show();
                }
            }
        });

    }

    private void initTimePicker() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        try {
//            Date date = formatter.parse(mStrBirthday == ""? "1970-01-01" : mStrBirthday);
            Date date = formatter.parse("1999-10-19");
            selectedDate.setTime(date);//指定控件初始值显示哪一天
        } catch (Exception e) {

        }
        //时间选择器
        pvTime = new TimePickerBuilder(UserActivity.this, new OnTimeSelectListener() {
            public void onTimeSelect(final Date date, View v) {
                String choiceTime = formatter.format(date);//日期 String
//                long  startl = date.getTime();//日期 long
                mTvUserEditBirthday.setText(choiceTime);
            }
        }).setDate(selectedDate)//设置系统时间为当前时间
                .setSubmitText("确定")//确定按钮文字
                .setCancelText("取消")//取消按钮文字
                .setTitleText("请选择日期")//标题
                .setType(new boolean[]{true, true, true, false, false, false})//设置年月日时分秒是否显示 true:显示 false:隐藏
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setDividerColor(0xFF24AD9D)//设置分割线颜色
                .isCyclic(false)//是否循环显示日期 例如滑动到31日自动转到1日 有个问题：不能实现日期和月份联动
                .setLineSpacingMultiplier((float) 2.5)
                .build();
    }

    private void initGenderPicker() {
        List<String> arrayList = Arrays.asList(getResources().getStringArray(R.array.gender));
        pvGender = new OptionsPickerBuilder(UserActivity.this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = arrayList.get(options1).toString();
                mTvUserEditGender.setText(tx);
            }
        }).setSelectOptions(0)
                .setSubmitText("确定")//确定按钮文字
                .setCancelText("取消")//取消按钮文字
                .setTitleText("请选择性别")//标题
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setDividerColor(0xFF24AD9D)//设置分割线颜色
                .setLineSpacingMultiplier((float) 2.5)
                .build();
        pvGender.setPicker(arrayList);
    }

    private void initInfo() {
        Map res = HttpUtil.get("/user/getInfo", UserActivity.this);
        if (res.get("success").toString() == "true") {
            Bitmap bitmap = BitmapUtil.getHttpBitmap(res.get("avatar").toString());
//            mIvUserEditProfile.setImageBitmap(bitmap);
            Glide.with(this)
                    .load(res.get("avatar").toString())
                    .into(mIvUserEditProfile);
            mEtUserEditNickname.setText(res.get("nickname").toString());
            mEtUserEditTelephone.setText(res.get("telephone").toString());
            mEtUSerEditSignature.setText(res.get("signature").toString());
            mStrBirthday = res.get("birthday").toString();
            mTvUserEditBirthday.setText(mStrBirthday);
            switch (res.get("gender").toString()) {
                case "male":
                    mTvUserEditGender.setText("男生");
                    break;
                case "female":
                    mTvUserEditGender.setText("女生");
                    break;
                default:
                    mTvUserEditGender.setText("未知");
                    break;
            }
        } else {
            ToastUtil.showMsg(UserActivity.this,
                    res.get("msg") != null ? res.get("msg").toString() : "Unknown Error");
        }
    }

    private void openCamera() {
        if (ContextCompat.checkSelfPermission(UserActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {   //权限还没有授予，需要在这里写申请权限的代码
            // 第二个参数是一个字符串数组，里面是你需要申请的权限 可以设置申请多个权限
            // 最后一个参数是标志你这次申请的权限，该常量在onRequestPermissionsResult中使用到
            ActivityCompat.requestPermissions(UserActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    GlobalVariable.CAMERA_REQUEST_CODE);

        } else if (ContextCompat.checkSelfPermission(UserActivity.this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(UserActivity.this,
                    new String[]{Manifest.permission.CAMERA},
                    GlobalVariable.CAMERA_REQUEST_CODE);
        } else {
            takePhoto();
        }
    }

    private void takePhoto() {
        // 跳转到系统的拍照界面
        Intent intentToTakePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 指定照片存储位置为sd卡本目录下
        // 这里设置为固定名字 这样就只会只有一张temp图 如果要所有中间图片都保存可以通过时间或者加其他东西设置图片的名称
        // File.separator为系统自带的分隔符 是一个固定的常量
        Date date = new Date();
        mTempPhotoPath = Environment.getExternalStorageDirectory() + File.separator + new Date().getTime() + ".jpeg";
        // 获取图片所在位置的Uri路径    *****这里为什么这么做参考问题2*****
        /*imageUri = Uri.fromFile(new File(mTempPhotoPath));*/
        imageUri = FileProvider.getUriForFile(UserActivity.this,
                UserActivity.this.getApplicationContext().getPackageName() + ".my.provider",
                new File(mTempPhotoPath));
        //下面这句指定调用相机拍照后的照片存储的路径
        intentToTakePhoto.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        Log.e("111", imageUri.toString());
        startActivityForResult(intentToTakePhoto, GlobalVariable.CAMERA_REQUEST_CODE);
    }

    private void openPhotos() {
        if (ContextCompat.checkSelfPermission(UserActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {   //权限还没有授予，需要在这里写申请权限的代码
            // 第二个参数是一个字符串数组，里面是你需要申请的权限 可以设置申请多个权限
            // 最后一个参数是标志你这次申请的权限，该常量在onRequestPermissionsResult中使用到
            ActivityCompat.requestPermissions(UserActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    GlobalVariable.GALLERY_REQUEST_CODE);

        } else { //权限已经被授予，在这里直接写要执行的相应方法即可
            choosePhoto();
        }
    }

    private void choosePhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        // 如果限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型" 所有类型则写 "image/*"
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, GlobalVariable.GALLERY_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        Log.e("code",String.valueOf(requestCode));

        if (requestCode == GlobalVariable.CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takePhoto();
            } else {
                // Permission Denied
                ToastUtil.showMsg(UserActivity.this, "Permission Denied");
            }
        }

        if (requestCode == GlobalVariable.GALLERY_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                choosePhoto();
            } else {
                // Permission Denied
                ToastUtil.showMsg(UserActivity.this, "Permission Denied");
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == UserActivity.RESULT_OK) {   // 回传成功
            switch (requestCode) {                // 选择请求码
                case GlobalVariable.CAMERA_REQUEST_CODE: {
                    try {
                        // 裁剪
                        startUCrop();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }
                case GlobalVariable.GALLERY_REQUEST_CODE: {
                    // 获取图片
                    try {
                        imageUri = data.getData();
                        if (imageUri != null) {
                            startUCrop();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }
                case UCrop.REQUEST_CROP: {
                    // 裁剪照片
                    final Uri croppedUri = UCrop.getOutput(data);
                    Log.e("uri",croppedUri.toString());
                    try {
                        if(croppedUri!=null) {
                            Glide.with(this)
                                    .load(croppedUri.toString())
                                    .into(mIvUserEditProfile);
                            File file = new File(new URI(croppedUri.toString()));
                            postProfile(file);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }

                case UCrop.RESULT_ERROR: {
                    final Throwable cropError = UCrop.getError(data);
                    Log.i("RESULT_ERROR","UCrop_RESULT_ERROR");
                    break;
                }
            }
        }
    }

    private void startUCrop(){
        //裁剪后保存到文件中
        Uri destinationUri = Uri.fromFile(new File(getCacheDir(), new Date().getTime() + ".jpeg"));
        UCrop uCrop = UCrop.of(imageUri, destinationUri);
        UCrop.Options options = new UCrop.Options();
        //设置裁剪图片可操作的手势
        options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.ROTATE, UCropActivity.ALL);
        //设置toolbar颜色
        options.setToolbarColor(ActivityCompat.getColor(this, R.color.orangeRed));
        //设置状态栏颜色
        options.setStatusBarColor(ActivityCompat.getColor(this, R.color.orangeRed));
        //是否能调整裁剪框
        // options.setFreeStyleCropEnabled(true);
        uCrop.withOptions(options);
        uCrop.start(this);
    }

    private void postProfile (File file) {
        List<File> files = new ArrayList<>();
        files.add(file);
        Map res = HttpUtil.postFiles("/user/uploadAvatar",null, files, "avatar",UserActivity.this);
        if (res.get("success").toString() == "true") {
            ToastUtil.showMsg(UserActivity.this, getResources().getString(R.string.modify_successfully));
//                overridePendingTransition(0,0);
        }
        else {
            ToastUtil.showMsg(UserActivity.this,
                    res.get("msg") != null?res.get("msg").toString() : "Unknown Error");
        }
    }
}