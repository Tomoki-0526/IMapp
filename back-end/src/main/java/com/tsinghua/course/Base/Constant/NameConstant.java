package com.tsinghua.course.Base.Constant;

/**
 * @描述 名称常量列表，用于记录系统中各种各样的命名
 **/
public class NameConstant {
    /** 通用名称 */
    // 应用包名称
    public static final String PACKAGE_NAME = "com.tsinghua.course";
    // 默认http内容
    public static final String DEFAULT_CONTENT = "text/html; charset=UTF-8";
    // HttpSession的名称
    public static final String HTTP_SESSION_NAME = "courseSession";
    // 定时日志名称
    public static final String TIMER_LOG = "timer";

    /** 定时任务id名 */
    public static final String TEST_JOB = "testJob";

    /** 操作系统名称 */
    // 字段
    public static final String OS_NAME = "os.name";
    // Windows
    public static final String WIN = "win";

    /** 默认分组 */
    public static final String DEFAULT_GROUP = "我的好友";

    /** 消息类型 */
    // 文本
    public static final int MESSAGE_TYPE_TEXT = 0;
    // 图片
    public static final int MESSAGE_TYPE_IMAGE = 1;
    // 音频
    public static final int MESSAGE_TYPE_AUDIO = 2;
    // 视频
    public static final int MESSAGE_TYPE_VIDEO = 3;
    // 位置信息
    public static final int MESSAGE_TYPE_LOCATION = 4;
}
