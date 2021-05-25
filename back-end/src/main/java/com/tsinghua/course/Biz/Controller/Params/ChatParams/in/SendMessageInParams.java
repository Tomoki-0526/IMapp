package com.tsinghua.course.Biz.Controller.Params.ChatParams.in;

import com.tsinghua.course.Base.Annotation.BizType;
import com.tsinghua.course.Base.Annotation.Required;
import com.tsinghua.course.Biz.BizTypeEnum;
import com.tsinghua.course.Biz.Controller.Params.CommonInParams;

import java.util.Date;

/**
 * @描述 发送消息给指定用户的入参
 */
@BizType(BizTypeEnum.CHAT_SEND_MESSAGE)
public class SendMessageInParams extends CommonInParams {
    // 聊天关系id
    @Required
    String link_id;

    // 接收方用户名
    @Required
    String to_username;

    // 消息内容
    @Required
    String content;

    // 发送时间
    @Required
    String send_time;

    // 消息类型
    @Required
    String type;

    public String getLinkId() { return link_id; }
    public void setLinkId(String link_id) { this.link_id = link_id; }

    public String getToUsername() { return to_username; }
    public void setToUsername(String to_username) { this.to_username = to_username; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getSendTime() { return send_time; }
    public void setSendTime(String send_time) { this.send_time = send_time; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}
