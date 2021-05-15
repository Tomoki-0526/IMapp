package com.tsinghua.course.Base.CustomizedClass;

/**
 * @描述 FriendItem 用于返回给客户端的一个好友的条目
 **/
public class FriendItem {
    // 好友用户名
    String friend_username;
    // 好友头像
    String friend_avatar;
    // 好友昵称
    String friend_nickname;
    // 好友备注
    String friend_remark;

    public String getFriendUsername() { return friend_username; }
    public void setFriendUsername(String friend_username) { this.friend_username = friend_username; }

    public String getFriendAvatar() { return friend_avatar; }
    public void setFriendAvatar(String friend_avatar) { this.friend_avatar = friend_avatar; }

    public String getFriendNickname() { return friend_nickname; }
    public void setFriendNickname(String friend_nickname) { this.friend_nickname = friend_nickname; }

    public String getFriendRemark() { return friend_remark; }
    public void setFriendRemark(String friend_remark) { this.friend_remark = friend_remark; }
}
