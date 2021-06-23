package com.example.course29.util;

public class GlobalVariable {
    // 拍照回传码
    public final static int CAMERA_REQUEST_CODE = 0;
    // 相册选择回传吗
    public final static int GALLERY_REQUEST_CODE = 1;

    public final static int RECORD_REQUEST_CODE = 2;

    public final static int GPS_REQUEST_CODE = 3;
    // 当前用户
    private static String mUsername;
    private static String mPassword;

    public static void setGlobalUsername(String username) {
        mUsername = username;
    }

    public static String getGlobalUsername() {
        return mUsername;
    }

    public static void setGlobalPassword(String password) { mPassword = password; }

    public static String getGlobalPassword() { return mPassword; }

    private static String webSocketStr;

    public static void setWebSocketStr(String webSocketStr) {
        GlobalVariable.webSocketStr = webSocketStr;
    }

    public static String getWebSocketStr() {
        return webSocketStr;
    }
}
