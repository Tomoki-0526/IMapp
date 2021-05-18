package com.tsinghua.course.Base.CustomizedClass;

/**
 * @描述 FriendRequestItem 前端用好友申请条目
 **/
public class FriendRequestItem {
    // 用户名
    String from_username;
    // 头像
    String from_avatar;
    // 昵称
    String from_nickname;
    // 申请理由
    String extra;
    // 状态
    int status;

    public String getFromUsername() { return from_username; }
    public void setFromUsername(String from_username) { this.from_username = from_username; }

    public String getFromNickname() { return from_nickname; }
    public void setFromNickname(String from_nickname) { this.from_nickname = from_nickname; }

    public String getFromAvatar() { return from_avatar; }
    public void setFromAvatar(String from_avatar) { this.from_avatar = from_avatar; }

    public String getExtra() { return extra; }
    public void setExtra(String extra) { this.extra = extra; }

    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }
}
