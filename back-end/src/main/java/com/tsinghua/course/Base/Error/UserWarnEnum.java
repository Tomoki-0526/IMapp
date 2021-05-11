package com.tsinghua.course.Base.Error;

/**
 * @描述 用户操作警告枚举
 **/
public enum UserWarnEnum implements ExceptionInterface {
    LOGIN_FAILED("UserWarn001", "用户或密码不正确"),
    NEED_LOGIN("UserWarn002", "用户未登录或登录已过期"),
    PERMISSION_DENIED("UserWarn003", "无权限访问对应内容"),
    NEED_USERNAME("UserWarn004", "缺少用户名"),
    INVALID_USERNAME("UserWarn005", "用户名不合法"),
    DUPLICATE_USERNAME("UserWarn006", "用户名重复"),
    NEED_PASSWORD("UserWarn007", "缺少密码"),
    INVALID_PASSWORD("UserWarn008", "密码不合法"),
    NEED_OLD_PASSWORD("UserWarn009", "缺少旧密码"),
    MISMATCHED_PASSWORD("UserWarn010", "旧密码不匹配"),
    NEED_NEW_PASSWORD("UserWarn011", "缺少新密码"),
    INVALID_NEW_PASSWORD("UserWarn012", "新密码不合法"),
    NEED_CONFIRM_PASSWORD("UserWarn013", "缺少确认密码"),
    MISMATCHED_NEW_PASSWORD("UserWarn014", "两次输入密码不一致"),
    NEED_AVATAR("UserWarn015", "缺少头像"),
    NEED_NICKNAME("UserWarn016", "缺少昵称"),
    ;

    UserWarnEnum(String code, String msg) {
        errorCode = code;
        errorMsg = msg;
    }

    private String errorCode;
    private String errorMsg;
    @Override
    public String getErrorCode() {
        return errorCode;
    }

    @Override
    public String getErrorMessage() {
        return errorMsg;
    }
}
