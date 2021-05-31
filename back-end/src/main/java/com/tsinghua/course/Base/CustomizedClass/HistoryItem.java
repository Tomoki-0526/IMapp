package com.tsinghua.course.Base.CustomizedClass;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * @描述 历史消息记录
 */
@Document("HistoryItem")
public class HistoryItem {
    // 消息id
    String msg_id;
    // 发送方头像
    String from_avatar;
    // 发送方用户名
    String from_username;
    // 消息内容
    String content;
    // 消息类型
    int type;
    // 发送时间
    String send_time;

    public String getMsgId() { return msg_id; }
    public void setMsgId(String msg_id) { this.msg_id = msg_id; }

    public String getFromAvatar() { return from_avatar; }
    public void setFromAvatar(String from_avatar) { this.from_avatar = from_avatar; }

    public String getFromUsername() { return from_username; }
    public void setFromUsername(String from_username) { this.from_username = from_username; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public int getType() { return type; }
    public void setType(int type) { this.type = type; }

    public String getSendTime() { return send_time; }
    public void setSendTime(String send_time) { this.send_time = send_time; }
}
