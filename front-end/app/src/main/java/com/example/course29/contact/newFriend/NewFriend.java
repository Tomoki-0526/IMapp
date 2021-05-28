package com.example.course29.contact.newFriend;

public class NewFriend {
    private String nickname; // 昵称
    private String avatar; // 头像
    private String username; // 用户名
    private String extra; // 申请理由
    private String status; // 状态

    public NewFriend(String avatar, String username, String nickname, String extra, String status) {
        this.nickname = nickname;
        this.avatar = avatar;
        this.extra = extra;
        this.status = status;
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

    public String getExtra() {
        return extra;
    }

    public String getStatus() {
        return status;
    }
}
