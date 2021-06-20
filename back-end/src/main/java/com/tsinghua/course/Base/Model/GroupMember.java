package com.tsinghua.course.Base.Model;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @描述 群聊成员
 */
@Document("GroupMember")
public class GroupMember {
    // mongodb唯一id
    String id;
    // 群聊id
    String groupLinkId;
    // 用户名
    String username;
    // 当前是否在窗口
    boolean inWindow;
    // 未读数
    int unread;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupLinkId() {
        return groupLinkId;
    }

    public void setGroupLinkId(String groupLinkId) {
        this.groupLinkId = groupLinkId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isInWindow() {
        return inWindow;
    }

    public void setInWindow(boolean inWindow) {
        this.inWindow = inWindow;
    }

    public int getUnread() {
        return unread;
    }

    public void setUnread(int unread) {
        this.unread = unread;
    }
}
