package com.example.course29.util;

import android.content.Context;
import android.widget.Toast;

//进行一个简单的封装
public class ToastUtil {
    public static Toast mToast;
    public static void showMsg(Context context, String msg) {
        if ((mToast == null)) {
            mToast = Toast.makeText(context,msg,Toast.LENGTH_SHORT);
        }
        else {
            mToast.setText(msg);
        }
        mToast.show();
    }
}
