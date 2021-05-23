package com.tsinghua.course.Base.Constant;

/**
 * @描述 用于key的常量列表，一般对应于类中的属性名称，如User类中的username属性
 **/
public class KeyConstant {
    /** 以下为全局通用key */
    // 参数
    public static final String PARAMS = "params";
    // 路径
    public static final String PATH = "path";
    // 操作类型
    public static final String BIZ_TYPE = "bizType";
    // ID
    public static final String ID = "id";

    /** 以下为User关键key */
    // 用户名
    public static final String USERNAME = "username";
    // 密码
    public static final String PASSWORD = "password";
    // 头像
    public static final String AVATAR = "avatar";
    // 昵称
    public static final String NICKNAME = "nickname";
    // 性别
    public static final String GENDER = "gender";
    // 生日
    public static final String BIRTHDAY = "birthday";
    // 年龄
    public static final String AGE = "age";
    // 手机号码
    public static final String TELEPHONE = "telephone";
    // 个性签名
    public static final String SIGNATURE = "signature";

    /** Friendship关键key */
    // 好友用户名
    public static final String FRIEND_USERNAME = "friend_username";
    // 好友备注
    public static final String REMARK = "remark";
    // 星标
    public static final String STAR = "star";
    // 分组ID
    public static final String GROUP_ID = "group_id";

    /** FriendGroup关键key */
    // 分组
    public static final String GROUP_NAME = "group_name";

    /** FriendRequest关键key */
    // 发送方用户名
    public static final String FROM_USERNAME = "from_username";
    // 接收方用户名
    public static final String TO_USERNAME = "to_username";
    // 状态
    public static final String STATUS = "status";

    /** ChatItem关键key */
    // 聊天关系id
    public static final String LINK_ID = "link_id";
    // from_user在线状态
    public static final String FROM_WINDOW = "from_window";
    // to_user在线状态
    public static final String TO_WINDOW = "to_window";
    // 未读数
    public static final String UNREAD = "unread";

    /** ChatMessage关键key */
    // 发送时间
    public static final String SEND_TIME = "send_time";
    // 最后一条消息
    public static final String LATEST = "latest";
}
