package com.tsinghua.course.Base.CustomizedClass;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @描述 一个群成员项
 */
@Document("MemberItem")
public class MemberItem {
    // 头像
    String avatar;
    // 用户名
    String username;
    // 昵称
    String nickname;
    // 备注
    String remark;
    // 是否是好友
    boolean isFriend;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public boolean isFriend() {
        return isFriend;
    }

    public void setFriend(boolean friend) {
        isFriend = friend;
    }
}
