package com.tsinghua.course.Biz.Controller.Params.ChatParams.out;

import com.tsinghua.course.Base.Annotation.BizType;
import com.tsinghua.course.Biz.BizTypeEnum;
import com.tsinghua.course.Biz.Controller.Params.CommonOutParams;

/**
 * @描述 发送消息给指定用户的socket出参
 */
@BizType(BizTypeEnum.CHAT_SEND_MESSAGE)
public class SendMessageOutParams extends CommonOutParams {
    // 消息内容
    String content;
    // 消息类型
    int type;
    // 接收方是否在聊天窗口
    boolean window;
    // 未读数
    int unread;

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public int getType() { return type; }
    public void setType(int type) { this.type = type; }

    public boolean getWindow() { return window; }
    public void setWindow(boolean window) { this.window = window;}

    public int getUnread() { return unread; }
    public void setUnread(int unread) { this.unread = unread; }
}
