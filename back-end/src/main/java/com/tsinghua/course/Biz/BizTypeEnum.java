package com.tsinghua.course.Biz;

import com.tsinghua.course.Biz.Controller.*;

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
    USER_UPLOAD_AVATAR(UserController.class, "/user/uploadAvatar", "上传头像"),

    /** 通讯录业务类型 */
    FRIEND_FIND_STRANGER(FriendController.class, "/friend/findStranger", "搜索陌生人"),
    FRIEND_GET_STRANGER_INFO(FriendController.class, "/friend/getStrangerInfo", "获取陌生人信息"),
    FRIEND_NEW_FRIEND_REQUEST(FriendController.class, "/friend/newFriendRequest", "添加陌生用户为好友"),
    FRIEND_FIND_FRIEND(FriendController.class, "/friend/findFriend", "查找好友"),
    FRIEND_GET_STAR_FRIENDS(FriendController.class, "/friend/getStarFriends", "查看星标好友"),
    FRIEND_SET_STAR_FRIEND(FriendController.class, "/friend/setStarFriend", "设置星标好友"),
    FRIEND_CANCEL_STAR_FRIEND(FriendController.class, "/friend/cancelStarFriend", "取消星标好友"),
    FRIEND_GET_FRIEND_INFO(FriendController.class, "/friend/getFriendInfo", "查看某一位好友的信息"),
    FRIEND_REMOVE_FRIEND(FriendController.class, "/friend/removeFriend", "删除好友"),
    FRIEND_SET_FRIEND_REMARK(FriendController.class, "/friend/setFriendRemark", "设置好友备注"),
    FRIEND_GET_FRIEND_REQUEST(FriendController.class, "/friend/getFriendRequest", "查看好友申请"),
    FRIEND_CHECK_FRIEND_REQUEST(FriendController.class, "/friend/checkFriendRequest", "审核好友申请"),
    FRIEND_GET_FRIENDS(FriendController.class, "/friend/getFriends", "获取通讯录"),

    /** 聊天业务类型 */
    CHAT_GET_CHAT_LINK(ChatController.class, "/chat/getChatLink", "获取聊天关系（暂时是私聊）"),
    CHAT_QUIT_CHAT(ChatController.class, "/chat/quitChat", "退出聊天"),
    CHAT_SEND_MESSAGE(ChatController.class, "/chat/sendMessage", "发送消息"),
    CHAT_GET_CHATTINGS(ChatController.class, "/chat/getChattings", "获取所有聊天"),
    CHAT_REMOVE_MESSAGES(ChatController.class, "/chat/removeMessages", "删除聊天记录"),
    CHAT_CREATE_GROUP(ChatController.class, "/chat/createGroup", "创建群聊"),
    CHAT_GET_GROUPS(ChatController.class, "/chat/getGroups", "查看所有群"),
    CHAT_ENTER_GROUP_CHAT(ChatController.class, "/chat/enterGroupChat", "进入群聊"),
    CHAT_QUIT_GROUP_CHAT(ChatController.class, "/chat/quitGroupChat", "离开群聊界面"),
    CHAT_GET_GROUP_MEMBERS(ChatController.class, "/chat/getGroupMembers", "查看群成员"),
    CHAT_INVITE_TO_GROUP(ChatController.class, "/chat/inviteToGroup", "邀请好友进入群聊"),
    CHAT_QUIT_GROUP(ChatController.class, "/chat/quitGroup", "退出群聊"),
    CHAT_GET_HISTORY(ChatController.class, "/chat/getHistory", "查看历史记录"),

    /** 动态业务类型 */
    MOMENT_PUBLISH_MOMENT(MomentController.class, "/moment/publishMoment", "发布动态"),
    MOMENT_REMOVE_MOMENT(MomentController.class, "/moment/removeMoment", "删除动态"),
    MOMENT_GET_MOMENTS(MomentController.class, "/moment/getMoments", "查看动态列表"),
    MOMENT_LIKE_MOMENT(MomentController.class, "/moment/likeMoment", "点赞动态"),
    MOMENT_CANCEL_LIKE_MOMENT(MomentController.class, "/moment/cancelLikeMoment", "撤销点赞"),
    MOMENT_COMMENT_ON_MOMENT(MomentController.class, "/moment/commentOnMoment", "评论动态"),
    MOMENT_REMOVE_COMMENT(MomentController.class, "/moment/removeComment", "删除动态"),
    MOMENT_GET_SINGLE_MOMENT(MomentController.class, "/moment/getSingleMoment", "查看单条动态"),

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
