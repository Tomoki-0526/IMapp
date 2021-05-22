package com.tsinghua.course.Base.Model;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * @描述 用户聊天关系表
 */
@Document("ChatUserLink")
public class ChatUserLink {
    // mongodb唯一id
    String id;
    // 发送方用户名
    String from_username;
    // 接收方用户名
    String to_username;
    // 创建时间
    Date create_time;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getFromUsername() { return from_username; }
    public void setFromUsername(String from_username) { this.from_username = from_username; }

    public String getToUsername() { return to_username; }
    public void setToUsername(String to_username) { this.to_username = to_username; }

    public Date getCreateTime() { return create_time; }
    public void setCreateTime(Date create_time) { this.create_time = create_time; }
}
