package com.example.course29.util;

public class GlobalVariable {
    // 拍照回传码
    public final static int CAMERA_REQUEST_CODE = 0;
    // 相册选择回传吗
    public final static int GALLERY_REQUEST_CODE = 1;
    // 当前用户
    private static String mUsername;

    public static void setGlobalUsername(String username) {
        mUsername = username;
    }

    public static String getGlobalUsername() {
        return mUsername;
    }
}
