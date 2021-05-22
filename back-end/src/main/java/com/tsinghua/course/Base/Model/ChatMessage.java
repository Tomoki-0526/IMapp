package com.tsinghua.course.Base.Model;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * @描述 聊天内容详情表
 */
@Document
public class ChatMessage {
    // mongodb唯一id
    String id;
    // 用户聊天关系表主键
    String link_id;
    // 本用户名
    String username;
    // 发送方用户名
    String from_username;
    // 接收方用户名
    String to_username;
    // 内容
    String content;
    // 发送时间
    Date send_time;
    // 消息类型
    int type;
    // 是否是最后一条消息
    boolean latest;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getLinkId() { return link_id; }
    public void setLinkId(String link_id) { this.link_id = link_id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getFromUsername() { return from_username; }
    public void setFromUsername(String from_username) { this.from_username = from_username; }

    public String getToUsername() { return to_username; }
    public void setToUsername(String to_username) { this.to_username = to_username; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public Date getSendTime() { return send_time; }
    public void setSendTime(Date send_time) { this.send_time = send_time; }

    public int getType() { return type; }
    public void setType(int type) { this.type = type; }

    public boolean getLatest() { return latest; }
    public void setLatest(boolean latest) { this.latest = latest; }
}
