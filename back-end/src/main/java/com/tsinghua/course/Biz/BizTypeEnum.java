package com.tsinghua.course.Biz;

import com.tsinghua.course.Biz.Controller.FriendController;
import com.tsinghua.course.Biz.Controller.TestController;
import com.tsinghua.course.Biz.Controller.TimerController;
import com.tsinghua.course.Biz.Controller.UserController;

/**
 * @描述 业务类型枚举，所有的业务类型都需要枚举在此类中
 **/
public enum BizTypeEnum {
    /** 以下为用户业务类型 */
    USER_LOGIN(UserController.class, "/user/login", "用户登录"),
    USER_REGISTER(UserController.class, "/user/register", "用户注册"),
    USER_LOGOUT(UserController.class, "/user/logout", "用户登出"),
    USER_MODIFY_PASSWORD(UserController.class, "/user/modifyPassword", "修改密码"),
    USER_GET_INFO(UserController.class, "/user/getInfo", "查看个人信息"),
    USER_UPDATE_INFO(UserController.class, "/user/updateInfo", "更新个人信息"),

    /** 通讯录业务类型 */
    FRIEND_FIND_STRANGER(FriendController.class, "/friend/findStranger", "搜索陌生人"),
    FRIEND_GET_STRANGER_INFO(FriendController.class, "/friend/getStrangerInfo", "获取陌生人信息"),

    /** 定时任务业务测试 */
    LOG_TEST(TimerController.class, null, "定时日志测试"),

    /** 测试业务，在书写正式代码时可以删除，在书写正式代码前先运行测试业务，如果测试业务无问题说明各模块正常 */
    LOGIN_TEST(TestController.class, "/test/loginPermission", "登录控制测试"),
    ADMIN_TEST(TestController.class, "/test/adminPermission", "管理员权限控制测试"),
    REDIS_TEST(TestController.class, "/test/redis", "redis缓存测试"),
    TIMER_TEST(TestController.class, "/test/timer", "定时器测试"),
    ERROR_TEST(TestController.class, "/test/error", "内部报错测试"),
    FILE_UPLOAD_TEST(TestController.class, "/test/upload", "文件上传测试"),
    FILE_DOWNLOAD_TEST(TestController.class, "/test/url", "获取文件下载的路径"),
    MULTI_RETURN_TEST(TestController.class, "/test/multiParams", "返回多个参数的测试"),
    MONGODB_TEST(TestController.class, "/test/mongodb", "mongodb数据库功能测试")
    ;

    BizTypeEnum(Class<?> controller, String httpPath, String description) {
        this.controller = controller;
        this.description = description;
        this.httpPath = httpPath;
    }

    /** 执行业务具体的类 */
    Class<?> controller;
    /** 业务对应的http请求路径 */
    String httpPath;
    /** 业务描述 */
    String description;

    public Class<?> getControllerClass() {
        return controller;
    }

    public String getDescription() {
        return description;
    }

    public String getHttpPath() {
        return httpPath;
    }
}
