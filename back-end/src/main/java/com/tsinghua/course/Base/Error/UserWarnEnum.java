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
    MISMATCHED_PASSWORD("UserWarn009", "旧密码不匹配"),
    INVALID_NEW_PASSWORD("UserWarn010", "新密码不合法"),
    MISMATCHED_NEW_PASSWORD("UserWarn011", "两次输入密码不一致"),
    INVALID_GENDER("UserWarn012", "性别不合法"),
    INVALID_BIRTHDAY("UserWarn013", "生日不合法"),
    INVALID_TELEPHONE("UserWarn014", "手机号不合法"),
    FIND_STRANGER_NO_RESULT("UserWarn015", "未找到用户"),
    FIND_FRIEND_NO_RESULT("UserWarn016", "未找到好友"),
    NO_STAR_FRIENDS("UserWarn017", "没有特别关心好友"),
    NO_FRIEND_REQUEST("UserWarn018", "没有新好友申请"),
    NO_SUCH_GROUP("UserWarn019", "没有该分组"),
    DUPLICATE_GROUP("UserWarn020", "分组名重复"),
    NEED_NICKNAME("UserWarn021", "缺少昵称"),
    NEED_TELEPHONE("UserWarn022", "缺少手机号"),
    ALREADY_FRIEND("UserWarn023", "对方已经是您的好友"),
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
