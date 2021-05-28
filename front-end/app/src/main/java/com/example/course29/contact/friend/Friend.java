package com.example.course29.contact.friend;

public class Friend {
    private String nickname; // 昵称
    private String avatar; // 头像
    private String username; // 用户名
    private String remark; // 备注

    public Friend(String avatar, String username, String nickname, String remark) {
        this.nickname = nickname;
        this.avatar = avatar;
        this.remark = remark;
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getNickname() {
        return nickname;
    }

    public String getUsername() {
        return username;
    }

    public String getRemark() {
        return remark;
    }

}
