package com.example.course29.UserInfo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.example.course29.LogonActivity;
import com.example.course29.MainActivity;
import com.example.course29.MenuActivity;
import com.example.course29.R;
import com.example.course29.util.BitmapUtil;
import com.example.course29.util.HttpUtil;
import com.example.course29.util.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UserActivity extends AppCompatActivity {
    private EditText mEtUserEditNickname;
    private ImageView mIvUserEditProfile;
    private ImageView mIvUserEditReturn;
    private EditText mEtUSerEditSignature;
    private EditText mEtUserEditTelephone;
    private Spinner mSpnUserEditGender;
    private Button mBtnUpdateInfo;
    private TextView mTvUserEditBirthday;
    private String mStrGender = "";
    private String mStrBirthday = "";
    private TimePickerView pvTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        mEtUserEditNickname = findViewById(R.id.et_userEdit_nickname);
        mIvUserEditProfile = findViewById(R.id.iv_userEdit_profile);
        mIvUserEditReturn = findViewById(R.id.iv_userEdit_return);
        mEtUSerEditSignature = findViewById(R.id.et_userEdit_signature);
        mSpnUserEditGender = findViewById(R.id.spn_userEdit_gender);
        mEtUserEditTelephone = findViewById(R.id.et_userEdit_telephone);
        mTvUserEditBirthday = findViewById(R.id.et_userEdit_birthday);
        mBtnUpdateInfo = findViewById(R.id.btn_updateInfo);

        initInfo();
        initTimePicker();

        mIvUserEditReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mSpnUserEditGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mStrGender = getResources().getStringArray(R.array.gender)[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        mBtnUpdateInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map map = new HashMap();
                switch (mStrGender){
                    case "男生":
                        map.put("gender","male");
                        break;
                    case "女生":
                        map.put("gender","female");
                        break;
                    default:
                        map.put("gender","");
                        break;
                }
                map.put("birthday",mTvUserEditBirthday.getText().toString());
                map.put("signature",mEtUSerEditSignature.getText().toString());
                map.put("nickname",mEtUserEditNickname.getText().toString());
                map.put("telephone",mEtUserEditTelephone.getText().toString());
                Log.e("map",map.toString());

                Map res = HttpUtil.post("/user/updateInfo",map, UserActivity.this);
                if (res.get("success").toString() == "true") {
                    ToastUtil.showMsg(UserActivity.this, getResources().getString(R.string.modify_successfully));
//                overridePendingTransition(0,0);
                }
                else {
                    ToastUtil.showMsg(UserActivity.this,
                            res.get("msg") != null?res.get("msg").toString() : "Unknown Error");
                }
            }
        });

        mTvUserEditBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pvTime != null) {
                    ToastUtil.showMsg(UserActivity.this,"pvshow");
                    pvTime.show();
                }
            }
        });

    }
    private void initTimePicker () {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        try {
//            Date date = formatter.parse(mStrBirthday == ""? "1970-01-01" : mStrBirthday);
            Date date = formatter.parse("1999-10-19");
            selectedDate.setTime(date);//指定控件初始值显示哪一天
        }catch (Exception e){

        }
        //时间选择器
        pvTime = new TimePickerBuilder(UserActivity.this, new OnTimeSelectListener() {
            public void onTimeSelect(final Date date, View v) {
                String choiceTime =  formatter.format(date);//日期 String
//                long  startl = date.getTime();//日期 long
                mTvUserEditBirthday.setText(choiceTime);
            }
        }).setDate(selectedDate)//设置系统时间为当前时间
                .setSubmitText("确定")//确定按钮文字
                .setCancelText("取消")//取消按钮文字
                .setTitleText("请选择")//标题
                .setType(new boolean[]{true, true, true, false, false, false})//设置年月日时分秒是否显示 true:显示 false:隐藏
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setDividerColor(0xFF24AD9D)//设置分割线颜色
                .isCyclic(false)//是否循环显示日期 例如滑动到31日自动转到1日 有个问题：不能实现日期和月份联动
                .setLineSpacingMultiplier((float) 2.5)
                .build();
    }
    private void initInfo () {
        Map res = HttpUtil.get("/user/getInfo", UserActivity.this);
        if (res.get("success").toString() == "true") {
            Bitmap bitmap = BitmapUtil.getHttpBitmap(res.get("avatar").toString());
            mIvUserEditProfile.setImageBitmap(bitmap);
            mEtUserEditNickname.setText(res.get("nickname").toString());
            mEtUserEditTelephone.setText(res.get("telephone").toString());
            mEtUSerEditSignature.setText(res.get("signature").toString());
            mStrBirthday = res.get("birthday").toString();
            mTvUserEditBirthday.setText(mStrBirthday);
            switch (res.get("gender").toString()) {
                case "male":
                    mSpnUserEditGender.setSelection(1,true);
                    mStrGender="男生";
                    break;
                case "female":
                    mSpnUserEditGender.setSelection(2,true);
                    mStrGender="女生";
                    break;
                default:
                    mSpnUserEditGender.setSelection(0,true);
                    mStrGender="未知";
                    break;
            }
        }
        else {
            ToastUtil.showMsg(UserActivity.this,
                    res.get("msg") != null?res.get("msg").toString() : "Unknown Error");
        }
    }
}