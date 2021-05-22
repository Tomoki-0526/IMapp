package com.tsinghua.course.Base.Model;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @描述 聊天条目表
 */
@Document
public class ChatItem {
    // mongodb唯一id
    String id;
    // 用户聊天关系表主键
    String link_id;
    // 发送方用户名
    String from_username;
    // 接收方用户名
    String to_username;
    // 发送方是否在窗口
    boolean from_window;
    // 接收方是否在窗口
    boolean to_window;
    // 未读数
    int unread;
    // 列表状态，是否删除
    boolean status;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getLinkId() { return link_id; }
    public void setLinkId(String link_id) { this.link_id = link_id; }

    public String getFromUsername() { return from_username; }
    public void setFromUsername(String from_username) { this.from_username = from_username; }

    public String getToUsername() { return to_username; }
    public void setToUsername(String to_username) { this.to_username = to_username; }

    public boolean getFromWindow() { return from_window; }
    public void setFromWindow(boolean from_window) { this.from_window = from_window; }

    public boolean getToWindow() { return to_window; }
    public void setToWindow(boolean to_window) { this.to_window = to_window; }

    public int getUnread() { return unread; }
    public void setUnread(int unread) { this.unread = unread; }

    public boolean getStatus() { return status; }
    public void setStatus(boolean status) { this.status = status; }
}
