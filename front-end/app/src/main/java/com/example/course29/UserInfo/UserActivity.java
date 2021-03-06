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
    // ??????????????????????????????
    private String mTempPhotoPath;
    // ???????????????Uri??????
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
                    case "??????":
                        map.put("gender", "male");
                        break;
                    case "??????":
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
        Calendar selectedDate = Calendar.getInstance();//??????????????????
        try {
//            Date date = formatter.parse(mStrBirthday == ""? "1970-01-01" : mStrBirthday);
            Date date = formatter.parse("1999-10-19");
            selectedDate.setTime(date);//????????????????????????????????????
        } catch (Exception e) {

        }
        //???????????????
        pvTime = new TimePickerBuilder(UserActivity.this, new OnTimeSelectListener() {
            public void onTimeSelect(final Date date, View v) {
                String choiceTime = formatter.format(date);//?????? String
//                long  startl = date.getTime();//?????? long
                mTvUserEditBirthday.setText(choiceTime);
            }
        }).setDate(selectedDate)//?????????????????????????????????
                .setSubmitText("??????")//??????????????????
                .setCancelText("??????")//??????????????????
                .setTitleText("???????????????")//??????
                .setType(new boolean[]{true, true, true, false, false, false})//???????????????????????????????????? true:?????? false:??????
                .isCenterLabel(false) //?????????????????????????????????label?????????false?????????item???????????????label???
                .setDividerColor(0xFF24AD9D)//?????????????????????
                .isCyclic(false)//???????????????????????? ???????????????31???????????????1??? ????????????????????????????????????????????????
                .setLineSpacingMultiplier((float) 2.5)
                .build();
    }

    private void initGenderPicker() {
        List<String> arrayList = Arrays.asList(getResources().getStringArray(R.array.gender));
        pvGender = new OptionsPickerBuilder(UserActivity.this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //?????????????????????????????????????????????
                String tx = arrayList.get(options1).toString();
                mTvUserEditGender.setText(tx);
            }
        }).setSelectOptions(0)
                .setSubmitText("??????")//??????????????????
                .setCancelText("??????")//??????????????????
                .setTitleText("???????????????")//??????
                .isCenterLabel(false) //?????????????????????????????????label?????????false?????????item???????????????label???
                .setDividerColor(0xFF24AD9D)//?????????????????????
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
                    mTvUserEditGender.setText("??????");
                    break;
                case "female":
                    mTvUserEditGender.setText("??????");
                    break;
                default:
                    mTvUserEditGender.setText("??????");
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
                != PackageManager.PERMISSION_GRANTED) {   //???????????????????????????????????????????????????????????????
            // ??????????????????????????????????????????????????????????????????????????? ??????????????????????????????
            // ??????????????????????????????????????????????????????????????????onRequestPermissionsResult????????????
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
        // ??????????????????????????????
        Intent intentToTakePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // ???????????????????????????sd???????????????
        // ??????????????????????????? ???????????????????????????temp??? ????????????????????????????????????????????????????????????????????????????????????????????????
        // File.separator??????????????????????????? ????????????????????????
        Date date = new Date();
        mTempPhotoPath = Environment.getExternalStorageDirectory() + File.separator + new Date().getTime() + ".jpeg";
        // ???????????????????????????Uri??????    *****????????????????????????????????????2*****
        /*imageUri = Uri.fromFile(new File(mTempPhotoPath));*/
        imageUri = FileProvider.getUriForFile(UserActivity.this,
                UserActivity.this.getApplicationContext().getPackageName() + ".my.provider",
                new File(mTempPhotoPath));
        //???????????????????????????????????????????????????????????????
        intentToTakePhoto.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        Log.e("111", imageUri.toString());
        startActivityForResult(intentToTakePhoto, GlobalVariable.CAMERA_REQUEST_CODE);
    }

    private void openPhotos() {
        if (ContextCompat.checkSelfPermission(UserActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {   //???????????????????????????????????????????????????????????????
            // ??????????????????????????????????????????????????????????????????????????? ??????????????????????????????
            // ??????????????????????????????????????????????????????????????????onRequestPermissionsResult????????????
            ActivityCompat.requestPermissions(UserActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    GlobalVariable.GALLERY_REQUEST_CODE);

        } else { //????????????????????????????????????????????????????????????????????????
            choosePhoto();
        }
    }

    private void choosePhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        // ?????????????????????????????????????????????????????????????????????"image/jpeg ??? image/png????????????" ?????????????????? "image/*"
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
        if (resultCode == UserActivity.RESULT_OK) {   // ????????????
            switch (requestCode) {                // ???????????????
                case GlobalVariable.CAMERA_REQUEST_CODE: {
                    try {
                        // ??????
                        startUCrop();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }
                case GlobalVariable.GALLERY_REQUEST_CODE: {
                    // ????????????
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
                    // ????????????
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
        //???????????????????????????
        Uri destinationUri = Uri.fromFile(new File(getCacheDir(), new Date().getTime() + ".jpeg"));
        UCrop uCrop = UCrop.of(imageUri, destinationUri);
        UCrop.Options options = new UCrop.Options();
        //????????????????????????????????????
        options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.ROTATE, UCropActivity.ALL);
        //??????toolbar??????
        options.setToolbarColor(ActivityCompat.getColor(this, R.color.SecondaryYellow));
        //?????????????????????
        options.setStatusBarColor(ActivityCompat.getColor(this, R.color.SecondaryYellow));
        //????????????????????????
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